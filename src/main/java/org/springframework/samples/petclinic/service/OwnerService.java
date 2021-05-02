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
import org.springframework.samples.petclinic.model.AdoptionApplication;
import org.springframework.samples.petclinic.model.AdoptionRequest;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.AdoptionApplicationRepository;
import org.springframework.samples.petclinic.repository.AdoptionRequestRepository;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class OwnerService {

	private final OwnerRepository ownerRepository;	
	private final AdoptionRequestRepository adoptionRequestRepository;
	private final AdoptionApplicationRepository adoptionApplicationRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PetService petService;
	
	@Autowired
	private AuthoritiesService authoritiesService;

	@Autowired
	public OwnerService(final OwnerRepository ownerRepository, 
		final AdoptionRequestRepository adoptionRequestRepository,final AdoptionApplicationRepository adoptionApplicationRepository
		) {
		this.ownerRepository = ownerRepository;
		this.adoptionRequestRepository = adoptionRequestRepository;
		this.adoptionApplicationRepository = adoptionApplicationRepository;
	}	

	@Transactional(readOnly = true)
	public Owner findOwnerById(final int id) throws DataAccessException {
		return this.ownerRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Collection<Owner> findOwnerByLastName(final String lastName) throws DataAccessException {
		return this.ownerRepository.findByLastName(lastName);
	}

	@Transactional
	public void saveOwner(final Owner owner) throws DataAccessException {
		//creating owner
		this.ownerRepository.save(owner);		
		//creating user
		this.userService.saveUser(owner.getUser());
		//creating authorities
		this.authoritiesService.saveAuthorities(owner.getUser().getUsername(), "owner");
	}
	
	public void delete(final Owner owner) {
		if(this.adoptionRequestRepository.findByOwner(owner)!= null) {
			AdoptionRequest request = this.adoptionRequestRepository.findByOwner(owner);
			
			for (final AdoptionApplication p : this.adoptionApplicationRepository.findByAdoptionRequest(request)) {
				this.adoptionApplicationRepository.delete(p);
			}
			this.adoptionRequestRepository.delete(request);
		}
		for (final Pet p : owner.getPets()) {
			this.petService.delete(p);
		}
		
		this.ownerRepository.delete(owner);
	}
	
	public Owner getPrincipal() {
		final String username = this.userService.getPrincipal();
		return this.ownerRepository.findByUserUsername(username);
	}

}