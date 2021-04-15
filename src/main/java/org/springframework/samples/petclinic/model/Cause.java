package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "causes")
public class Cause extends BaseEntity {
	
	@NotBlank
	@Column(name = "name")
	private String name;
	
	@NotBlank
	@Column(name = "description")
	private String description;
	
	@NotNull
	@Column(name = "budget_target")
	@Min(0)
	private Double budgetTarget;
	
	@NotBlank
	@Column(name = "organization")
	private String organization;
	
	@NotNull
	@Column(name = "is_closed")
	private Boolean isClosed;

	
	public String getName() {
		return this.name;
	}

	
	public void setName(final String name) {
		this.name = name;
	}

	
	public String getDescription() {
		return this.description;
	}

	
	public void setDescription(final String description) {
		this.description = description;
	}

	
	public Double getBudgetTarget() {
		return this.budgetTarget;
	}

	
	public void setBudgetTarget(final Double budgetTarget) {
		this.budgetTarget = budgetTarget;
	}

	
	public String getOrganization() {
		return this.organization;
	}

	
	public void setOrganization(final String organization) {
		this.organization = organization;
	}

	
	public Boolean getIsClosed() {
		return this.isClosed;
	}

	
	public void setIsClosed(final Boolean isClosed) {
		this.isClosed = isClosed;
	}

	

}
