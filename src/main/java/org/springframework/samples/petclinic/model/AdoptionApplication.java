package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "adoption_application")
public class AdoptionApplication extends BaseEntity{
	
	@ManyToOne(optional=false)
	@JoinColumn(name = "adoption_request_id")
	private AdoptionRequest adoptionRequest;

}
