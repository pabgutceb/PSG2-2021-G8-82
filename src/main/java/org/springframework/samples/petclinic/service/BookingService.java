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

	@Transactional(rollbackFor = DateOverlapException.class)
	public void saveBooking(final Booking booking) throws DataAccessException, DateOverlapException {
		final Integer otherBooking = (int) this.bookingRepository.getBookingByDate(booking.getStartDate(),booking.getFinishDate()).stream().filter(b->b.getId()!=booking.getId()).count();
        if (otherBooking!=0) {            	
        	throw new DateOverlapException();
        }else
    		this.bookingRepository.save(booking);              
		}	
	
	public void delete(final Booking booking) {
		this.bookingRepository.delete(booking);
	}
	
}
