package it.unifi.ing.swam.controller;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import it.unifi.ing.swam.components.BookingSessionComponent;
import it.unifi.ing.swam.dao.BookingDao;
import it.unifi.ing.swam.model.ModelFactory;
import it.unifi.ing.swam.model.Booking;

@Model
public class BookingLoginController {

	@Inject
	private BookingSessionComponent reservationSession;
	
	@Inject
	private BookingDao bookingDao;

	private Booking bookingData;
	
	private String error;
	
	public BookingLoginController() {
		bookingData = ModelFactory.reservation();
	}

	/*@PostConstruct
	public void init(){
		System.out.println("[OPEN] - Componente "+ this.toString() +" avviato");
	}

	@PreDestroy
	public void shutDown(){
		System.out.println("[CLOSE] - Componente "+ this.toString() +" distrutto");
	}*/


	public String login() {
		Booking myBooking = bookingDao.searchReservation(bookingData.getReservationId(), bookingData.getEmail());
		
		if( myBooking == null ) {
			setError("Login failed! Retry");
			return "booking-login";
		}
		
		bookingData = myBooking;
		reservationSession.setId(myBooking.getId());
		return "booking-view?faces-redirect=true";
	}
	
	public String logout() {
		reservationSession.setId(null);
		return "index?faces-redirect=true";
	}
	
	public void unsetLogin(){
		reservationSession.setId(null);
	}

	public Booking getBookingData() {
		if(bookingData == null && reservationSession.isLoggedIn()){
			bookingData = bookingDao.findById(reservationSession.getId());
		}
		return bookingData;
	}
	
	public void setBookingData(Booking bookingData) {
		this.bookingData = bookingData;
	}

	public String getError() {
		if(error == null)
			error = "";
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
}
