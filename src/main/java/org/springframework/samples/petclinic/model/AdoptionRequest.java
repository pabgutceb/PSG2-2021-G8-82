package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author carcabcol
 */

@Entity
@Table(name = "adoption_requests")
public class AdoptionRequest extends BaseEntity{
	
	//An owner can create MANY AdoptionRequests
	@ManyToOne
	@JoinColumn(name = "owner_id")
	private Owner owner;
	
	//Only ONE AdoptionRequest can be created per Pet
	@OneToOne(optional=false)
	@JoinColumn(name = "pet_id")
	private Pet pet;
	
	//When a an Application is approved, this attribute is updated
	//meanwhile is null
	@OneToOne(optional=true)
	@JoinColumn(name = "approved_application_id")
	private AdoptionApplication approvedApplication;

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

	public AdoptionApplication getApprovedApplication() {
		return approvedApplication;
	}

	public void setApprovedApplication(AdoptionApplication approvedApplication) {
		this.approvedApplication = approvedApplication;
	}
	
}
