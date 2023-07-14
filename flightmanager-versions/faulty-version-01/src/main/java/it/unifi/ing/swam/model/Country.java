package it.unifi.ing.swam.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "country")
public class Country extends BaseEntity {

	private String name;

	private Long countryCode;

	private float countryFee;

	private boolean UE;

	public Country(String UUID) {
		super(UUID);
	}

	public Country() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(Long countryCode) {
		this.countryCode = countryCode;
	}

	public float getCountryFee() {
		return countryFee;
	}

	public void setCountryFee(float countryFee) {
		this.countryFee = countryFee;
	}

	public String toString() {
		return this.name;
	}

	public boolean isUE() {
		return UE;
	}

	public void setUE(boolean uE) {
		this.UE = uE;
	}

}
