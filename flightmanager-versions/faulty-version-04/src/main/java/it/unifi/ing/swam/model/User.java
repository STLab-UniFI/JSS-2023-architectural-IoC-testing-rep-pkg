package it.unifi.ing.swam.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="user")
public class User extends BaseEntity {
	
	private String username;

	private UserRole userRole;

	@Column(nullable = false)
	private String password;

	@OneToOne
	private Country homeCountry;

	private Date registrationDate;

	public Country getHomeCountry() {
		return homeCountry;
	}

	public void setHomeCountry(Country homeCountry) {
		this.homeCountry = homeCountry;
	}

	public User() {
		super();
	}
	
	public User(String uuid) {
		super(uuid);
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

	public UserRole getUserRole() {
		return userRole;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
	public boolean hasRole(UserRole userRole) {
		return this.userRole == userRole;
	}
}
