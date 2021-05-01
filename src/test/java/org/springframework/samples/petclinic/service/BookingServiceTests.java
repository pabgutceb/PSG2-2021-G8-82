package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Booking;
import org.springframework.samples.petclinic.service.exceptions.DateOverlapException;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class BookingServiceTests {
	
	@Autowired
	protected BookingService bookingService;
	
	@Test
	void shouldFindBookingCorrectId() {
		Booking existingBooking = bookingService.findBookingbyId(9);
		assertThat(existingBooking.getPet().getName()).isEqualTo("Samantha");
		assertThat(existingBooking.getCanceled()).isFalse();
	}
	
	@Test
	void shouldSoftDeleteBooking() {
		Booking existingBooking = bookingService.findBookingbyId(9);
		assertThat(existingBooking.getCanceled()).isFalse();
		
		bookingService.delete(existingBooking);
		assertThat(existingBooking.getCanceled()).isTrue();
	}
	
	@Test
	void shouldInsertBooking() throws DataAccessException, DateOverlapException {
		Booking existingBooking = bookingService.findBookingbyId(10);
		
		Booking newBooking = new Booking();
		newBooking.setPet(existingBooking.getPet());
		newBooking.setCanceled(false);
		newBooking.setStartDate(existingBooking.getStartDate().plusMonths(8));
		newBooking.setFinishDate(existingBooking.getFinishDate().plusMonths(8));
		
		bookingService.saveBooking(newBooking);
		assertThat(bookingService.findBookingbyId(existingBooking.getId()+1)).isNotNull();
		
	}
	
	@Test
	void shouldNotInsertOverlappingBooking() {
	    Exception dateOverlapException = assertThrows(DateOverlapException.class, () -> {
			Booking existingBooking = bookingService.findBookingbyId(10);
			
			Booking newBooking = new Booking();
			newBooking.setPet(existingBooking.getPet());
			newBooking.setCanceled(false);
			newBooking.setStartDate(existingBooking.getStartDate());
			newBooking.setFinishDate(existingBooking.getFinishDate());
			
			bookingService.saveBooking(newBooking);
	    });
	    
	    assertThat(dateOverlapException.getMessage()).isNullOrEmpty();
	}
	

}
