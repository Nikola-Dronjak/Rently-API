package com.nikoladronjak.rently.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nikoladronjak.rently.domain.Lease;
import com.nikoladronjak.rently.domain.Rent;
import com.nikoladronjak.rently.domain.UtilityLease;
import com.nikoladronjak.rently.dto.RentDTO;
import com.nikoladronjak.rently.repository.LeaseRepository;
import com.nikoladronjak.rently.repository.RentRepository;
import com.nikoladronjak.rently.repository.ResidenceRepository;
import com.nikoladronjak.rently.repository.UtilityLeaseRepository;

@Service
public class RentService {

	@Autowired
	private ResidenceRepository residenceRepository;

	@Autowired
	private LeaseRepository leaseRepository;

	@Autowired
	private UtilityLeaseRepository utilityLeaseRepository;

	@Autowired
	private RentRepository rentRepository;

	public ResponseEntity<?> getAll() {
		try {
			List<Rent> rents = rentRepository.findAll();
			List<RentDTO> rentDTOs = rents.stream().map(this::convertToDTO).collect(Collectors.toList());
			return ResponseEntity.ok(rentDTOs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	public ResponseEntity<?> getById(Integer id) {
		try {
			Optional<Rent> rentFromDb = rentRepository.findById(id);
			if (!rentFromDb.isPresent())
				throw new RuntimeException("There is no rent with the given id.");

			RentDTO rentDTO = convertToDTO(rentFromDb.get());
			return ResponseEntity.ok(rentDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	public ResponseEntity<?> add(RentDTO rentDTO) {
		try {
			Optional<Lease> leaseFromDb = leaseRepository.findById(rentDTO.getLeaseId());
			if (!leaseFromDb.isPresent())
				throw new RuntimeException("There is no lease for the given leaseId.");

			if (residenceRepository.findById(leaseFromDb.get().getProperty().getPropertyId()).isPresent()) {
				double leaseRentalRate = leaseFromDb.get().getRentalRate();
				Rent rent = convertFromDTO(rentDTO);
				rent.setTotalRent(leaseRentalRate);
				rent.setUtilityLeases(new ArrayList<UtilityLease>());
				Rent newRent = rentRepository.save(rent);
				RentDTO newRentDTO = convertToDTO(newRent);
				return ResponseEntity.ok(newRentDTO);
			}

			double leaseRentalRate = leaseFromDb.get().getRentalRate();
			double sumOfUtilityLeaseRentalRates = 0;
			for (Integer utilityLeaseId : rentDTO.getUtilityLeaseIds()) {
				Optional<UtilityLease> utilityLeaseFromDb = utilityLeaseRepository.findById(utilityLeaseId);
				if (!utilityLeaseFromDb.isPresent())
					throw new RuntimeException("There is no utility lease for the utilityLeaseId: " + utilityLeaseId);
				sumOfUtilityLeaseRentalRates += utilityLeaseFromDb.get().getRentalRate();
			}

			Optional<Rent> existingRent = rentRepository.findByLeaseLeaseId(rentDTO.getLeaseId());
			if (existingRent.isPresent())
				throw new RuntimeException("This rent already exists.");

			Rent rent = convertFromDTO(rentDTO);
			rent.setTotalRent(sumOfUtilityLeaseRentalRates + leaseRentalRate);
			Rent newRent = rentRepository.save(rent);
			for (Integer utilityLeaseId : rentDTO.getUtilityLeaseIds()) {
				Optional<UtilityLease> utilityLeaseFromDb = utilityLeaseRepository.findById(utilityLeaseId);
				UtilityLease utilityLease = utilityLeaseFromDb.get();
				utilityLease.getRents().add(newRent);
				utilityLeaseRepository.save(utilityLease);
			}
			RentDTO newRentDTO = convertToDTO(newRent);
			return ResponseEntity.ok(newRentDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	public ResponseEntity<?> update(Integer id, RentDTO rentDTO) {
		try {
			Optional<Rent> rentFromDb = rentRepository.findById(id);
			if (!rentFromDb.isPresent())
				throw new RuntimeException("There is no rent for the given id.");

			Optional<Lease> leaseFromDb = leaseRepository.findById(rentDTO.getLeaseId());
			if (!leaseFromDb.isPresent())
				throw new RuntimeException("There is no lease for the given leaseId.");

			double leaseRentalRate = leaseFromDb.get().getRentalRate();
			double sumOfUtilityLeaseRentalRates = 0;
			for (Integer utilityLeaseId : rentDTO.getUtilityLeaseIds()) {
				Optional<UtilityLease> utilityLeaseFromDb = utilityLeaseRepository.findById(utilityLeaseId);
				if (!utilityLeaseFromDb.isPresent())
					throw new RuntimeException("There is no utility lease for the utilityLeaseId: " + utilityLeaseId);
				sumOfUtilityLeaseRentalRates += utilityLeaseFromDb.get().getRentalRate();
			}

			Optional<Rent> existingRent = rentRepository.findByLeaseLeaseId(rentDTO.getLeaseId());
			if (existingRent.isPresent()) {
				if (existingRent.get().getRentId() != id) {
					throw new RuntimeException("This rent already exists.");
				}
			}

			Rent rent = convertFromDTO(rentDTO);
			rent.setRentId(id);
			rent.setTotalRent(sumOfUtilityLeaseRentalRates + leaseRentalRate);
			for (UtilityLease utilityLease : rentFromDb.get().getUtilityLeases()) {
				utilityLease.getRents().remove(rentFromDb.get());
				utilityLeaseRepository.save(utilityLease);
			}
			Rent updatedRent = rentRepository.save(rent);
			for (Integer utilityLeaseId : rentDTO.getUtilityLeaseIds()) {
				Optional<UtilityLease> utilityLeaseFromDb = utilityLeaseRepository.findById(utilityLeaseId);
				UtilityLease utilityLease = utilityLeaseFromDb.get();
				utilityLease.getRents().add(updatedRent);
				utilityLeaseRepository.save(utilityLease);
			}
			RentDTO updatedRentDTO = convertToDTO(updatedRent);
			return ResponseEntity.ok(updatedRentDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	public ResponseEntity<?> delete(Integer id) {
		try {
			Optional<Rent> rentFromDb = rentRepository.findById(id);
			if (!rentFromDb.isPresent())
				throw new RuntimeException("There is no rent with the given id.");

			for (UtilityLease utilityLease : rentFromDb.get().getUtilityLeases()) {
				utilityLease.getRents().remove(rentFromDb.get());
				utilityLeaseRepository.save(utilityLease);
			}
			rentRepository.deleteById(id);
			RentDTO deletedRentDTO = convertToDTO(rentFromDb.get());
			return ResponseEntity.ok(deletedRentDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	private RentDTO convertToDTO(Rent rent) {
		List<Integer> utilityLeaseIds = rent.getUtilityLeases().stream()
				.map(utilityLease -> utilityLease.getUtilityLeaseId()).collect(Collectors.toList());

		RentDTO rentDTO = new RentDTO();
		rentDTO.setLeaseId(rent.getLease().getLeaseId());
		rentDTO.setUtilityLeaseIds(utilityLeaseIds);
		rentDTO.setTotalRent(rent.getTotalRent());

		return rentDTO;
	}

	private Rent convertFromDTO(RentDTO rentDTO) {
		Lease lease = new Lease();
		lease.setLeaseId(rentDTO.getLeaseId());

		List<UtilityLease> utilityLeases = rentDTO.getUtilityLeaseIds().stream().map(utilityLeaseRepository::findById)
				.filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());

		Rent rent = new Rent();
		rent.setLease(lease);
		rent.setUtilityLeases(utilityLeases);

		return rent;
	}
}
