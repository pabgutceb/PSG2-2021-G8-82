package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="donations")
public class Donation extends BaseEntity{

	@ManyToOne
	@JoinColumn(name = "cause_id")
	private Cause cause;
	
	@NotNull
	@Column(name = "amount")
	@Positive(message = "La cantidad a ingresar debe ser mayor a 0")
	private Double amount;
	
	@Column(name = "donation_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotNull
	private LocalDate donationDate;

	
	@ManyToOne
	@JoinColumn(name = "owner_id")
	private Owner owner;
	
	public Cause getCause() {
		return this.cause;
	}

	
	public void setCause(final Cause cause) {
		this.cause = cause;
	}

	
	public Double getAmount() {
		return this.amount;
	}

	
	public void setAmount(final Double amount) {
		this.amount = amount;
	}

	
	public LocalDate getDonationDate() {
		return this.donationDate;
	}

	
	public void setDonationDate(final LocalDate donationDate) {
		this.donationDate = donationDate;
	}

	public Owner getClient() {
		return this.owner;
	}
	
	public void setClient(final Owner owner) {
		this.owner = owner;
	}
	
}
