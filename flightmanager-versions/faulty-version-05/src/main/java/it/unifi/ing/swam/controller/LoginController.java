package it.unifi.ing.swam.controller;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.inject.Named;

import it.unifi.ing.swam.components.BookingListComponent;
import it.unifi.ing.swam.components.BookingSessionComponent;
import it.unifi.ing.swam.components.LoggedUserComponent;
import it.unifi.ing.swam.components.PasswordManagerComponent;
import it.unifi.ing.swam.dao.UserDao;
import it.unifi.ing.swam.model.User;
import it.unifi.ing.swam.model.ModelFactory;
import it.unifi.ing.swam.model.UserRole;

@Named
@RequestScoped
public class LoginController {
	
	@Inject
	private LoggedUserComponent loggedUserComponent;
	
	@Inject
	private UserDao userDao;

	@Inject
	private PasswordManagerComponent pwdManager;

	@Inject
	private BookingSessionComponent reservationSession;
	
	@Inject
	private BookingListComponent bookingListComponent;
	
	private User user;
	private String username;
	private String password;
	
	private String error;
	private String toHome = "index?faces-redirect=true";
	private String toAdminPage = "administrator-page?faces-redirect=true";
	
	public LoginController() {
		user = ModelFactory.user();
	}

	/*public String login(){
		if(!loginAsCustomer()){
			setError("Login failed! Retry");
			return "";
		}
		else{ // utente loggato
			if(loggedUserComponent.isAdmin()) return toAdminPage;
			return toHome;
		}
	}*/

	public String loginAsCustomer(){
		
		User user = userDao.login(username, pwdManager.encode(password));

		if (user != null)
			this.loggedUserComponent.initUser(user);

		return ( user == null ) ? "" : toHome;
	}

	public String loginAsAdmin(){
		User user = userDao.login(username, pwdManager.encode(password));
		if(!user.hasRole(UserRole.Admin) ) return null;
		if (user != null)
			this.loggedUserComponent.initUser(user);

		return ( user == null ) ? "" : toAdminPage;
	}

	public String logout() {
		loggedUserComponent.shutDownUser();
		reservationSession.setId(null);
		bookingListComponent.clearList();
		return "index?faces-redirect=true";
	}
	
	public void unsetLogin(){
		loggedUserComponent.setUser(null);
	}
	
	public User getUser() {
		if(user == null && loggedUserComponent.isLoggedIn()){
			user = userDao.findById(loggedUserComponent.getUser().getId());
		}
		return user;
	}
	
	public String getError() {
		if(error == null)
			error = "";
		return error;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setError(String error) {
		this.error = error;
	}

}
