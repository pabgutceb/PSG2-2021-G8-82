package org.springframework.samples.petclinic.web;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.AdoptionRequest;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.AdoptionRequestService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.PetTransactionFromUnauthorizedOwner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adoptions")
public class AdoptionRequestController {
	
	private static final String VIEW_ADOPTION_REQUEST_CREATE_OR_UPDATE_FORM = 
			"adoptionRequests/createOrUpdateAdoptionRequestForm";
	
	@Autowired
	private AdoptionRequestService adoptionRequestService;
	
	@Autowired
	private PetService petService;
	
	@GetMapping(value = "/new")
	public String initCreationForm(final ModelMap model) {
		Collection<Pet> adoptablePets = petService.findAvailableForAdoptionRequestByPrincipal();
		model.put("adoptablePets", adoptablePets);
		model.put("adoptionRequest", adoptionRequestService.create());
		return VIEW_ADOPTION_REQUEST_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value="/new")
	public String processCreationForm(@Valid AdoptionRequest adoptionRequest, 
			final BindingResult result, 
			final ModelMap model) {
		if (result.hasErrors()) {
			model.put("adoptionRequest", adoptionRequest);
			return VIEW_ADOPTION_REQUEST_CREATE_OR_UPDATE_FORM;
		}else {
			try {
				adoptionRequestService.saveAdoptionRequest(adoptionRequest);
				return "redirect:/";
			} catch (PetTransactionFromUnauthorizedOwner e) {
				return VIEW_ADOPTION_REQUEST_CREATE_OR_UPDATE_FORM;
			}
			
		}
		
	}

}
