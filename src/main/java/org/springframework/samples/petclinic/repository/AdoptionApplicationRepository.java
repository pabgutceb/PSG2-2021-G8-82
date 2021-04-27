package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.AdoptionApplication;
import org.springframework.samples.petclinic.model.AdoptionRequest;

public interface AdoptionApplicationRepository extends CrudRepository<AdoptionApplication, Integer>{
	
	public List<AdoptionApplication> findByAdoptionRequest(AdoptionRequest request);
	
	@Override
	Collection<AdoptionApplication> findAll() throws DataAccessException;
}
