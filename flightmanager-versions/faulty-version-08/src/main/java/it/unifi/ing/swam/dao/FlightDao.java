package it.unifi.ing.swam.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.unifi.ing.swam.model.Airport;
import it.unifi.ing.swam.model.Booking;
import it.unifi.ing.swam.model.Flight;

@RequestScoped
public class FlightDao implements Serializable {

	static final long serialVersionUID = 1L;

	@PersistenceContext
	private EntityManager entityManager;

	public void save(Flight flight) {
		if (flight.getId() != null)
			this.entityManager.merge(flight);
		else
			this.entityManager.persist(flight);
	}

	public void deleteOld(Flight flight) {
		this.entityManager.remove(this.entityManager.contains(flight) ? flight : this.entityManager.merge(flight));
	}

	public void delete(Flight flight) {
		List<Booking> bookingToDelete = this.entityManager
				.createQuery("SELECT b " + "FROM Booking b " + "WHERE b.flight.id = :flightId", Booking.class)
				.setParameter("flightId", flight.getId()).getResultList();
		for (Booking booking : bookingToDelete) {
			this.entityManager.remove(booking);
		}
		this.entityManager.remove(this.entityManager.contains(flight) ? flight : this.entityManager.merge(flight));

	}

	// TODO restituisce i dati completi del volo
	public Flight getFlightData(Long flightId) {
		return entityManager.find(Flight.class, flightId);
	}

	public Flight findById(Long flightId) {
		return entityManager.find(Flight.class, flightId);
	}

	public Flight findByFlightNumber(String flightNumber) {
		List<Flight> result = this.entityManager
				.createQuery("SELECT f " + "FROM Flight f " + "WHERE f.flightNumber = :flightNumber", Flight.class)
				.setParameter("flightNumber", flightNumber).setMaxResults(1).getResultList();

		if (result.isEmpty()) {
			return null;
		}

		return result.get(0);
	}

	public List<Flight> getAllFlights(Date fromDate) {
		List<Flight> result = this.entityManager
				.createQuery("SELECT f " + "FROM Flight f " + "WHERE f.departureDate >= :fromDate "
						+ "ORDER BY f.departureDate asc", Flight.class)
				.setParameter("fromDate", fromDate).getResultList();
		if (result.isEmpty()) {
			return null;
		}
		return result;
	}

	public List<Flight> getFlights(Airport source, Airport destination, Date date) {
		List<Flight> result = this.entityManager.createQuery("SELECT f " + "FROM Flight f "
				+ "WHERE f.sourceAirport.id = :source and f.destinationAirport.id = :destination and f.departureDate = :date",
				Flight.class).setParameter("source", source.getId()).setParameter("destination", destination.getId())
				.setParameter("date", date).getResultList();

		if (result.isEmpty()) {
			return null;
		}

		return result;
	}

	public List<Flight> getFlightsFromDate(Airport source, Airport destination, Date fromDate) {
		List<Flight> result = this.entityManager.createQuery("SELECT f " + "FROM Flight f "
				+ "WHERE f.sourceAirport.id = :source and f.destinationAirport.id = :destination and f.departureDate >= :fromDate "
				+ "ORDER BY f.departureDate asc", Flight.class).setParameter("source", source.getId())
				.setParameter("destination", destination.getId()).setParameter("fromDate", fromDate).getResultList();

		if (result.isEmpty()) {
			return null;
		}

		return result;
	}

	public List<Flight> getFlights(String source, String destination, Date date) {
		List<Flight> result = this.entityManager.createQuery("SELECT f " + "FROM Flight f, Airport a1, Airport a2 "
				+ "WHERE f.sourceAirport.id = a1.id and a1.cityName = :source and f.destinationAirport.id = a2.id and a2.cityName = :destination and departureDate = :date "
				+ "ORDER BY f.departureDate asc", Flight.class).setParameter("source", source)
				.setParameter("destination", destination).setParameter("date", date).getResultList();
		if (result.isEmpty()) {
			return new ArrayList<>();
		}
		return result;
	}

	public List<Flight> getFlightsFromDestination(Airport destination, Date fromDate) {
		List<Flight> result = this.entityManager.createQuery("SELECT f " + "FROM Flight f, Airport a2 "
				+ "WHERE f.destinationAirport.id = a2.id and a2 = :destination and f.departureDate >= :fromDate "
				+ "ORDER BY f.departureDate asc", Flight.class).setParameter("destination", destination)
				.setParameter("fromDate", fromDate).getResultList();
		if (result.isEmpty()) {
			return null;
		}
		return result;
	}

	public List<Flight> getFlightsFromSource(Airport source, Date fromDate) {
		List<Flight> result = this.entityManager
				.createQuery("SELECT f " + "FROM Flight f, Airport a1 "
						+ "WHERE f.sourceAirport = a1.id and a1 = :source and f.departureDate >= :fromDate "
						+ "ORDER BY f.departureDate asc", Flight.class)
				.setParameter("source", source).setParameter("fromDate", fromDate).getResultList();
		if (result.isEmpty()) {
			return null;
		}
		return result;
	}

	public List<String> getSourceAirports() {
		List<String> result = this.entityManager.createQuery(
				"SELECT DISTINCT(a.cityName) " + "FROM Flight f, Airport a "
						+ "WHERE f.sourceAirport = a.id and f.departureDate >= :today " + "ORDER BY a.cityName asc",
				String.class).setParameter("today", new Date()).getResultList();
		if (result.isEmpty()) {
			return null;
		}
		return result;
	}

	public List<String> getDestinationAirports() {
		List<String> result = this.entityManager
				.createQuery("SELECT DISTINCT(a.cityName) " + "FROM Flight f, Airport a "
						+ "WHERE f.destinationAirport = a.id " + "ORDER BY a.cityName asc", String.class)
				.getResultList();
		if (result.isEmpty()) {
			return null;
		}
		return result;
	}

	public List<String> getDestinationAirportsFromSource(String source) {
		List<String> result = this.entityManager.createQuery("SELECT DISTINCT(a.cityName) "
				+ "FROM Flight f, Airport a1, Airport a "
				+ "WHERE f.sourceAirport = a1.id and a1.cityName = :source and f.destinationAirport = a.id and f.departureDate >= :today "
				+ "ORDER BY a.cityName asc", String.class).setParameter("source", source)
				.setParameter("today", new Date()).getResultList();
		if (result.isEmpty()) {
			return null;
		}
		return result;
	}

	public List<Date> getAvailableDateFromSourceToDestination(String source, String destination) {
		List<Date> result = this.entityManager.createQuery("SELECT DISTINCT(f.departureDate) "
				+ "FROM Flight f, Airport a1, Airport a "
				+ "WHERE f.sourceAirport = a1.id and a1.cityName = :source and f.destinationAirport = a.id and a.cityName = :destination and f.departureDate >= :today "
				+ "ORDER BY f.departureDate asc", Date.class).setParameter("source", source)
				.setParameter("destination", destination).setParameter("today", new Date()).getResultList();
		if (result.isEmpty()) {
			return null;
		}
		return result;
	}

}
