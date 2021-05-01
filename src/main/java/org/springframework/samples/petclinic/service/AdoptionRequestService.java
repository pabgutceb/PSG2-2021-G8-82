package org.springframework.samples.petclinic.service;

import java.util.Collection;

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
		final Owner principalOwner = this.ownerService.getPrincipal();
		final AdoptionRequest res = new AdoptionRequest();
		res.setOwner(principalOwner);
		
		return res;
	}
	
	@Transactional(readOnly = true)
	public AdoptionRequest findByPet(final Pet pet) {
		return this.adoptionRequestRepository.findByPet(pet);
	}
	
	@Transactional
	public void saveAdoptionRequest(final AdoptionRequest adoptionRequest) 
			throws PetTransactionFromUnauthorizedOwner {
		final Owner principalOwner = this.ownerService.getPrincipal();
		this.petService.checkIfOwnerIsAuthorized(adoptionRequest.getPet(), principalOwner);
		
		if(adoptionRequest.isNew()) {
			// when the AdoptionRequest is new
			
			
			adoptionRequest.setOwner(principalOwner);
			this.adoptionRequestRepository.save(adoptionRequest);

		}else {
			// when the AdoptionRequest is being updated
			this.adoptionRequestRepository.save(adoptionRequest);
		}
	}
	
	public Collection<AdoptionRequest> findAll() {
		return this.adoptionRequestRepository.findAll();
	}

	public AdoptionRequest findById(final int adoptionRequestId) {
		return this.adoptionRequestRepository.findById(adoptionRequestId).orElse(null);
	}

	@Transactional(readOnly = true)
	public AdoptionRequest findByOwner(final Owner owner) {
		return this.adoptionRequestRepository.findByOwner(owner);
	}
}
