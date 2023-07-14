package it.unifi.ing.swam.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.unifi.ing.swam.model.Airport;

@RequestScoped
public class AirportDao implements Serializable{

	static final long serialVersionUID = 120938791L;
	
	private int debugNumber = 10;

	@PersistenceContext
	private EntityManager entityManager;

	public void save(Airport airport) {
		if(airport.getId() != null)
			this.entityManager.merge(airport);
		else
			this.entityManager.persist(airport);
	}
	
	public void delete(Airport airport) {
		this.entityManager.remove(
				this.entityManager.contains(airport) ? airport : this.entityManager.merge(airport));
	}
	
	public Airport findById(Long airportId) {
		return this.entityManager.find(Airport.class, airportId);
	}
	
	public List<Airport> getAllAirports() {
		List<Airport> result =  this.entityManager
						.createQuery("SELECT a "
									+ "FROM Airport a ", Airport.class)
						.getResultList();
		
		if(result.isEmpty()) {
			return null;
		}
		
		return result;
	}
	
	public List<String> getAllAirportsNames() {
		List<String> result =  this.entityManager
						.createQuery("SELECT DISTINCT(a.cityName) "
									+ "FROM Airport a "
									+ "ORDER BY a.cityName asc", String.class)
						.getResultList();
		
		if(result.isEmpty()) {
			return null;
		}
		
		return result;
	}
	
	public Airport getAirport(Airport airport){
		List<Airport> result =  this.entityManager
				.createQuery("SELECT a "
							+ "FROM Airport a "
							+ "WHERE a.id = :idAirport", Airport.class)
				.setParameter("idAirport", airport.getId())
				.setMaxResults(1)
				.getResultList();

		if(result.isEmpty()) {
			return null;
		}
		
		return result.get(0);
	}

	public int getDebugNumber() {
		return debugNumber;
	}

	public void setDebugNumber(int debugNumber) {
		this.debugNumber = debugNumber;
	}
}