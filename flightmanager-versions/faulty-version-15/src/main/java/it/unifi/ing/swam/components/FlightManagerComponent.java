package it.unifi.ing.swam.components;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import it.unifi.ing.swam.dao.FlightDao;
import it.unifi.ing.swam.model.Flight;

@Named
@ConversationScoped
public class FlightManagerComponent implements Serializable {

	static final long serialVersionUID = 120948301L;
	private static final int maxMinutesConversation = 15;

	@Inject
	private FlightDao flightDao;


	@Inject
	private Conversation conversation;
	private List<Flight> flightsOut;

	@PostConstruct
	private void init() {
		System.out.println("[CREATED] - " + this.getClass() + " has just been constructed");
	}
	
	public boolean existsResult() {
		if (flightsOut == null)
			return false;
		return !flightsOut.isEmpty();
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

	// si suppone sia un fetch di tutti i dati del volo
	public Flight fetchFlight(Long flightID) {
		return flightDao.getFlightData(flightID);
	}


	// Only basic information are retrieved for each Flight
	public void searchFlightsOut(Date departureDate, String source, String destination) {
		if (flightsOut == null || flightsOut.isEmpty()) { // XXX non so se questa guarda crei danni
			flightsOut = flightDao.getFlights(source, destination, departureDate);
		}
	}

	public List<Flight> getFlightsOut() {
		return flightsOut;
	}

	// XXX questi getter/setter conversation dubito servano
	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

}
