package org.springframework.samples.petclinic.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Booking;
import org.springframework.samples.petclinic.service.exceptions.DateOverlapException;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class BookingServiceTests {
	
	@Autowired
	protected BookingService bookingService;
	
	@Test
	void shouldFindBookingCorrectId() {
		Booking existingBooking = this.bookingService.findBookingbyId(9);
		Assertions.assertThat(existingBooking.getPet().getName()).isEqualTo("Samantha");
		Assertions.assertThat(existingBooking.getCanceled()).isFalse();
	}
	
	@Test
	void shouldSoftDeleteBooking() {
		Booking existingBooking = this.bookingService.findBookingbyId(9);
		Assertions.assertThat(existingBooking.getCanceled()).isFalse();
		
		this.bookingService.delete(existingBooking);
		Assertions.assertThat(existingBooking.getCanceled()).isTrue();
	}
	
	@Test
	void shouldInsertBooking() throws DataAccessException, DateOverlapException {
		Booking existingBooking = this.bookingService.findBookingbyId(10);
		
		Booking newBooking = new Booking();
		newBooking.setPet(existingBooking.getPet());
		newBooking.setCanceled(false);
		newBooking.setStartDate(existingBooking.getStartDate().plusMonths(8));
		newBooking.setFinishDate(existingBooking.getFinishDate().plusMonths(8));
		
		this.bookingService.saveBooking(newBooking);
		Assertions.assertThat(this.bookingService.findBookingbyId(existingBooking.getId()+1)).isNotNull();
		
	}
	
	@Test
	void shouldNotInsertOverlappingBooking() {
	    Exception dateOverlapException = org.junit.jupiter.api.Assertions.assertThrows(DateOverlapException.class, () -> {
			Booking existingBooking = this.bookingService.findBookingbyId(10);
			
			Booking newBooking = new Booking();
			newBooking.setPet(existingBooking.getPet());
			newBooking.setCanceled(false);
			newBooking.setStartDate(existingBooking.getStartDate());
			newBooking.setFinishDate(existingBooking.getFinishDate());
			
			this.bookingService.saveBooking(newBooking);
	    });
	    
	    Assertions.assertThat(dateOverlapException.getMessage()).isNullOrEmpty();
	}
	

}
