package it.unifi.ing.swam.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import it.unifi.ing.swam.dao.AirportDao;
import it.unifi.ing.swam.dao.FlightDao;
import it.unifi.ing.swam.model.Airport;
import it.unifi.ing.swam.model.Flight;
import it.unifi.ing.swam.model.ModelFactory;
import it.unifi.ing.swam.util.Util;

@ConversationScoped
@Named
public class FlightController implements Serializable {

	static final long serialVersionUID = 14198273610L;

	private static final String firstFormPageName = "add-flights";

	@Inject
	private FlightDao flightDao;

	@Inject
	private AirportDao airportDao;

	@Inject
	private Conversation conversation;

	private Flight flight;

	private List<Airport> airports;
	private List<Flight> flights;

	private Airport sourceAirportNew;
	private Airport destinationAirportNew;

	private List<Integer> listRepeats;
	private Integer repeats;

	private Long sourceAirportId;
	private Long destinationAirportId;

	private String error;
	private Date fromDate;

	private boolean justInsertFlight;
	private boolean justDeleteFlight;

	private boolean insertFlightBackToo;

	private Flight focusFlight;

	@PostConstruct
	private void autoInit() {
		if (shouldConversationBegin()) {
			start();
			init();
		}
	}

	private boolean shouldConversationBegin() {
		if (isTheMainFormPage())
			return true;
		else
			return false;
	}

	private boolean isTheMainFormPage() {
		String uri = getNameCurrentPage();
		return uri.contains(firstFormPageName);
	}

	private String getNameCurrentPage() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		return request.getRequestURI();
	}

	private void start() {
		if (conversation.isTransient()) {
			conversation.begin();
			conversation.setTimeout(1000 * 60 * 20);
		}
	}

	public String goToFlightListPage() {
		start();
		searchFlightsFromToday();
		return "flights-list?faces-redirect=true";
	}

	private void searchFlightsFromToday() {
		fromDate = new Date();
		sourceAirportId = 0L;
		destinationAirportId = 0L;
		searchFlights();
	}

	public String searchFlights() {
		error = "";
		if (sourceAirportId == 0 && destinationAirportId == 0) {
			flights = flightDao.getAllFlights(fromDate);
			if (flights == null || flights.isEmpty())
				error = "No available flights since " + Util.getOnlyDate(fromDate);
		} else if (sourceAirportId == 0) {
			Airport d = airportDao.findById(destinationAirportId);
			flights = flightDao.getFlightsFromDestination(d, fromDate);
			if (flights == null || flights.isEmpty())
				error = "No available flights to " + d.getCityName() + " since " + Util.getOnlyDate(fromDate);
		} else if (destinationAirportId == 0) {
			Airport s = airportDao.findById(sourceAirportId);
			flights = flightDao.getFlightsFromSource(s, fromDate);
			if (flights == null || flights.isEmpty())
				error = "No available flights from " + s.getCityName() + " since " + Util.getOnlyDate(fromDate);
		} else {
			Airport s = airportDao.findById(sourceAirportId);
			Airport d = airportDao.findById(destinationAirportId);
			flights = flightDao.getFlightsFromDate(s, d, fromDate);
			if (flights == null || flights.isEmpty())
				error = "No available flights from " + s.getCityName() + " to " + d.getCityName() + " since "
						+ Util.getOnlyDate(fromDate);
		}
		return "flights-list?faces-redirect=true";
	}
	
	public String returnToFlightListPage() {
		return "flights-list?faces-redirect=true";
	}

	public String stopConversationAndReturnHome() {
		stop();
		return "administrator-page?faces-redirect=true";
	}

	public int stop() {
		String id = conversation.getId();
		if (!conversation.isTransient())
			conversation.end();
		return Integer.valueOf(id);
	}

	public String startAndOpen() {
		start();
		init();
		return returnInsertFlightPage();
	}

	public void init() {
		repeats = 1;
		if (flight == null) {
			flight = ModelFactory.flight();
			flight.setReservedSeats(0);

			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.HOUR_OF_DAY, -1);

			flight.setDepartureDate(cal.getTime());
			insertFlightBackToo = false;
			justInsertFlight = true;
		}
	}

	public String returnInsertFlightPage() {
		return "add-flights?faces-redirect=true";
	}

	public String saveInfoAndNext() {
		flight.setSourceAirport(airportDao.findById(sourceAirportId));
		flight.setDestinationAirport(airportDao.findById(destinationAirportId));
		flight.setDepartureTime(Util.subOneHour(flight.getDepartureTime()));
		return "insert-flights-details?faces-redirect=true";
	}

	@Transactional
	public String saveFlightAndGoHome() {
		error = "";
		saveFlights();
		return stopConversationAndReturnHome();
	}

	@Transactional
	public String saveFlightAndAddNew() {
		error = "";
		saveFlights();
		int oldcid = stop();
		// --- soluzione con errore
		// return startAndOpen();

		// --- soluzione corretta
		return returnInsertFlightPage();
	}

	@Transactional
	private void saveFlights() {
		setJustInsertFlight(true);
		saveForwardAndReturnFlight(flight);
		Date date = flight.getDepartureDate();
		for (int i = 0; i < repeats.intValue() - 1; i++) {
			date = Util.incrementDay(date);
			saveForwardAndReturnFlight(flight, date);
		}
	}

	private void saveForwardAndReturnFlight(Flight flightToInsert, Date date) {
		Flight fl = ModelFactory.flight();
		fl.copyFlight(flightToInsert);
		fl.setDepartureDate(date);
		saveForwardAndReturnFlight(fl);
	}

	private void saveForwardAndReturnFlight(Flight flightToInsert) {
		flightDao.save(flightToInsert);
		if (insertFlightBackToo) {
			saveReturnFlight(flightToInsert);
		}
	}

	private void saveReturnFlight(Flight flightToInsert) {
		Flight backFlight = ModelFactory.flight();
		backFlight.copyFlight(flightToInsert);
		backFlight.setSourceAirport(flightToInsert.getDestinationAirport());
		backFlight.setDestinationAirport(flightToInsert.getSourceAirport());
		flightDao.save(backFlight);
	}

	@Transactional
	public String deleteFlight(String idStr) {
		Long id = Long.valueOf(idStr);
		flightDao.delete(flightDao.findById(id));
		flights = null;
		setJustDeleteFlight(true);
		return "delete-flight-success?faces-redirect=true";
	}

	public void reset() {
		flight = null;
		repeats = new Integer(1);
		sourceAirportId = airports == null ? null : airports.get(0).getId(); // O_o'
		destinationAirportId = sourceAirportId;
	}
	
	
	public String viewFlight(Flight flight) {
		this.setFocusFlight(flight);
		return "flight-view?faces-redirect=true";
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public List<Airport> getAirports() {
		if (airports == null) {
			airports = airportDao.getAllAirports();
		}
		return airports;
	}

	public List<Flight> getFlights() {
		if (flights == null) {
			flights = new ArrayList<Flight>();
		}
		return flights;
	}

	public Integer getRepeats() {
		return repeats;
	}

	public void setRepeats(Integer repeats) {
		this.repeats = repeats;
	}

	public List<Integer> getListRepeats() {
		if (listRepeats == null) {
			listRepeats = new ArrayList<Integer>();
			for (int i = 0; i < 31; i++) {
				listRepeats.add(i + 1);
			}
		}
		return listRepeats;
	}

	public void setListRepeats(List<Integer> listRepeats) {
		this.listRepeats = listRepeats;
	}

	public Airport getSourceAirportNew() {
		if (sourceAirportNew == null) {
			sourceAirportNew = ModelFactory.airport();
		}
		return sourceAirportNew;
	}

	public void setSourceAirportNew(Airport sourceAirportNew) {
		this.sourceAirportNew = sourceAirportNew;
	}

	public Airport getDestinationAirportNew() {
		if (destinationAirportNew == null) {
			destinationAirportNew = ModelFactory.airport();
		}
		return destinationAirportNew;
	}

	public void setDestinationAirportNew(Airport destinationAirportNew) {
		this.destinationAirportNew = destinationAirportNew;
	}

	public Long getSourceAirportId() {
		return sourceAirportId;
	}

	public void setSourceAirportId(Long sourceAirportId) {
		this.sourceAirportId = sourceAirportId;
	}

	public Long getDestinationAirportId() {
		return destinationAirportId;
	}

	public void setDestinationAirportId(Long destinationAirportId) {
		this.destinationAirportId = destinationAirportId;
	}

	public String getError() {
		if (error == null)
			error = "";
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Date getFromDate() {
		if (fromDate == null)
			fromDate = new Date();
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public boolean isJustInsertFlight() {
		return justInsertFlight;
	}

	public void setJustInsertFlight(boolean justInsertFlight) {
		this.justInsertFlight = justInsertFlight;
	}

	public String backToInsertFlight() {
		error = "";
		return "add-flights?faces-redirect=true";
	}

	public boolean isJustDeleteFlight() {
		return justDeleteFlight;
	}

	public void setJustDeleteFlight(boolean justDeleteFlight) {
		this.justDeleteFlight = justDeleteFlight;
	}

	public boolean isInsertFlightBackToo() {
		return insertFlightBackToo;
	}

	public void setInsertFlightBackToo(boolean insertFlightBackToo) {
		this.insertFlightBackToo = insertFlightBackToo;
	}

	public Flight getFocusFlight() {
		return focusFlight;
	}

	public void setFocusFlight(Flight focusFlight) {
		this.focusFlight = focusFlight;
	}

}
