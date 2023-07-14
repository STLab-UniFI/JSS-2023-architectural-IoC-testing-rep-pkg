package it.unifi.ing.swam.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import it.unifi.ing.swam.dao.AirportDao;
import it.unifi.ing.swam.dao.CountryDao;
import it.unifi.ing.swam.model.Airport;
import it.unifi.ing.swam.model.Country;
import it.unifi.ing.swam.model.ModelFactory;

@ConversationScoped
@Named
public class AirportController implements Serializable {

	static final long serialVersionUID = 1L;

	private static final String firstFormPageName = "add-airports-main";

	@Inject
	private AirportDao airportDao;

	@Inject
	private CountryDao countryDao;

	@Inject
	private Conversation conversation;

	private Airport airport;

	private List<Country> countryList;
	
	private List<Airport> airports;

	private Airport focusAirport;

	@PostConstruct
	private void init() {
		airport = ModelFactory.airport();
		if (shouldConversationBegin())
			start();
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
	
	public String goToAirportListPage() {
		start();
		return "airport-list?faces-redirect=true";
	}

	public String startAndOpen() {
		start();
		return "add-airports-main?faces-redirect=true";
	}

	private void start() {
		if (conversation.isTransient()) {
			conversation.begin();
			conversation.setTimeout(1000 * 60 * 20);
			System.out.println("New Conv id: " + conversation.getId());
		}
	}

	public int stop() {
		String id = conversation.getId();
		if (!conversation.isTransient()) {
			conversation.end();
			System.out.println("Stopped Conv id: " + id);
			
		}
		return Integer.valueOf(id);
	}

	public String stopConversationAndReturnHome() {
		stop();
		return "administrator-page?faces-redirect=true";
	}

	//TODO get rid of duplication
	public String redirectToHome() throws IOException {
		stop();
		return "administrator-page?faces-redirect=true";
	}

	public String addAirportDetails() {
		return "add-airports-details?faces-redirect=true";
	}

	public String backToMainForm() {
		return "add-airports-main?faces-redirect=true";
	}

	@Transactional
	public String saveAndAddNewAirport() {
		airportDao.save(airport);
		int oldcid = stop();
		System.out.println("OLDCID: " + oldcid);
		return "add-airports-main?faces-redirect=true";

	}

	@Transactional
	public String saveAndGoHome() {
		airportDao.save(airport);
		return "administrator-page?faces-redirect=true";
	}
	

	public Airport getAirport() {
		return airport;
	}

	public void setAirport(Airport airport) {
		this.airport = airport;
	}

	public List<Country> getCountryList() {
		if (countryList == null)
			countryList = countryDao.getAllCountries();
		return countryList;
	}
	
	public List<Airport> getAirports() {
		if (airports == null) {
			airports = airportDao.getAllAirports();
		}
		return airports;
	}
	
	public String viewAirport(Airport airport) {
		this.focusAirport = airport;
		return "airport-view?faces-redirect=true";
	}

	public Airport getFocusAirport() {
		return focusAirport;
	}

	public void setFocusAirport(Airport focusAiport) {
		this.focusAirport = focusAiport;
	}

}
