package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.repository.DonationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DonationService {

private final DonationRepository donationRepository;
	
	@Autowired
	public DonationService(final DonationRepository donationRepository) {
		this.donationRepository = donationRepository;
	}
	
	
	@Transactional
	public void saveDonation(final Donation donation) throws DataAccessException {
		
		this.donationRepository.save(donation);
	}
	
	
	public Donation findDonationById(final int donationId) {
		return this.donationRepository.findDonationById(donationId);
	}
	
	@Transactional
	public Collection<Donation> findDonations() throws DataAccessException {
		return this.donationRepository.findAll();
		
	}
	
	public void delete(final Donation donation) {
		this.donationRepository.delete(donation);
	}
	
}
