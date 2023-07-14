package it.unifi.ing.swam.components;

import it.unifi.ing.swam.model.Flight;
import it.unifi.ing.swam.model.ModelFactory;
import it.unifi.ing.swam.model.temp.TemporaryReservationSeats;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequestScoped
public class TemporaryReservationComponent implements Serializable {

    private static final long serialVersionUID = 27620988221L;

    @Inject
    private TemporaryReservationRepository temporaryReservations;

    private ArrayList<Long> temporaryReservationSeatsList;

    @PostConstruct
    public void init(){
        System.out.println("[OPEN] - Componente* "+ this.toString() +" avviato");
    }

    @PreDestroy
    public void cleanTemporarySeats(){
            System.out.println("Context of user is closing, cleaning temporary seats");
            removeTemporarySeats();
            System.out.println("[CLOSE] - Componente "+ this.toString() +" distrutto");
    }
    
	public void reserveTemporarySeats(Flight flight, int nPassengers) {
		if (temporaryReservationSeatsList == null || temporaryReservationSeatsList.isEmpty()) {
			temporaryReservationSeatsList = new ArrayList<Long>();
		}
		TemporaryReservationSeats temporaryReservationSeats;
		System.out.println("Temporary booking for " + nPassengers + " passengers");
		temporaryReservationSeats = ModelFactory.temporaryReservationSeats();
		temporaryReservationSeats.setDate(new Date());
		temporaryReservationSeats.setFlight(flight);
		temporaryReservationSeats.setnPassengers(nPassengers);
		temporaryReservationSeats = temporaryReservations.addTemporarySeats(temporaryReservationSeats);
		if (temporaryReservationSeats != null) {
			temporaryReservationSeatsList.add(temporaryReservationSeats.getId());
		}

	}
	
	// XXX could be used but should not
    public void reserveTemporarySeats(List<Flight> flightsList, int nPassengers) {
        if(temporaryReservationSeatsList == null || temporaryReservationSeatsList.isEmpty()) {
            temporaryReservationSeatsList = new ArrayList<Long>();
        }
        TemporaryReservationSeats temporaryReservationSeats;
        System.out.println("Temporary booking for " + nPassengers + " passengers");
        for(int i = 0; i < flightsList.size(); i++) {
            temporaryReservationSeats = ModelFactory.temporaryReservationSeats();
            temporaryReservationSeats.setDate( new Date() );
            temporaryReservationSeats.setFlight(flightsList.get(i));
            temporaryReservationSeats.setnPassengers(nPassengers);
            temporaryReservationSeats = temporaryReservations.addTemporarySeats(temporaryReservationSeats);
            if(temporaryReservationSeats != null ){
                temporaryReservationSeatsList.add(temporaryReservationSeats.getId());
            }
        }
    }

    public int getTemporaryReservedSeats(Flight flight){
    	System.out.println("I trs sono: "+ temporaryReservations.getTemporaryReservedSeatsOfFlight(flight));
        return temporaryReservations.getTemporaryReservedSeatsOfFlight(flight);
    }

    public void removeTemporarySeats(){
    	if(temporaryReservationSeatsList == null || temporaryReservationSeatsList.isEmpty())
    		return; 
    	
        System.out.println("Cleaning temporary seats .. ");
        for(int i = 0; i < temporaryReservationSeatsList.size(); i++){
            System.out.println("Removing booking " + (i+1) + " of " + temporaryReservationSeatsList.size() );
            temporaryReservations.removeTemporaryReservation(temporaryReservationSeatsList.get(i));
        }
        temporaryReservationSeatsList.clear();
        System.out.println("Temporary seats list cleaned");
    }

}
