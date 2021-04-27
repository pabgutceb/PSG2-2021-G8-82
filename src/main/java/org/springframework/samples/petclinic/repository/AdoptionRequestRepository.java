package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.AdoptionRequest;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;

public interface AdoptionRequestRepository extends CrudRepository<AdoptionRequest, Integer>{
	
	public AdoptionRequest findByPet(Pet pet);
	
	public AdoptionRequest findByOwner(Owner owner);
	
	@Override
	@Modifying
	@Query("DELETE FROM AdoptionRequest o WHERE o = ?1")
	void delete(AdoptionRequest o);
	
	@Override
	Collection<AdoptionRequest> findAll() throws DataAccessException;

}
