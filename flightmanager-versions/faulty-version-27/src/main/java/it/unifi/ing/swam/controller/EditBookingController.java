package it.unifi.ing.swam.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import it.unifi.ing.swam.components.BookingSessionComponent;
import it.unifi.ing.swam.components.LoggedUserComponent;
import it.unifi.ing.swam.dao.BookingDao;
import it.unifi.ing.swam.dao.FlightDao;
import it.unifi.ing.swam.model.Booking;
import it.unifi.ing.swam.model.Flight;
import it.unifi.ing.swam.model.Passenger;

@Model
@SessionScoped // probabilmente non serve che sia di Session anche questo => Zombie?
@Named
public class EditBookingController implements Serializable {

	static final long serialVersionUID = 1987533787032L;

	@Inject
	private BookingDao bookingDao;

	@Inject
	private FlightDao flightDao;

	@Inject
	private BookingSessionComponent reservationSession;

	@PostConstruct
	public void init() {
		System.out.println("[OPEN] - Componente " + this.toString() + " avviato");
	}

	@PreDestroy
	public void shutDown() {
		System.out.println("[CLOSE] - Componente " + this.toString() + " distrutto");
	}

	private Booking booking;

	private List<Passenger> passengers;

	private boolean justUpdatePassengers;

	private boolean justDeleteReservation;

	// ADDED For Booking-List Page
	@Inject
	private LoggedUserComponent loggedUser;

	private List<Booking> bookingList;

	public List<Booking> getBookingList() {
		if (bookingList == null)
			bookingList = bookingDao.getAllBookingPerUser(loggedUser.getUserID());

		return bookingList;
	}

	public String viewBooking(Booking booking) {
		this.booking = booking;
		this.reservationSession.setId(booking.getId());
		return "booking-view?faces-redirect=true";
	}

	// END Addition

	public Booking getBooking() {
		if (booking == null || booking.getId() != reservationSession.getId()) {
			try {
				Long id = Long.valueOf(reservationSession.getId());
				booking = bookingDao.findById(id);
				passengers = null;
			} catch (NumberFormatException nfe) {
				throw new IllegalArgumentException("id not a number");
			}
		}
		return booking;
	}

	public List<Passenger> getPassengers() {
		if (passengers == null) {
			passengers = getBooking().getPassengers();
		}
		return passengers;
	}

	public void setPassengers(List<Passenger> passengers) {
		this.passengers = passengers;
	}

	public boolean isJustUpdatePassengers() {
		return justUpdatePassengers;
	}

	public void setJustUpdatePassengers(boolean justUpdatePassengers) {
		this.justUpdatePassengers = justUpdatePassengers;
	}

	public boolean isJustDeleteReservation() {
		return justDeleteReservation;
	}

	public void setJustDeleteReservation(boolean justDeleteReservation) {
		this.justDeleteReservation = justDeleteReservation;
	}

	@Transactional
	public String updatePassengers() {
		booking.setPassengers(passengers);
		bookingDao.save(booking);
		justUpdatePassengers = true;
		return "update-passengers-success?faces-redirect=true";
	}

	@Transactional
	public String deleteReservation() {
		bookingDao.delete(booking);
		Flight flight = booking.getFlight();
		int nPassengers = booking.getPassengers().size();
		Long id = flight.getId();
		Flight f = flightDao.findById(id);
		f.setReservedSeats(f.getReservedSeats() - nPassengers);
		flightDao.save(f);

		reservationSession.setId(null);
		booking = null;
		passengers = null;
		justDeleteReservation = true;

		return "delete-booking-success?faces-redirect=true";
	}
}
