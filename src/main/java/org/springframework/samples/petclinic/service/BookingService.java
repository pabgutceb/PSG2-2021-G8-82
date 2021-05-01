package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Booking;
import org.springframework.samples.petclinic.repository.BookingRepository;
import org.springframework.samples.petclinic.service.exceptions.DateOverlapException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

	private final BookingRepository bookingRepository;

	@Autowired
	public BookingService(final BookingRepository bookingRepository) {
		this.bookingRepository = bookingRepository;
	}
	

	@Transactional(readOnly = true)
	public Booking findBookingbyId(final Integer id) throws DataAccessException {
		return this.bookingRepository.findBookingById(id);
	}

	@Transactional(rollbackFor = DateOverlapException.class)
	public void saveBooking(final Booking booking) throws DataAccessException, DateOverlapException {
		final Integer otherBooking = (int) this.bookingRepository.getBookingByDate(booking.getStartDate(),booking.getFinishDate(),booking.getPet().getId()).stream().filter(b->!b.getId().equals(booking.getId())).count();
        if (otherBooking!=0) {            	
        	throw new DateOverlapException();
        }else
    		this.bookingRepository.save(booking);              
		}	
	
	public void delete(final Booking booking) {
		booking.setCanceled(true);
		this.bookingRepository.save(booking);
	}
	
}
