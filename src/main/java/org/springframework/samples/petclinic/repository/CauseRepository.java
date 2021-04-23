package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Cause;

public interface CauseRepository extends CrudRepository<Cause,Integer> {

	@Query("SELECT cause FROM Cause cause WHERE cause.id =:id")
	Cause findCauseById(int id) throws DataAccessException;
	
	@Override
	Collection<Cause> findAll() throws DataAccessException;
	
	
	

	
	
	

}
