package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.AdoptionRequest;
import org.springframework.samples.petclinic.model.Pet;

public interface AdoptionRequestRepository extends CrudRepository<AdoptionRequest, Integer>{
	
	public AdoptionRequest findByPet(Pet pet);
	
	
	@Override
	Collection<AdoptionRequest> findAll() throws DataAccessException;

}
