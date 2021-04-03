package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import org.springframework.samples.petclinic.model.Booking;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class BookingValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Booking.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Booking booking = (Booking) target;
		
		if(booking.getStartDate()==null) {
			errors.rejectValue("startDate", "notEmpty", "Must not be empty");
		}

		if(booking.getFinishDate()==null) {
			errors.rejectValue("finishDate", "notEmpty", "Must not be empty");
		}

		if(booking.getStartDate()!=null && booking.getFinishDate()!=null && booking.getFinishDate().isBefore(booking.getStartDate())) {
			errors.rejectValue("finishDate", "FinishDateAfterStartDate", "Finish date must be after start date"); 
		}

		if(booking.getStartDate()!=null && booking.getStartDate().isBefore(LocalDate.now())) {
			errors.rejectValue("startDate", "notPastDate", "You cannot make a reservation for a past date"); 
		}
		
	}

	
}
