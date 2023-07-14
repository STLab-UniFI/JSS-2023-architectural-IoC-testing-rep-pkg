package it.unifi.ing.swam.components;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import it.unifi.ing.swam.model.Passenger;

@Named
@RequestScoped
public class editBookingComponent {
	
	private List<Passenger> passengers;

	public List<Passenger> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<Passenger> passengerList) {
		this.passengers = passengerList;
	}
	
	

}
