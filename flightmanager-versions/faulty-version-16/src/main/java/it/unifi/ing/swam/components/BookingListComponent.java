package it.unifi.ing.swam.components;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import it.unifi.ing.swam.dao.BookingDao;
import it.unifi.ing.swam.model.Booking;

@SessionScoped
public class BookingListComponent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private BookingDao bookingDao;

	@Inject
	private LoggedUserComponent loggedUserComponent;

	private List<Booking> bookingList;

	public List<Booking> getBookingList() {
		if (bookingList == null || bookingList.isEmpty()) {
			bookingList = bookingDao.getAllBookingPerUser(loggedUserComponent.getUserID());
			System.out.println("Recuperata la lista di booking");
		}
		return bookingList;
	}

	public void updateBookingListAfterDelete(Booking bookingToRemove) {
		bookingList.remove(bookingToRemove);
	}

	public void clearList() {
		if (bookingList != null)
			bookingList.clear();
	}

	public Object getActualBookingList() {
		return bookingList;
	}
	

}
