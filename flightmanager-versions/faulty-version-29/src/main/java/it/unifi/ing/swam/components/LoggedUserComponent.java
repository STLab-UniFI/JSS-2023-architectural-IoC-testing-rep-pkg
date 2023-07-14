package it.unifi.ing.swam.components;

import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import it.unifi.ing.swam.components.billing.BillingComponent;
import it.unifi.ing.swam.dao.UserDao;
import it.unifi.ing.swam.model.Country;
import it.unifi.ing.swam.model.User;
import it.unifi.ing.swam.model.UserRole;

@SessionScoped
@Named
public class LoggedUserComponent implements Serializable {

	private static final long serialVersionUID = 387217630271L;

	private User user;
	private int history;
	private Country homeCountry;

	@Inject
	private UserDao userDao;

	@Inject
	private BillingComponent billingComponent;

	public void initUser(User loggingUser) {
		this.user = loggingUser;
		history = userDao.getHistory(user.getId());
		billingComponent.setCountry(user.getHomeCountry());
	}

	public Long getUserID() {
		return user.getId();
	}

	public boolean isLoggedIn() {
		return user != null;
	}

	// FIXME: one of them is useless
	public void checkNotLoggedin() throws IOException {
		if (!isLoggedIn()) {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath() + "/login.xhtml");
		}
	}

	public void checkLoggedin() throws IOException {
		if (isLoggedIn()) {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath() + "/index.xhtml");
		}
	}

	@Transactional
	public void shutDownUser() {
		if (user != null) { 
			userDao.save(user);
			this.user = null;
		}
	}
	
	public void addBookingToHistory() {
		history++;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getHistory() {
		return history;
	}

	public void setHistory(int history) {
		this.history = history;
	}

	public boolean isAdmin() {
		if (isLoggedIn())
			return user.hasRole(UserRole.Admin);
		return false;
	}

	public boolean isRegisteredUser() {
		if (isLoggedIn())
			return user.hasRole(UserRole.Registered);
		return false;
	}

	@PostConstruct
	public void init() {
		System.out.println("[OPEN] - Componente " + this.toString() + " avviato");
	}

	@PreDestroy
	public void shutDown() {
		System.out.println("[CLOSE] - Componente " + this.toString() + " distrutto");
	}

	public Country getHomeCountry() {
		homeCountry = this.user.getHomeCountry();
		return homeCountry;
	}

	public void setHomeCountry(Country homeCountry) {
		this.homeCountry = homeCountry;
	}

	public float getHomeCountryFee() {
		billingComponent.setCountry(user.getHomeCountry());
		float fee = this.billingComponent.getFee();
		int affiliationYears = getDiffYears(user.getRegistrationDate(), new Date());
		float affiliationFeeDiscount = 0;

		if (affiliationYears > 15)
			affiliationFeeDiscount = 0.02f;
		else if (affiliationYears > 10)
			affiliationFeeDiscount = 0.015f;
		else if (affiliationYears > 5)
			affiliationFeeDiscount = 0.01f;
		else if (affiliationYears > 1)
			affiliationFeeDiscount = 0.005f;

		float newFee = (fee - affiliationFeeDiscount);

		return (newFee > 0) ? newFee : 0;
	}

	private Calendar getCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal;
	}

	private int getDiffYears(Date from, Date to) {
		Calendar a = getCalendar(from);
		Calendar b = getCalendar(to);
		int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
		if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH)
				|| (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
			diff--;
		}
		return diff;
	}
}
