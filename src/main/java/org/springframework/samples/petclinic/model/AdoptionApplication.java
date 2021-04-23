package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "adoption_application")
public class AdoptionApplication extends BaseEntity{
	
	@ManyToOne(optional=false)
	@JoinColumn(name = "adoption_request_id")
	private AdoptionRequest adoptionRequest;
	
	@ManyToOne
	@JoinColumn(name = "owner_id")
	private Owner owner;
	
	@NotEmpty
	private String comment;

	public AdoptionRequest getAdoptionRequest() {
		return adoptionRequest;
	}

	public void setAdoptionRequest(AdoptionRequest adoptionRequest) {
		this.adoptionRequest = adoptionRequest;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	

}
