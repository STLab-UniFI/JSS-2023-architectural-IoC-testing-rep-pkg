package it.unifi.ing.swam.controller.booking;

import it.unifi.ing.swam.components.TemporaryReservationComponent;
import it.unifi.ing.swam.components.billing.BillingComponent;
import it.unifi.ing.swam.dao.BookingDao;
import it.unifi.ing.swam.dao.FlightDao;
import it.unifi.ing.swam.model.Booking;
import it.unifi.ing.swam.model.Flight;
import it.unifi.ing.swam.model.ModelFactory;
import it.unifi.ing.swam.model.Passenger;
import it.unifi.ing.swam.util.Util;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class BookingController {

	@Inject
	protected BookingDao bookingDao;

	@Inject
	protected FlightDao flightDao;

	@Inject
	protected BillingComponent billingComponent;

	@Inject
	protected TemporaryReservationComponent temporaryReservationComponent;

	protected Long flightOut;
	protected Flight forwardFlight;
	protected List<Flight> flights;
	protected Flight flight;
	protected Integer nPassengers;
	protected List<Passenger> passengers;
	protected Booking booking;
	protected boolean justBooked;
	protected boolean discounted;
	protected float basePrice;
	protected float finalPrice;
	protected float finalPriceFull;

	public void initialize() {
		reserveTemporarySeats();
		initBooking();
		finalPrice = billingComponent.getFinalPrice(getBasePrice(), booking);
		finalPriceFull = billingComponent.getFinalPriceFull(getBasePrice(), booking);
		booking.setFinalPrice(finalPrice);
	}

	public void reserveTemporarySeats() {
		if (getFlight() != null && nPassengers != null) {
			temporaryReservationComponent.reserveTemporarySeats(getFlight(), nPassengers);
		}
	}

	public Flight getFlight() {
		if(flight == null)
			flight = flightDao.findById(flightOut);
		return flight;
	}
	
    public void initBooking() {
        booking = ModelFactory.reservation();
        booking.setDate(new Date());
        booking.setPassengers(getPassengers());
        booking.setFlight(getFlight());
        basePrice = initBasePrice();
        booking.setPrice(getBasePrice());
    }

    private float initBasePrice() {
        return getBasePriceFlightOut() * nPassengers;
    }
    
    private float getBasePriceFlightOut() {
        return booking.getFlight().getPricePerPerson();
    }
    
    public float getTotalPriceForward() {
        if (flightOut != null && flightOut != 0) {
            return Util.round(getPriceFlightOut() * getnPassengers().floatValue(), 2);
        }
        return 0;
    }
    
    public float getPriceFlightOut() {
        float basePrice = getBasePriceFlightOut();
        return basePrice + basePrice * billingComponent.getFee();
    }

    @Transactional
    public String save() {
        Long lastId = bookingDao.getIdFromLastReservation();
        booking.setReservationId("FMID-" + (lastId.intValue() + 1));
        booking.setPassengers(passengers);
        bookingDao.save(booking);
        justBooked = true;
        updateReservedSeats();
        return "confirmation";
    }

	@Transactional
	public void updateReservedSeats() {
		Flight flightOut = booking.getFlight();
		flightOut.setReservedSeats(flightOut.getReservedSeats() + booking.getPassengers().size());
		flightDao.save(flightOut);
	}

	@Transactional
	public void cleanTemporaryReservations() {
		if (temporaryReservationComponent != null) {
			temporaryReservationComponent.removeTemporarySeats();
		}
	}

	public Long getFlightOut() {
		return flightOut;
	}

	public void setFlightOut(Long flightOut) {
		this.flightOut = flightOut;
		update();
	}

	public Integer getnPassengers() {
		return nPassengers;
	}

	public void setnPassengers(Integer nPassengers) {
		this.nPassengers = nPassengers;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public List<Passenger> getPassengers() {
		passengers = new ArrayList<Passenger>();
		for (int i = 0; i < getnPassengers(); i++) {
			passengers.add(ModelFactory.passenger());
		}
		return passengers;
	}

	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}

	public Flight getForwardFlight() {
		if (forwardFlight == null) {
			if (flightOut != null && flightOut != 0)
				forwardFlight = flightDao.findById(flightOut);
		}
		return forwardFlight;
	}

	public float getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(float basePrice) {
		this.basePrice = basePrice;
	}

	public float getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(float finalPrice) {
		this.finalPrice = finalPrice;
	}

	public boolean isDiscounted() {
		return billingComponent.isDiscounted();
	}

	public void setDiscounted(boolean discounted) {
		this.discounted = discounted;
	}

	public float getFinalPriceFull() {
		return finalPriceFull;
	}

	public void setFinalPriceFull(float finalPriceFull) {
		this.finalPriceFull = finalPriceFull;
	}
	
	private void update() {
		flight = flightDao.findById(flightOut);
	}
}
