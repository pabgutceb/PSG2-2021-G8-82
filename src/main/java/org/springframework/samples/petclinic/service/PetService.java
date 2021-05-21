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
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Booking;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.BookingRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.samples.petclinic.service.exceptions.PetTransactionFromUnauthorizedOwner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class PetService {

	private final PetRepository petRepository;
	
	private final VisitRepository visitRepository;
	
	private final BookingRepository bookingRepository;
	
	@Autowired
	private OwnerService ownerService;
	

	@Autowired
	public PetService(final PetRepository petRepository,
			final VisitRepository visitRepository,
			final BookingRepository bookingRepository) {
		this.petRepository = petRepository;
		this.visitRepository = visitRepository;
		this.bookingRepository = bookingRepository;
	}

	@Transactional(readOnly = true)
	public Collection<PetType> findPetTypes() throws DataAccessException {
		return this.petRepository.findPetTypes();
	}
	
	@Transactional
	public void saveVisit(final Visit visit) throws DataAccessException {
		this.visitRepository.save(visit);
	}

	@Transactional(readOnly = true)
	public Pet findPetById(final int id) throws DataAccessException {
		return this.petRepository.findById(id);
	}
	
	@Transactional(readOnly = true)
	public Visit findVisitById(final int id) throws DataAccessException {
		return this.visitRepository.findById(id);
	}
	
	@Transactional(readOnly = true)
	public Collection<Pet> findAvailableForAdoptionRequestByOwner(final Owner owner){
		return this.petRepository.findAvailableForAdoptionRequestByOwnerId(owner.getId());
	}
	
	@Transactional(readOnly = true)
	public Collection<Pet> findAvailableForAdoptionRequestByPrincipal(){
		final Owner ownerPrincipal = this.ownerService.getPrincipal();
		return this.findAvailableForAdoptionRequestByOwner(ownerPrincipal);
	}

	@Transactional(rollbackFor = DuplicatedPetNameException.class)
	public void savePet(final Pet pet) throws DataAccessException, DuplicatedPetNameException {
			Pet otherPet=pet.getOwner().getPetwithIdDifferent(pet.getName(), pet.getId());
            if (StringUtils.hasLength(pet.getName()) &&  (otherPet!= null && !otherPet.getId().equals(pet.getId()))) {            	
            	throw new DuplicatedPetNameException();
            }else
                this.petRepository.save(pet);                
	}
	
	/**
	Check if an Owner has authorization to perform an action related to a pet
	@param
		pet - The pet related to the action
		owner - The owner that is trying to perform the action over a pet
	 * @throws PetTransactionFromUnauthorizedOwner if the Owner is not the real owner of the pet
	@throws what kind of exception does this method throw
	*/
	@Transactional(readOnly = true)
	public void checkIfOwnerIsAuthorized(final Pet pet, final Owner owner) throws PetTransactionFromUnauthorizedOwner {
		final Pet petFromBD = this.findPetById(pet.getId());
		final Owner ownerFromBD = this.ownerService.findOwnerById(owner.getId());
		
		if(!petFromBD.getOwner().equals(ownerFromBD)) {
			throw new PetTransactionFromUnauthorizedOwner(
					String.format(
							"The owner%d is not authorized to perform actions over pet%d, only owner%d is authorized", 
							ownerFromBD.getId(), 
							petFromBD.getId(),
							petFromBD.getOwner().getId()));
		}
	}

	@Transactional(readOnly = true)
	public Collection<Visit> findVisitsByPetId(final int petId) {
		return this.visitRepository.findByPetId(petId);
	}
	
	@Transactional
	public void delete(final Pet pet) {
		for (final Visit v : pet.getVisits()) {
			this.visitRepository.delete(v);
		}
		for (final Booking b : pet.getAllBookings()) {
			this.bookingRepository.delete(b);
		}
		this.petRepository.delete(pet);
	}

	@Transactional
	public void delete(final Visit visit) {
			this.visitRepository.delete(visit);
	}

}
