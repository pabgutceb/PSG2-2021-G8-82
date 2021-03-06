/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;

/**
 * Repository class for <code>Vet</code> domain objects All method names are compliant
 * with Spring Data naming conventions so this interface can easily be extended for Spring
 * Data See here:
 * http://static.springsource.org/spring-data/jpa/docs/current/reference/html/jpa.repositories.html#jpa.query-methods.query-creation
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 */
public interface VetRepository extends CrudRepository<Vet, Integer>{

	/**
	 * Retrieve all <code>Vet</code>s from the data store.
	 * @return a <code>Collection</code> of <code>Vet</code>s
	 */
	@Override
	Collection<Vet> findAll() throws DataAccessException;
	
	Vet findById(int id) throws DataAccessException;
	
	@Override
	@Modifying
	@Query("DELETE FROM Vet vet WHERE vet = ?1")
	void delete(Vet v);

	/**
	 * Retrieve all <code>Specialty</code>s from the data store.
	 * @return a <code>Collection</code> of <code>Specialty</code>s
	 */
	@Query("SELECT vspec FROM Specialty vspec ORDER BY vspec.name")
	List<Specialty> findSpecialties() throws DataAccessException;

	@Query("SELECT vet FROM Vet vet WHERE vet.firstName = ?1 AND vet.lastName = ?2")
	Vet getVetByName(String firstName, String lastName);

	/**
	 * Save an <code>Owner</code> to the data store, either inserting or updating it.
	 * @param owner the <code>Owner</code> to save
	 * @return 
	 * @see BaseEntity#isNew
	 */
}
