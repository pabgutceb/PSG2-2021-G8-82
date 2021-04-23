
package org.springframework.samples.petclinic.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Booking;

public interface BookingRepository extends CrudRepository<Booking, Integer> {

	
	Booking findBookingById(int id) throws DataAccessException;
	
	@Override
	@Modifying
	@Query("DELETE FROM Booking b WHERE b = ?1")
	void delete(Booking b);

	@Query("SELECT b FROM Booking b WHERE ((?1 <= b.startDate AND ?2 > b.startDate) "
			+ "OR (?1 < b.finishDate AND ?2 >= b.finishDate) "
			+ "OR (?1 >= b.startDate AND ?2 <= b.finishDate))"
			+ "AND b.pet.id = ?3 "
			+ "AND b.canceled = false")
	List<Booking> getBookingByDate(LocalDate startDate, LocalDate finishDate, Integer petId);
}
