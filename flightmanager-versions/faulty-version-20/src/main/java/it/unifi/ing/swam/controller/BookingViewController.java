package it.unifi.ing.swam.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import it.unifi.ing.swam.components.BookingListComponent;
import it.unifi.ing.swam.components.LoggedUserComponent;
import it.unifi.ing.swam.components.editBookingComponent;
import it.unifi.ing.swam.dao.BookingDao;
import it.unifi.ing.swam.dao.FlightDao;
import it.unifi.ing.swam.model.Booking;
import it.unifi.ing.swam.model.Flight;
import it.unifi.ing.swam.model.ModelFactory;
import it.unifi.ing.swam.model.Passenger;

// TODO:
/*
 * 3- incapsulare la lista di booking in un componente di tipo session
 */

@ConversationScoped
@Named
public class BookingViewController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int maxMinutesConversation = 15;

	@Inject
	private Conversation conversation;

	@Inject
	private LoggedUserComponent loggedUserComponent;

	@Inject
	private editBookingComponent editBookingComponent;

	@Inject
	private BookingListComponent bookingListComponent;

	@Inject
	private BookingDao bookingDao;

	@Inject
	private FlightDao flightDao;

	private String error;

	private Booking selectedBooking;

	@PostConstruct
	public void init() {
		selectedBooking = ModelFactory.reservation();
	}

	public boolean isLoggedIn() {
		return loggedUserComponent.isLoggedIn();
	}

	public String login() {
		selectedBooking = bookingDao.searchReservation(selectedBooking.getReservationId(), selectedBooking.getEmail());
		if (selectedBooking == null) {
			setError("Login failed! Retry");
			return "booking-login";
		}

		start();
		return "booking-view?faces-redirect=true";
	}

	public String logout() {
		stop();
		return "index?faces-redirect=true";
	}

	public void start() {
		if (conversation.isTransient()) {
			conversation.begin();
			conversation.setTimeout(1000 * 60 * maxMinutesConversation);
			System.out.println("started new conversation with cid: " + conversation.getId());
		}
	}

	public void stop() {
		if (!conversation.isTransient()) {
			System.out.println("Conversation with ID " + conversation.getId() + " is being closed");
			conversation.end();
		}
	}

	public String backToHome() {
		stop();
		return "index?faces-redirect=true";
	}

	@Transactional
	public String deleteReservation() {
		bookingDao.delete(selectedBooking);

		updateFlightSeatAfterBookingDelete();

		if (isLoggedIn())
			bookingListComponent.updateBookingListAfterDelete(selectedBooking);

		return "delete-booking-success?faces-redirect=true";
	}

	private void updateFlightSeatAfterBookingDelete() {
		Flight flight = selectedBooking.getFlight();
		int nPassengers = selectedBooking.getPassengers().size();

		Flight f = flightDao.findById(flight.getId());
		f.setReservedSeats(f.getReservedSeats() - nPassengers);
		flightDao.save(f);
	}

	public String redirectAfterDelete() {
		if (isLoggedIn())
			return "booking-list?faces-redirect=true";
		else
			return backToHome();
	}

	public String beginBookingViewProcedure() {
		start();
		return "booking-list?faces-redirect=true";
	}

	public List<Booking> getBookingList() {
		return bookingListComponent.getBookingList();
	}

	public String viewBookingDetails(Booking booking) {
		this.selectedBooking = booking;
		return "booking-view?faces-redirect=true";
	}

	public List<Passenger> getEditPassengers() {
		editBookingComponent.setPassengers(getClonedPassengerList(selectedBooking.getPassengers()));
		return editBookingComponent.getPassengers();
	}

	private List<Passenger> getClonedPassengerList(List<Passenger> passengers) {
		List<Passenger> passengersCopy = new ArrayList<Passenger>(passengers.size());
		for (Passenger item : passengers)
			passengersCopy.add(new Passenger(item));
		return passengersCopy;
	}

	public List<Passenger> getPassengers() {
		return selectedBooking.getPassengers();
	}

	@Transactional
	public String updatePassengers() {
		selectedBooking.setPassengers(editBookingComponent.getPassengers());
		bookingDao.save(selectedBooking);
		return "update-passengers-success?faces-redirect=true";
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Booking getSelectedBooking() {
		return selectedBooking;
	}

}
