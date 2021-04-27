package org.springframework.samples.petclinic.model.forms;

import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;

public class AdoptionRequestForm {
	
	private Owner owner;
	private Pet pet;
	private Boolean confirmation;
	
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
	public Boolean getConfirmation() {
		return confirmation;
	}
	public void setConfirmation(Boolean confirmation) {
		this.confirmation = confirmation;
	}

}
