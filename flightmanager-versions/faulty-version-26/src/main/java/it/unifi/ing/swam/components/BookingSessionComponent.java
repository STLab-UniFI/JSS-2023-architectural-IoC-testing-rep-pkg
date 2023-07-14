package it.unifi.ing.swam.components;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import it.unifi.ing.swam.dao.BookingDao;
import it.unifi.ing.swam.model.Booking;

@SessionScoped
@Named
public class BookingSessionComponent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Booking booking;

	@PostConstruct
	public void init(){
		System.out.println("[OPEN] - Componente "+ this.toString() +" avviato");
	}

	@PreDestroy
	public void shutDown(){
		System.out.println("[CLOSE] - Componente "+ this.toString() +" distrutto");
	}
	
	@Inject
	private BookingDao bookingDao;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
		booking = (id == null) ? null : bookingDao.findById(id);
	}
	
	public boolean isLoggedIn() {
		return id != null;
	}
	
	public void checkNotLoggedin() throws IOException {
	    if (!isLoggedIn()) {
	        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	        ec.redirect(ec.getRequestContextPath() + "/booking-login.xhtml");
	    }
	}
	
	public void checkLoggedin() throws IOException {
	    if (isLoggedIn()) {
	        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	        ec.redirect(ec.getRequestContextPath() + "/booking-view.xhtml");
	    }
	}
	
	public Booking getBooking() {
		return booking;
	}
	public void setBooking(Booking booking) {
		this.booking = booking;
	}

}
