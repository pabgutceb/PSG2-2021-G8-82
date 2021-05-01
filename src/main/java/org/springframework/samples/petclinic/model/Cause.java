package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

@Entity
@Table(name = "causes")
public class Cause extends BaseEntity {
	
	
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "cause")
	private Set<Donation> donations;
	
	@ManyToOne
	@JoinColumn(name = "owner_id")
	private Owner owner;
	
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
	
	@NotNull
	@Column(name = "total_budget")
	@Min(0)
	private Double totalBudget;
	
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

	
	public boolean getIsClosed() {
		return this.isClosed;
	}

	
	public void setIsClosed(final Boolean isClosed) {
		this.isClosed = isClosed;
	}


	
	public Owner getOwner() {
		return this.owner;
	}

	public void setOwner(final Owner owner) {
		this.owner = owner;
	}


	
	public Double getTotalBudget() {
		return this.totalBudget;
	}


	
	public void setTotalBudget(final Double totalBudget) {
		this.totalBudget = totalBudget;
	}
	
	protected Set<Donation> getDonationsInternal() {
		if (this.donations == null) {
			this.donations = new HashSet<>();
		}
		return this.donations;
	}

	 public List<Donation> getDonations() {
	        final List<Donation> sortedDonations = new ArrayList<>(this.getDonationsInternal());
	        PropertyComparator.sort(sortedDonations, new MutableSortDefinition("date", false, false));
	        return Collections.unmodifiableList(sortedDonations);
	    }
	
	public void addDonation(final Donation donation) {
		this.getDonationsInternal().add(donation);
		donation.setCause(this);
	}
	
	public boolean removeDonation(final Donation donation) {
		return this.getDonationsInternal().remove(donation);
	}

	
	}
	
	
	
	

	


