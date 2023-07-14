package it.unifi.ing.swam.dao;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.unifi.ing.swam.model.Booking;

@RequestScoped
public class BookingDao implements Serializable {
	
	static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public void save(Booking booking) {
		if(booking.getId() != null)
			entityManager.merge(booking);
		else
			entityManager.persist(booking);
	}
	
	public void delete(Booking booking) {
		this.entityManager.remove(
				this.entityManager.contains(booking) ? booking : this.entityManager.merge(booking));
	}
	
	public Booking findById(Long id) {
		return entityManager.find(Booking.class, id);
	}
	
	public Booking searchReservation(String reservationId, String email) {
		List<Booking> result = this.entityManager
				.createQuery("SELECT r "
							+ "FROM Booking r "
							+ "WHERE r.reservationId = :reservationId AND r.email = :email ",
							Booking.class)
				.setParameter("reservationId", reservationId)
				.setParameter("email", email)
				.setMaxResults(1)
				.getResultList();
		
		if(result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}

	public int getBookingNumberPerUser(Long userID){
		int result = this.entityManager
				.createQuery("SELECT count (distinct r)"
								+ "FROM Booking r "
								+ "WHERE :userID in (r.passengers)",
						int.class).setParameter("userID", userID).getSingleResult();
		return result;
	}
	
	public List<Booking> getAllBookingPerUser(Long userID){ 
		List<Booking> result = this.entityManager
				.createQuery("SELECT r "
								+ "FROM Booking r "
								+ "WHERE r.registeredUserOwner.id = :userID",
						Booking.class).setParameter("userID", userID).getResultList();
		if(result.isEmpty())
			return null;
		
		return result;
	}
	
	public Long getIdFromLastReservation() {
		Long result = this.entityManager
				.createQuery("SELECT MAX(r.id) "
							+ "FROM Booking r", Long.class)
				.getSingleResult();
		
		if(result == null)
			return Long.valueOf(0);
		
		return result;
	}
}
