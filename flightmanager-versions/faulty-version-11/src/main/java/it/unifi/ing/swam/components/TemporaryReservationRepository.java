package it.unifi.ing.swam.components;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;

import it.unifi.ing.swam.model.Flight;
import it.unifi.ing.swam.model.temp.TemporaryReservationSeats;

@RequestScoped
public class TemporaryReservationRepository {

    private Long tosID;
    private Map<Long, TemporaryReservationSeats> temporaryReservations;


    @PostConstruct
    private void init(){
        tosID = 0L;
        temporaryReservations = new HashMap<>();

    }
    
    public TemporaryReservationSeats addTemporarySeats(TemporaryReservationSeats temporaryReservationSeats){
        Long id = temporaryReservationSeats.getId();
        if(id != null){ // booking update
            temporaryReservations.replace(id, temporaryReservationSeats);
            return temporaryReservations.get(id);
        } else {
            temporaryReservationSeats.setId(tosID);
            temporaryReservations.put(tosID, temporaryReservationSeats);
            tosID ++;
            return temporaryReservationSeats;
        }

    }

    public int getTemporaryReservedSeatsOfFlight(Flight flight){
        int counter=0;
        for (TemporaryReservationSeats tempSeat: temporaryReservations.values()) {
        	if(tempSeat.getFlight().getId().longValue() == flight.getId().longValue()){	
                counter += tempSeat.getnPassengers();
            }
        }
        return counter;
    }

    public void removeTemporaryReservation(Long reservationID){
        temporaryReservations.remove(reservationID);
    }

    // XXX questo non credo sia necessario
    @PreDestroy
    private void free(){
        tosID = null;
        temporaryReservations = null;
    }

}
