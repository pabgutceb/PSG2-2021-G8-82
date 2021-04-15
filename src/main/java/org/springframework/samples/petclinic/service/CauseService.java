package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.repository.CauseRepository;
import org.springframework.samples.petclinic.service.exceptions.DateOverlapException;
import org.springframework.transaction.annotation.Transactional;

public class CauseService {
	
	private final CauseRepository causeRepository;
	
	@Autowired
	public CauseService(final CauseRepository causeRepository) {
		this.causeRepository = causeRepository;
	}
	
	@Transactional(readOnly = true)
	public Cause findCauseById(final Integer id) throws DataAccessException {
		return this.causeRepository.findCauseById(id);
	}
	
	@Transactional(rollbackFor = DateOverlapException.class)
	public void saveCause(final Cause cause) throws DataAccessException, DateOverlapException {
		final Integer otherCause = (int) this.causeRepository.findAll().stream().filter(b->b.getId()!=cause.getId()).count();
        if (otherCause!=0) {            	
        	throw new DateOverlapException();
        } else
    		this.causeRepository.save(cause);              
	}
	
	public void delete(final Cause cause) {
		this.causeRepository.delete(cause);
	}

}
