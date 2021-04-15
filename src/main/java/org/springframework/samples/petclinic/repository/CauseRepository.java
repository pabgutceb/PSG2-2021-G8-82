package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Cause;

public interface CauseRepository extends CrudRepository<Cause,Integer> {

	Cause findCauseById(int id) throws DataAccessException;
	
	@Override
	@Modifying
	@Query("DELETE FROM Cause c WHERE c = ?1")
	void delete(Cause c);
	
	@Override
	Collection<Cause> findAll() throws DataAccessException;
	
	
	
	
	
	
	

}
