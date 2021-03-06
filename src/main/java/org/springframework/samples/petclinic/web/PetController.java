/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
@RequestMapping("/owners/{ownerId}")
public class PetController {

	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

	private final PetService petService;
    private final OwnerService ownerService;
    
    private static final String VIEW_OWNER = "redirect:/owners/{ownerId}";
    
   

	@Autowired
	public PetController(final PetService petService, final OwnerService ownerService) {
		this.petService = petService;
                this.ownerService = ownerService;
	}

	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.petService.findPetTypes();
	}

	@ModelAttribute("owner")
	public Owner findOwner(@PathVariable("ownerId") final int ownerId) {
		return this.ownerService.findOwnerById(ownerId);
	}
        
                
	@InitBinder("owner")
	public void initOwnerBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("pet")
	public void initPetBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new PetValidator());
	}

	@GetMapping(value = "/pets/new")
	public String initCreationForm(final Owner owner, final ModelMap model) {
		final Pet pet = new Pet();
		owner.addPet(pet);
		model.put("pet", pet);
		return PetController.VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/pets/new")
	public String processCreationForm(final Owner owner, @Valid final Pet pet, BindingResult result, ModelMap model) {		
		if (result.hasErrors()) {
			System.err.println(result.getAllErrors());
			model.put("pet", pet);
			return PetController.VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}
		else {
                    try{
                    	owner.addPet(pet);
                    	this.petService.savePet(pet);
                    }catch(final DuplicatedPetNameException ex){
                        result.rejectValue("name", "duplicate", "already exists");
                        return PetController.VIEWS_PETS_CREATE_OR_UPDATE_FORM;
                    }
                    return PetController.VIEW_OWNER;
		}
	}

	@GetMapping(value = "/pets/{petId}/edit")
	public String initUpdateForm(@PathVariable("petId") final int petId, final ModelMap model) {
		final Pet pet = this.petService.findPetById(petId);
		model.put("pet", pet);
		return PetController.VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

    /**
     *
     * @param pet
     * @param result
     * @param petId
     * @param model
     * @param owner
     * @param model
     * @return
     */
        @PostMapping(value = "/pets/{petId}/edit")
	public String processUpdateForm(@Valid final Pet pet, final BindingResult result, final Owner owner,@PathVariable("petId") final int petId, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("pet", pet);
			return PetController.VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}
		else {
                        final Pet petToUpdate=this.petService.findPetById(petId);
                        petToUpdate.setBirthDate(pet.getBirthDate());
                        petToUpdate.setName(pet.getName());
                        petToUpdate.setType(pet.getType());
                    try {                    
                        this.petService.savePet(petToUpdate);                    
                    } catch (final DuplicatedPetNameException ex) {
                        result.rejectValue("name", "duplicate", "already exists");
                        return PetController.VIEWS_PETS_CREATE_OR_UPDATE_FORM;
                    }
			return PetController.VIEW_OWNER;
		}
	}


	
	@GetMapping(value = { "/pets/{petId}/delete"})
    public String deletePet(@PathVariable final int ownerId,@PathVariable final int petId, @RequestParam(value = "confirm", required = false) final Boolean confirm, final RedirectAttributes redirectAttributes) {
		final Pet pet = this.petService.findPetById(petId);
        if(confirm!=null && confirm) {
        	redirectAttributes.addFlashAttribute("messageCode", "vet.deleteSuccess");
            redirectAttributes.addFlashAttribute("messageArgument", pet.getName());
            redirectAttributes.addFlashAttribute("messageType", "success");
            this.petService.delete(pet);
        }else {
            redirectAttributes.addFlashAttribute("messageCode", "vet.deleteConfirm");
            redirectAttributes.addFlashAttribute("messageArgument", pet.getName());
            redirectAttributes.addFlashAttribute("messageType", "danger");
            redirectAttributes.addFlashAttribute("buttonMessage", "delete");
            redirectAttributes.addFlashAttribute("buttonURL", String.format("/owners/%d/pets/%d/delete?confirm=true",ownerId, petId));
            
        }
        return PetController.VIEW_OWNER;
    }
	
	@GetMapping(path = "/pets/{petId}/visits/{visitId}/delete")
	public String deleteVisit(@PathVariable("petId") final int petId,@PathVariable("visitId") final int visitId, final ModelMap model, final RedirectAttributes redirectAttributes) {
		final Visit visit = this.petService.findVisitById(visitId);
		final Pet pet = this.petService.findPetById(petId);
		if (pet.getId()!=null) {
			this.petService.delete(visit);
			redirectAttributes.addFlashAttribute("message", "Visita borrada correctamente");
		} else {
			redirectAttributes.addFlashAttribute("message", "Visita no encontrada");
		}

		return PetController.VIEW_OWNER;
	}
	
}
