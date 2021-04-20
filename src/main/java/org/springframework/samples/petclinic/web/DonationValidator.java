package org.springframework.samples.petclinic.web;

import org.springframework.samples.petclinic.model.Donation;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class DonationValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return Donation.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Donation donation = (Donation) target;
		
		if((donation.getCause().getBudgetTarget()-donation.getCause().getTotalBudget())< donation.getAmount() ) {
			errors.rejectValue("amount", "passLimits", "The amount of the donation pass the limit of the cause");
		}
		}

}
