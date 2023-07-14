package it.unifi.ing.swam.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="booking")
public class Booking extends BaseEntity {
	
	private String reservationId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	private float price;
	
	private float finalPrice;
	
	private String email;
	
	@OneToMany(fetch = FetchType.EAGER,
			cascade = CascadeType.ALL)
	private List<Passenger> passengers;
	
	@ManyToOne
	private User registeredUserOwner;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Flight flight;
	
	public Booking() {
		super();
	}
	
	public Booking(String uuid) {
		super(uuid);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Passenger> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<Passenger> passengers) {
		this.passengers = passengers;
	}
	
	public void setFlight(Flight flight) {
		this.flight = flight;
	}
	
	public Flight getFlight() {
		return flight;
	}
	
	public String getReservationId() {
		return reservationId;
	}

	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}

	public float getFinalPrice() {
		return finalPrice;
	}
	
	public void setFinalPrice(float finalPrice) {
		this.finalPrice = finalPrice;
	}

	public User getRegisteredUserOwner() {
		return registeredUserOwner;
	}

	public void setRegisteredUserOwner(User registeredUserOwner) {
		this.registeredUserOwner = registeredUserOwner;
	}

	/*
	public Strategy getPriceCalculation() {
		return priceCalculation;
	}

	public void setPriceCalculation(Strategy priceCalculation) {
		this.priceCalculation = priceCalculation;
	}*/
	
}
