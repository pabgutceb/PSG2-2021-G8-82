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
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;

/**
 * Spring Data JPA specialization of the {@link PetRepository} interface
 *
 * @author Michael Isvy
 * @since 15.1.2013
 */
public interface PetRepository extends CrudRepository<Pet, Integer> {

	/**
	 * Retrieve all <code>PetType</code>s from the data store.
	 * @return a <code>Collection</code> of <code>PetType</code>s
	 */
	@Query("SELECT ptype FROM PetType ptype ORDER BY ptype.name")
	List<PetType> findPetTypes() throws DataAccessException;
	
	/**
	 * Retrieve a <code>Pet</code> from the data store by id.
	 * @param id the id to search for
	 * @return the <code>Pet</code> if found
	 * @throws org.springframework.dao.DataRetrievalFailureException if not found
	 */
	Pet findById(int id) throws DataAccessException;

	/**
	 * Save a <code>Pet</code> to the data store, either inserting or updating it.
	 * @param pet the <code>Pet</code> to save
	 * @see BaseEntity#isNew
	 */
	@Override
	@Modifying
	@Query("DELETE FROM Pet p WHERE p = ?1")
	void delete(Pet pet);
	
	/**
	 * Retrieve all Pets that are available for opening an adoption process.
	 * @return a Collection of Pets
	 */
	//pet <> adopReq.pet and || AdoptionRequest adopReq
	@Query("SELECT pet FROM Pet pet WHERE pet.owner.id = :ownerId AND pet NOT IN (SELECT DISTINCT a.pet FROM AdoptionRequest a)")
	Collection<Pet> findAvailableForAdoptionRequestByOwnerId(@Param("ownerId") int ownerId);
}
