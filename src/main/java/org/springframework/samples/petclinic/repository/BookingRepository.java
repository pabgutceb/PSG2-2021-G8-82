
package org.springframework.samples.petclinic.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Booking;

public interface BookingRepository extends CrudRepository<Booking, Integer> {

	
	Booking findById(int id) throws DataAccessException;
	
	@Override
	@Modifying
	@Query("DELETE FROM Booking b WHERE b = ?1")
	void delete(Booking b);
}
