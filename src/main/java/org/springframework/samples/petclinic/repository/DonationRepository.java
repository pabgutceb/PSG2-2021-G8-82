package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Donation;

public interface DonationRepository extends CrudRepository<Donation,Integer>{

	
	Donation findDonationById(int id) throws DataAccessException;
	
	@Override
	Collection<Donation> findAll() throws DataAccessException;
	
}
