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
	private static final String VIEW_ADOPTION_LIST = "redirect:/adoptions";
	private static final String isFormDisabled = "isFormDisabled";
	private static final String adoptionRequestString = "adoptionRequest";
	private static final String messageCode = "messageCode";
	private static final String messageArgument = "messageArgument";
	private static final String messageType = "messageType";
	
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

		final AdoptionRequest newAdoptionRequest = this.adoptionRequestService.create();
		final Collection<Pet> adoptablePets = this.petService.findAvailableForAdoptionRequestByPrincipal();
		final Pet pet = this.petService.findPetById(petId);
		
		newAdoptionRequest.setPet(pet);
		
		if(!newAdoptionRequest.getOwner().equals(pet.getOwner())){
			model.put("pet", pet);
			model.put(AdoptionsController.isFormDisabled, true);

			model.put(AdoptionsController.adoptionRequestString, newAdoptionRequest);
			model.put(AdoptionsController.messageCode, "error.ownerNotAuthorized");
			model.put(AdoptionsController.messageArgument, pet.getOwner().getLastName());
			model.put(AdoptionsController.messageType, "danger");
		}else if(!adoptablePets.contains(pet)) {
			model.put("pet", pet);
			model.put(AdoptionsController.isFormDisabled, true);
			
			model.put(AdoptionsController.adoptionRequestString, newAdoptionRequest);
			model.put(AdoptionsController.messageCode, "error.petAlreadyForAdoption");
			model.put(AdoptionsController.messageArgument, pet.getName());
			model.put(AdoptionsController.messageType, "danger");
		}
		else {
			model.put(AdoptionsController.isFormDisabled, false);
			model.put("pet", pet);
			model.put("adoptablePets", adoptablePets);
			model.put(AdoptionsController.adoptionRequestString, newAdoptionRequest);	
		}
		return AdoptionsController.VIEW_ADOPTION_REQUEST_CREATE_OR_UPDATE_FORM;

	}
	
	@PostMapping(value="/requests/pet/{pet}/new")
	public String processCreationForm(@PathVariable("pet") final int petId, 
			@Valid final AdoptionRequest adoptionRequest, 
			final BindingResult result, 
			final ModelMap model, final RedirectAttributes redirectAttributes) {
		
		if (result.hasErrors()) {
			model.put(AdoptionsController.adoptionRequestString, adoptionRequest);
			return AdoptionsController.VIEW_ADOPTION_REQUEST_CREATE_OR_UPDATE_FORM;
		}else {
			try {
				this.adoptionRequestService.saveAdoptionRequest(adoptionRequest);
				redirectAttributes.addFlashAttribute(AdoptionsController.messageCode, "adoptions.newAdoptionRequestSuccess");
				redirectAttributes.addFlashAttribute(AdoptionsController.messageArgument, adoptionRequest.getPet().getName());
				redirectAttributes.addFlashAttribute(AdoptionsController.messageType, "success");
				return "redirect:/";
			} catch (final PetTransactionFromUnauthorizedOwner e) {
				adoptionRequest.setPet(null);
				model.put(AdoptionsController.adoptionRequestString, adoptionRequest);
				return String.format("redirect:/adoptions/requests/pet/%d/new", petId);
			}
			
		}
		
	}
	
	@GetMapping(value = { "" })
	public String showAdoptionList(final Map<String, Object> model) {
		final Owner owner = this.ownerService.getPrincipal();
		model.put("owner", owner);
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
	public String initNewAdoptionAplicationForm(@PathVariable("adoptionRequestId") final int adoptionRequestId, final Map<String, Object> model) {
		final AdoptionRequest adoptionRequest = this.adoptionRequestService.findById(adoptionRequestId);
		final Owner owner = this.ownerService.getPrincipal();
		if(adoptionRequest.getOwner().equals(owner)) {
			return  AdoptionsController.VIEW_ADOPTION_LIST;
		}
		final AdoptionApplication adoptionApplication = new AdoptionApplication();
		adoptionApplication.setAdoptionRequest(this.adoptionRequestService.findById(adoptionRequestId));
		
		model.put("adoptionApplication",adoptionApplication);
		
		return AdoptionsController.ADOPTION_APPLICATION_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/{adoptionRequestId}/application/new")
	public String processNewAdoptionAplicationForm(@Valid final AdoptionApplication adoptionApplication, final BindingResult result) {
		adoptionApplication.setOwner(this.ownerService.getPrincipal());
		if (result.hasErrors()) {
			return AdoptionsController.ADOPTION_APPLICATION_CREATE_OR_UPDATE_FORM;
		}
		else {
				this.adoptionApplicationService.saveAdoptionApplication(adoptionApplication);
				return  AdoptionsController.VIEW_ADOPTION_LIST;
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
    public String transferPet(@PathVariable final int ownerId,@PathVariable final int adoptionRequestId, @PathVariable final int adoptionApplicationId, final RedirectAttributes redirectAttributes) {
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
		} catch (final PetTransactionFromUnauthorizedOwner e) {

			e.printStackTrace();
		}
		
		
				
        return  AdoptionsController.VIEW_ADOPTION_LIST;
    }

}
