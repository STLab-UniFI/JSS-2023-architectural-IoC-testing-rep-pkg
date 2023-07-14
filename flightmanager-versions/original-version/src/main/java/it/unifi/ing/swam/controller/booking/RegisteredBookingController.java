package it.unifi.ing.swam.controller.booking;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import it.unifi.ing.swam.components.LoggedUserComponent;
import it.unifi.ing.swam.model.Country;

@SessionScoped
@Named
public class RegisteredBookingController extends BookingController implements Serializable {

	static final long serialVersionUID = 4891678203L;

	@Inject
	protected LoggedUserComponent loggedUserComponent;

	@PostConstruct
	private void init() {
		System.out.println("[CREATED] - this component: " + this.getClass() + " has just been constructed");
	}

	@Override
	public void initialize() {
		billingComponent.setCountry(getFlight().getDestinationAirport().getAirportCountry());
		System.out.println("inserita country: " + getFlight().getDestinationAirport().getAirportCountry());
		super.initialize();
	}


	@Transactional
	public String save() {
		loggedUserComponent.addBookingToHistory();
		booking.setRegisteredUserOwner(loggedUserComponent.getUser());
		temporaryReservationComponent.removeTemporarySeats();
		return super.save();
	}

	public String cancel() {
		temporaryReservationComponent.removeTemporarySeats();
		return "index?faces-redirect=true";
	}
	
	
	public String backToHome() {
		return "index?faces-redirect=true";
	}

}
