package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.repository.CauseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CauseService {
	
	private final CauseRepository causeRepository;
	
	@Autowired
	public CauseService(final CauseRepository causeRepository) {
		this.causeRepository = causeRepository;
	}
	
	
	@Transactional
	public void saveCause(final Cause cause) throws DataAccessException {
		this.causeRepository.save(cause);
	}
	
	
	public Cause findCauseById(final int causeId) {
		return this.causeRepository.findCauseById(causeId);
	}
	
	
	public Collection<Cause> findCauses() throws DataAccessException {
		return this.causeRepository.findAll();
		
	}
	
	public void delete(final Cause cause) {
		this.causeRepository.delete(cause);
	}

}
