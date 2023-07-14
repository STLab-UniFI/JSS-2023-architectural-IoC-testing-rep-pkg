package it.unifi.ing.swam.model;

import java.util.UUID;

import it.unifi.ing.swam.model.temp.TemporaryReservationSeats;

public class ModelFactory {
	
	private ModelFactory() {
		
	}

	public static Country country() { return new Country(UUID.randomUUID().toString());}
	
	public static User user() {
		return new User(UUID.randomUUID().toString());
	}
	
	public static Airport airport() {
		return new Airport(UUID.randomUUID().toString());
	}
	
	public static Flight flight() {
		return new Flight(UUID.randomUUID().toString());
	}
	
	public static Booking reservation() {
		return new Booking(UUID.randomUUID().toString());
	}
	
	public static Passenger passenger() {
		return new Passenger(UUID.randomUUID().toString());
	}
	
	public static TemporaryReservationSeats temporaryReservationSeats(){
		return new TemporaryReservationSeats(UUID.randomUUID().toString());
	}
}