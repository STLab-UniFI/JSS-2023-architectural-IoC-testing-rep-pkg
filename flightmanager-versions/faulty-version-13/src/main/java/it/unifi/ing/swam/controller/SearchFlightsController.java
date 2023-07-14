package it.unifi.ing.swam.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import it.unifi.ing.swam.components.FlightManagerComponent;
import it.unifi.ing.swam.components.LoggedUserComponent;
import it.unifi.ing.swam.components.TemporaryReservationComponent;
import it.unifi.ing.swam.components.billing.BillingComponent;
import it.unifi.ing.swam.controller.booking.RegisteredBookingController;
import it.unifi.ing.swam.controller.booking.VisitorBookingController;
import it.unifi.ing.swam.dao.AirportDao;
import it.unifi.ing.swam.model.Country;
import it.unifi.ing.swam.model.Flight;
import it.unifi.ing.swam.util.Util;

@SessionScoped
@Named
public class SearchFlightsController implements Serializable {

	static final long serialVersionUID = 39487298L;

	private static final int maxMinutesConversation = 15;

	private final int maxPassengersPerBooking = 15;

	@Inject
	private Conversation conversation;

	@Inject
	private TemporaryReservationComponent temporaryReservationComponent;

	@Inject
	private VisitorBookingController visitorBookingController;

	@Inject
	private RegisteredBookingController registeredBookingController;

	@Inject
	private FlightManagerComponent flightManagerComponent;

	@Inject
	private BillingComponent billingComponent;

	@Inject
	private LoggedUserComponent loggedUser;

	@Inject
	private AirportDao airportDao;

	private Long flightOut;
	private Long flightBack;
	private int nPassengers;
	private Date departureDate;
	private List<String> sources;
	private List<String> destinations;
	private String source;
	private String destination;
	private String enabledDateS;
	private float totalFee;
	private float countryFee;

	private Flight detailedFlight;

	private boolean shouldSearchFlights = false;

	private String error;
	
	public void initializeSearch() {
		if (shouldSearchFlights) {
			computeSearch();
		}
	}

	private void computeSearch() {
		if (shouldSearchFlights) {
			start();
			flightManagerComponent.searchFlightsOut(departureDate, source, destination);
			shouldSearchFlights = false;
		}
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

	public String searchFlight() {
		shouldSearchFlights = true;
		return "flights-result?faces-redirect=true";
	}
	
	public List<Flight> getFlights() {
		return flightManagerComponent.getFlightsOut();
	}

	public String getFlightDetails(Flight flight) {
		flightOut = flight.getId();
		this.detailedFlight = flightManagerComponent.fetchFlight(flight.getId());
		
		setFee(flight);
		return "flight-details?faces-redirect=true";
	}

	private void setFee(Flight flight) {
		
		Country userHomeCountry = null;
		Country destinationCountry = null;

		float userHomeFee = 0;

		if (loggedUser.isLoggedIn()) {
			userHomeCountry = loggedUser.getHomeCountry(); 
			userHomeFee = loggedUser.getHomeCountryFee();
		}

		Country flightDestinationCountry = flight.getDestinationAirport().getAirportCountry();

		if (userHomeCountry == null || !flightDestinationCountry.equals(userHomeCountry))
			destinationCountry = flightDestinationCountry;

		billingComponent.setCountry(destinationCountry);
		float dynamicFee = billingComponent.getFee();

		if (userHomeCountry != null && userHomeCountry.equals(flightDestinationCountry))
			countryFee = userHomeFee;
		else
			countryFee = dynamicFee;

		totalFee = Util.round(countryFee * flight.getPricePerPerson(), 2);
		
	}
	
	public String searchAnotherFlight() {
		stop();
		shouldSearchFlights = true;
		return "flights-result?faces-redirect=true";
	}

	public String backToHome() {
		stop();
		return "index?faces-redirect=true";
	}

	public List<String> getSources() {
		if (sources == null) {
			sources = airportDao.getAllAirportsNames();
		}
		return sources;
	}

	public List<String> getDestinations() {
		if (destinations == null) {
			destinations = airportDao.getAllAirportsNames();
		}
		return destinations;
	}

	public boolean enabledSource(String airport) {
		if (destination == null)
			return true;
		return !destination.equals(airport);
	}

	public boolean enabledDestination(String airport) {
		if (source == null)
			return true;
		return !source.equals(airport);
	}


	public String confirmFlights(String userType) {
		stop();
//		if (userType.equals("visitor")) {
//			start();
//			visitorBookingController.setFlightOut(flightOut);
//			visitorBookingController.setnPassengers(nPassengers);
//		}
		if (userType.equals("registered")) {
			registeredBookingController.setFlightOut(flightOut);
			registeredBookingController.setnPassengers(nPassengers);
		}
		return "booking-details?faces-redirect=true&includeViewParams=true";
	}
	
	public int pendingSeats() {
		return temporaryReservationComponent.getTemporaryReservedSeats(detailedFlight);
	}

	public void redirectToHome(String s) throws IOException {
		error = s;
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(ec.getRequestContextPath() + "/index.xhtml");
	}

	public int getMaxPassengersPerBooking() {
		return maxPassengersPerBooking;
	}

	public Long getFlightOut() {
		if (this.flightOut == null)
			this.flightOut = 0L;

		return flightOut;
	}

	@Transactional
	public boolean isAvailable(Flight flight) {
		int realSeats = flight.getTotalSeats() - flight.getReservedSeats();
		int temp = temporaryReservationComponent.getTemporaryReservedSeats(flight);
		int availableSeats = realSeats - temp;
		return (availableSeats >= nPassengers);
	}


	public int availableSeats(Flight flight) {
		int realSeats = flight.getTotalSeats() - flight.getReservedSeats();
		int temp = temporaryReservationComponent.getTemporaryReservedSeats(flight);
		int availableSeats = realSeats - temp;

		return availableSeats;
	}

	public String isAvailableStr(Flight flight) {
		if (isAvailable(flight))
			return "";
		return "disabled";
	}

	public void setFlightOut(Long flightOut) {
		this.flightOut = flightOut;
	}

	public Long getFlightBack() {
		if (this.flightBack == null)
			this.flightBack = 0L;

		return flightBack;
	}

	public int getnPassengers() {
		if (nPassengers == 0)
			nPassengers = 1;

		return nPassengers;
	}

	public void setnPassengers(int nPassengers) {
		this.nPassengers = nPassengers;
	}

	public Date getDepartureDate() {
		if (departureDate == null)
			departureDate = new Date();

		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public void setError(String err) {
		error = err;
	}

	public String getError() {
		return error;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getEnabledDateS() {
		return enabledDateS;
	}

	public void setEnabledDateS(String enabledDateS) {
		this.enabledDateS = enabledDateS;
	}

	public Flight getDetailedFlight() {
		return detailedFlight;
	}

	public void setDetailedFlight(Flight detailedFlight) {
		this.detailedFlight = detailedFlight;
	}

	public float getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(float totalFee) {
		this.totalFee = totalFee;
	}

}
