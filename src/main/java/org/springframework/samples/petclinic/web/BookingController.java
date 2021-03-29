package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Booking;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.BookingService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BookingController {

	private final BookingService bookingService;
	private final PetService petService;

	@Autowired
	public BookingController(final BookingService bookingService,final PetService petService) {
		this.bookingService = bookingService;
		this.petService = petService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@ModelAttribute("booking")
	public Booking loadBookingWithPet(@PathVariable("petId") final int petId) {
		final Pet pet = this.petService.findPetById(petId);
		final Booking booking = new Booking();
		booking.setPet(pet);
		return booking;
	}
	
	@GetMapping(value = "/owners/*/pets/{petId}/booking/new")
	public String initNewBookingForm(@PathVariable("petId") final int petId, final Map<String, Object> model) {
		return "pets/createOrUpdateBookingForm";
	}

	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/booking/new")
	public String processNewBookingForm(@Valid final Booking booking, final BindingResult result) {
		if(booking.getFinishDate()!=null && booking.getStartDate()!=null && !booking.getFinishDate().isAfter(booking.getStartDate())) {
			final FieldError error = new FieldError("finishDate","finishDate", "Finish date must be after start date");
			result.addError(error);
		}
		if (result.hasErrors()) {
			return "pets/createOrUpdateBookingForm";
		}
		else {
			this.bookingService.saveBooking(booking);
			return "redirect:/owners/{ownerId}";
		}
	}
	
	/*@GetMapping(path = "/booking/{bookingId}/delete")
	public String deleteBooking(@PathVariable("bookingId") final int bookingId, final ModelMap model, final RedirectAttributes redirectAttributes) {
		final Booking b = this.bookingService.
		if (pet.getId()!=null) {
			this.petService.delete(pet);
			redirectAttributes.addFlashAttribute("message", "Pet successfully deleted!");
		} else {
			redirectAttributes.addFlashAttribute("message", "Pet not found!");
		}

		return "redirect:/owners/{ownerId}";
	}*/

}