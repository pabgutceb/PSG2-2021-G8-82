package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.AdoptionApplication;
import org.springframework.samples.petclinic.model.AdoptionRequest;
import org.springframework.samples.petclinic.repository.AdoptionApplicationRepository;
import org.springframework.samples.petclinic.service.exceptions.DateOverlapException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdoptionApplicationService {

	private final AdoptionApplicationRepository adoptionApplicationRepository;

	@Autowired
	public AdoptionApplicationService(final AdoptionApplicationRepository adoptionApplicationRepository) {
		this.adoptionApplicationRepository = adoptionApplicationRepository;
	}

	@Transactional(rollbackFor = DateOverlapException.class)
	public void saveAdoptionApplication(final AdoptionApplication adoptionApplication) throws DataAccessException {
    	this.adoptionApplicationRepository.save(adoptionApplication);              
	}
	
	@Transactional(readOnly = true)
	public List<AdoptionApplication> findByRequest(final AdoptionRequest request) {
		return this.adoptionApplicationRepository.findByAdoptionRequest(request);
	}
	
	public Collection<AdoptionApplication> findAll() {
		return this.adoptionApplicationRepository.findAll();
	}
	
	public AdoptionApplication findById(int adoptionApplicationId) {
		return this.adoptionApplicationRepository.findById(adoptionApplicationId).get();
	}
}
