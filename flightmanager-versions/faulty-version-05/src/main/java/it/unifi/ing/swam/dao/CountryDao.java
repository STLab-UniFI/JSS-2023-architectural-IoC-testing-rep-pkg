package it.unifi.ing.swam.dao;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.unifi.ing.swam.model.Country;

@RequestScoped
public class CountryDao implements Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@PersistenceContext
    private EntityManager entityManager;

    public void save(Country country) {
        if(country.getId() != null)
            this.entityManager.merge(country);
        else
            this.entityManager.persist(country);
    }

    public void delete(Country country) {
        this.entityManager.remove(
                this.entityManager.contains(country) ? country : this.entityManager.merge(country));
    }

    public Country findById(Long countryId) {
        return this.entityManager.find(Country.class, countryId);
    }

    public List<Country> getAllCountries() {
        List<Country> result =  this.entityManager
                .createQuery("SELECT c "
                        + "FROM Country c ", Country.class)
                .getResultList();

        if(result.isEmpty()) {
            return null;
        }

        return result;
    }

}

