package it.unifi.ing.swam.controller.booking;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import it.unifi.ing.swam.components.TemporaryReservationComponent;

@ConversationScoped
@Named
public class VisitorBookingController extends BookingController implements Serializable {

	static final long serialVersionUID = 329857101L;
	private static final int maxMinutesConversation = 15;

	@Inject
	private Conversation conversation;

//	@PostConstruct
//	private void init() {
//		start();
//	}

//	@Override
//	public void initialize() {
//		start();
//		billingComponent.setCountry(getFlight().getSourceAirport().getAirportCountry());
//		super.initialize();
//	}
	
	
	public void initialize(Long flightOut, int passengers) {
		start();
		this.setFlightOut(flightOut);
		this.setnPassengers(passengers);
		billingComponent.setCountry(getFlight().getSourceAirport().getAirportCountry());
		super.initialize();
	}

	public void start() {
		if (conversation.isTransient()) {
			conversation.begin();
			conversation.setTimeout(1000 * 60 * maxMinutesConversation);
			System.out.println(
					"[CREATED] - " + this.getClass() + " has just been constructed with cid: " + conversation.getId());
		}
	}

	public void stop() {
		if (!conversation.isTransient())
			conversation.end();
	}

	@Transactional
	public String save() {
		return super.save();
	}

	public String cancel() {
		stop();
		return "index?faces-redirect=true";
	}

}
