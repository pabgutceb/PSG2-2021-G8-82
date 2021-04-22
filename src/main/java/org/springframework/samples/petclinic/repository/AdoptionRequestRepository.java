package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.AdoptionRequest;
import org.springframework.samples.petclinic.model.Pet;

public interface AdoptionRequestRepository extends CrudRepository<AdoptionRequest, Integer>{
	
	public AdoptionRequest findByPet(Pet pet);

}
