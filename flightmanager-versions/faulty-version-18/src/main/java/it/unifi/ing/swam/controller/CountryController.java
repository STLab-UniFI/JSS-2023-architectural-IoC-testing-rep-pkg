package it.unifi.ing.swam.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import it.unifi.ing.swam.dao.CountryDao;
import it.unifi.ing.swam.model.Country;
import it.unifi.ing.swam.model.ModelFactory;

@Named
@RequestScoped
public class CountryController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CountryDao countryDao;

	private List<Country> countryList;

	private Country country;
	
	@PostConstruct
	private void init() {
		country = ModelFactory.country();
	}
	
	public String goToCountryListPage() {
		return "country-list?faces-redirect=true";
	}

	public String returnHome() {
		return "administrator-page?faces-redirect=true";
	}

	public List<Country> getCountryList() {
		if (countryList == null)
			countryList = countryDao.getAllCountries();
		return countryList;
	}

	public String startCountryAddition() {
		return "add-country?faces-redirect=true";
	}


	@Transactional
	public String saveCountry() throws IOException {
		countryDao.save(country);
		return returnHome();
	}

	public Country getCountry() {
		return country;
	}

}
