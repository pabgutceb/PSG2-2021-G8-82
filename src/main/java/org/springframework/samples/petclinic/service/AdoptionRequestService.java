package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.AdoptionRequest;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.AdoptionRequestRepository;
import org.springframework.samples.petclinic.service.exceptions.PetTransactionFromUnauthorizedOwner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdoptionRequestService {
	
	@Autowired
	private AdoptionRequestRepository adoptionRequestRepository;
	
	@Autowired
	private OwnerService ownerService;
	
	@Autowired
	private PetService petService;
	
	@Transactional(readOnly = true)
	public AdoptionRequest create() {
		Owner principalOwner = ownerService.getPrincipal();
		AdoptionRequest res = new AdoptionRequest();
		res.setOwner(principalOwner);
		
		return res;
	}
	
	@Transactional(readOnly = true)
	public AdoptionRequest findByPet(Pet pet) {
		return adoptionRequestRepository.findByPet(pet);
	}
	
	@Transactional
	public void saveAdoptionRequest(final AdoptionRequest adoptionRequest) 
			throws PetTransactionFromUnauthorizedOwner {
		Owner principalOwner = ownerService.getPrincipal();
		petService.checkIfOwnerIsAuthorized(adoptionRequest.getPet(), principalOwner);
		
		if(adoptionRequest.isNew()) {
			// when the AdoptionRequest is new
			
			
			adoptionRequest.setOwner(principalOwner);
			adoptionRequestRepository.save(adoptionRequest);

		}else {
			// when the AdoptionRequest is being updated
			adoptionRequestRepository.save(adoptionRequest);
		}
	}

}
