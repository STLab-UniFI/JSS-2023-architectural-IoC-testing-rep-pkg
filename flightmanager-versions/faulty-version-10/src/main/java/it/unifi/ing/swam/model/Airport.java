package it.unifi.ing.swam.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="airport")
public class Airport extends BaseEntity {

	private String cityName; 

	private int ZIP; 

	private int GMT; 

	private String airportFullName; 

	private String address; 

	private boolean international;

	private String publicMailAddress;

	private String publicPhoneNumber;

	@OneToOne
	private Country airportCountry; 

	public Airport() {
		super();
	}

	public Airport(String uuid) {
		super(uuid);
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String name) {
		this.cityName = name;
	}

	public int getZIP() {
		return ZIP;
	}

	public void setZIP(int ZIP) {
		this.ZIP = ZIP;
	}

	public int getGMT() {
		return GMT;
	}

	public void setGMT(int GMT) {
		this.GMT = GMT;
	}

	public String getAirportFullName() {
		return airportFullName;
	}

	public void setAirportFullName(String city) {
		this.airportFullName = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isInternational() {
		return international;
	}

	public void setInternational(boolean international) {
		this.international = international;
	}

	public String getPublicMailAddress() {
		return publicMailAddress;
	}

	public void setPublicMailAddress(String publicMailAddress) {
		this.publicMailAddress = publicMailAddress;
	}

	public String getPublicPhoneNumber() {
		return publicPhoneNumber;
	}

	public void setPublicPhoneNumber(String publicPhoneNumber) {
		this.publicPhoneNumber = publicPhoneNumber;
	}

	public Country getAirportCountry() {
		return airportCountry;
	}

	public void setAirportCountry(Country airportCountry) {
		this.airportCountry = airportCountry;
	}
}


