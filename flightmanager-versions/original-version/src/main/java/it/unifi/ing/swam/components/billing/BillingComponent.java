package it.unifi.ing.swam.components.billing;


import it.unifi.ing.swam.dao.CountryDao;
import it.unifi.ing.swam.model.Booking;
import it.unifi.ing.swam.model.Country;
import it.unifi.ing.swam.util.Util;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;

@SessionScoped
public class BillingComponent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	DiscounterComponent discounterComponent;

	@Inject
	CountryDao countryDao;

	private boolean discounted;
	private Country country;
	private float homeFee;

	public float getFinalPriceFull(float basePrice, Booking booking) {
		return Util.round(basePrice + (basePrice * getFee()), 2);
	}

	public float getFinalPrice(float basePrice, Booking booking) {
		float discount = getFinalDiscount(booking);
		float noFeePrice = Util.round(basePrice - discount, 2);
		return Util.round(noFeePrice + (noFeePrice * getFee()), 2);
	}

	public float getFinalDiscount(Booking booking) {
		float totalDiscount = discounterComponent.apply(booking);
		if (totalDiscount < 0.005)
			discounted = false;
		else
			discounted = true;
		return totalDiscount;
	}

	public Country getCountry() {
		return country;
	}

    public float getHomeFee() {
        return homeFee;
    }

    public float getFee() {
        homeFee = countryDao.findById(country.getId()).getCountryFee();
        System.out.println("La fee di default è: "+ homeFee);
        return getHomeFee();
    }

	public void setHomeFee(float fee) {
		this.homeFee = fee;
	}

	public void setCountry(Country arrivalCountry) {
		if (arrivalCountry != null) {
			this.country = arrivalCountry;
		}
	}

	public boolean isDiscounted() {
		return discounted;
	}

	public void setDiscounted(boolean discounted) {
		this.discounted = discounted;
	}
}
