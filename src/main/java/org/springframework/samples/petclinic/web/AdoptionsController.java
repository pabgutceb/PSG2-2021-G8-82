package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.AdoptionApplication;
import org.springframework.samples.petclinic.model.AdoptionRequest;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.AdoptionApplicationService;
import org.springframework.samples.petclinic.service.AdoptionRequestService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.PetTransactionFromUnauthorizedOwner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/adoptions")
public class AdoptionsController {
	
	private static final String VIEW_ADOPTION_REQUEST_CREATE_OR_UPDATE_FORM = 
			"adoptionRequests/createOrUpdateAdoptionRequestForm";
	private static final String ADOPTION_APPLICATION_CREATE_OR_UPDATE_FORM = 
			"adoptionRequests/createOrUpdateAdoptionApplicationForm";
	
	@Autowired
	private AdoptionRequestService adoptionRequestService;

	@Autowired
	private AdoptionApplicationService adoptionApplicationService;
	
	@Autowired
	private OwnerService ownerService;
	
	@Autowired
	private PetService petService;
	
	@GetMapping(value = "/requests/pet/{petId}/new")
	public String initCreationForm(@PathVariable("petId") final int petId, final ModelMap model) {
		//TODO: Change "redirect:/" to "redirect:/adoptions/list"
		final AdoptionRequest newAdoptionRequest = this.adoptionRequestService.create();
		final Collection<Pet> adoptablePets = this.petService.findAvailableForAdoptionRequestByPrincipal();
		final Pet pet = this.petService.findPetById(petId);
		
		newAdoptionRequest.setPet(pet);
		
		if(!newAdoptionRequest.getOwner().equals(pet.getOwner())){
			model.put("pet", pet);
			model.put("isFormDisabled", true);

			model.put("adoptionRequest", newAdoptionRequest);
			model.put("messageCode", "error.ownerNotAuthorized");
			model.put("messageArgument", pet.getOwner().getLastName());
			model.put("messageType", "danger");
			return AdoptionsController.VIEW_ADOPTION_REQUEST_CREATE_OR_UPDATE_FORM;
		}else if(!adoptablePets.contains(pet)) {
			model.put("pet", pet);
			model.put("isFormDisabled", true);
			
			model.put("adoptionRequest", newAdoptionRequest);
			model.put("messageCode", "error.petAlreadyForAdoption");
			model.put("messageArgument", pet.getName());
			model.put("messageType", "danger");
			return AdoptionsController.VIEW_ADOPTION_REQUEST_CREATE_OR_UPDATE_FORM;
		}
		model.put("isFormDisabled", false);
		model.put("pet", pet);
		model.put("adoptablePets", adoptablePets);
		model.put("adoptionRequest", newAdoptionRequest);
		return AdoptionsController.VIEW_ADOPTION_REQUEST_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value="/requests/pet/{pet}/new")
	public String processCreationForm(@PathVariable("pet") final int petId, 
			@Valid final AdoptionRequest adoptionRequest, 
			final BindingResult result, 
			final ModelMap model, final RedirectAttributes redirectAttributes) {
		
		if (result.hasErrors()) {
			model.put("adoptionRequest", adoptionRequest);
			return AdoptionsController.VIEW_ADOPTION_REQUEST_CREATE_OR_UPDATE_FORM;
		}else {
			try {
				this.adoptionRequestService.saveAdoptionRequest(adoptionRequest);
				redirectAttributes.addFlashAttribute("messageCode", "adoptions.newAdoptionRequestSuccess");
				redirectAttributes.addFlashAttribute("messageArgument", adoptionRequest.getPet().getName());
				redirectAttributes.addFlashAttribute("messageType", "success");
				return "redirect:/";
			} catch (final PetTransactionFromUnauthorizedOwner e) {
				adoptionRequest.setPet(null);
				model.put("adoptionRequest", adoptionRequest);
				return String.format("redirect:/adoptions/requests/pet/%d/new", petId);
			}
			
		}
		
	}
	
	@GetMapping(value = { "" })
	public String showAdoptionList(final Map<String, Object> model) {
		final Collection<AdoptionRequest> adoptions = this.adoptionRequestService.findAll();
		model.put("adoptions", adoptions);
		return "adoptionRequests/adoptionList";
	}
	
	@GetMapping(value = { "/application" })
	public String adoptionApplicationForm(final Map<String, Object> model) {
		final Collection<AdoptionRequest> adoptions = this.adoptionRequestService.findAll();
		model.put("adoptions", adoptions);
		return "adoptionRequests/adoptionList";
	}
	
	@GetMapping(value = "/{adoptionRequestId}/application/new")
	public String initNewBookingForm(@PathVariable("adoptionRequestId") final int adoptionRequestId, final Map<String, Object> model) {
		AdoptionApplication adoptionApplication = new AdoptionApplication();
		adoptionApplication.setAdoptionRequest(this.adoptionRequestService.findById(adoptionRequestId));
		model.put("adoptionApplication",adoptionApplication);
		
		return AdoptionsController.ADOPTION_APPLICATION_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/{adoptionRequestId}/application/new")
	public String processNewBookingForm(@Valid final AdoptionApplication adoptionApplication, final BindingResult result) {
		adoptionApplication.setOwner(this.ownerService.getPrincipal());
		if (result.hasErrors()) {
			return AdoptionsController.ADOPTION_APPLICATION_CREATE_OR_UPDATE_FORM;
		}
		else {
				this.adoptionApplicationService.saveAdoptionApplication(adoptionApplication);
				return "redirect:/adoptions";
		}
	}
	
	@GetMapping(value = { "/applicationList" })
	public String listApplicationForm(final Map<String, Object> model) {
		final Owner principal = this.ownerService.getPrincipal();
		final AdoptionRequest request = this.adoptionRequestService.findByOwner(principal);
		final List<AdoptionApplication> applications= this.adoptionApplicationService.findByRequest(request);
		model.put("applications", applications);
		return "adoptionApplication/applicationList";
	}
	
	@GetMapping(value = { "/applicationList/owners/{ownerId}/adoptions/{adoptionRequestId}/applications/{adoptionApplicationId}"})
    public String transferPet(@PathVariable int ownerId,@PathVariable int adoptionRequestId, @PathVariable int adoptionApplicationId, RedirectAttributes redirectAttributes) {
		final Owner principal = this.ownerService.getPrincipal();
		final Owner ownerSolicitante= this.ownerService.findOwnerById(ownerId);
		final AdoptionRequest request = this.adoptionRequestService.findById(adoptionRequestId);
		final AdoptionApplication application= this.adoptionApplicationService.findById(adoptionApplicationId);
		ownerSolicitante.addPet(request.getPet());
		principal.removePet(request.getPet());
		request.setApprovedApplication(application);
		this.ownerService.saveOwner(ownerSolicitante);
		this.ownerService.saveOwner(principal);
		try {
			this.adoptionRequestService.saveAdoptionRequest(request);
		} catch (PetTransactionFromUnauthorizedOwner e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
				
        return "redirect:/adoptions";
    }

}
