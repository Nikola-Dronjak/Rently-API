package com.nikoladronjak.rently.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * Represents a service class responsible for handling the business logic
 * related to Rent entities. This class manages operations such as retrieval,
 * adding, modification and deletion of Rent entities. Additionally, it supports
 * conversion between Rent entities and RentDTOs.
 * 
 * @author Nikola Dronjak
 */
@Service
public class RentService {

	/**
	 * Repository for accessing data related to residences.
	 */
	@Autowired
	private ResidenceRepository residenceRepository;

	/**
	 * Repository for accessing data related to leases.
	 */
	@Autowired
	private LeaseRepository leaseRepository;

	/**
	 * Repository for accessing data related to utility leases.
	 */
	@Autowired
	private UtilityLeaseRepository utilityLeaseRepository;

	/**
	 * Repository for accessing data related to rents.
	 */
	@Autowired
	private RentRepository rentRepository;

	/**
	 * Validator for validating Rent entities.
	 */
	private final Validator validator;

	/**
	 * Default constructor for RentService. Initializes the validator using a
	 * ValidatorFactory.
	 */
	public RentService() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		this.validator = factory.getValidator();
	}

	/**
	 * Retrieves all rents from the database and converts them to RentDTOs.
	 * 
	 * @return ResponseEntity containing a list of RentDTOs if successful, or an
	 *         error message with HttpStatus.INTERNAL_SERVER_ERROR status (500) if
	 *         an exception occurs.
	 */
	public ResponseEntity<?> getAll() {
		try {
			List<Rent> rents = rentRepository.findAll();
			List<RentDTO> rentDTOs = rents.stream().map(this::convertToDTO).collect(Collectors.toList());
			return ResponseEntity.ok(rentDTOs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	/**
	 * Retrieves a rent from the database by the specified id and converts it to a
	 * RentDTO.
	 * 
	 * @param id The id of the rent that is being queried.
	 * @return ResponseEntity containing the RentDTO if successful, or an error
	 *         message with HttpStatus.BAD_REQUEST status (400) if an exception
	 *         occurs.
	 * @throws RuntimeException if there is no rent with the given id.
	 */
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

	/**
	 * Adds a new rent to the database based on the provided RentDTO.
	 * 
	 * @param rentDTO The RentDTO containing the details of the rent that is being
	 *                added.
	 * @return ResponseEntity containing the newly created RentDTO if successful, or
	 *         an error message with HttpStatus.BAD_REQUEST status (400) if the
	 *         rentDTO is not valid, or if an exception occurs.
	 * @throws RuntimeException if:
	 *                          <ul>
	 *                          <li>There is no lease for the given leaseId.</li>
	 *                          <li>There is no utility lease for the given
	 *                          utilityLeaseId.</li>
	 *                          <li>The Rent with the provided leaseId already
	 *                          exists.</li>
	 *                          </ul>
	 */
	public ResponseEntity<?> add(RentDTO rentDTO) {
		try {
			Optional<Lease> leaseFromDb = leaseRepository.findById(rentDTO.getLeaseId());
			if (!leaseFromDb.isPresent())
				throw new RuntimeException("There is no lease for the given leaseId.");

			if (residenceRepository.findById(leaseFromDb.get().getProperty().getPropertyId()).isPresent()) {
				double leaseRentalRate = leaseFromDb.get().getRentalRate();
				Rent rent = convertFromDTO(rentDTO);
				Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
				if (!violations.isEmpty()) {
					Map<String, String> errors = new HashMap<>();
					for (ConstraintViolation<Rent> violation : violations) {
						errors.put(violation.getPropertyPath().toString(), violation.getMessage());
					}
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
				}
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

			Rent rent = convertFromDTO(rentDTO);
			Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
			if (!violations.isEmpty()) {
				Map<String, String> errors = new HashMap<>();
				for (ConstraintViolation<Rent> violation : violations) {
					errors.put(violation.getPropertyPath().toString(), violation.getMessage());
				}
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
			}

			Optional<Rent> existingRent = rentRepository.findByLease_LeaseId(rentDTO.getLeaseId());
			if (existingRent.isPresent())
				throw new RuntimeException("This rent already exists.");

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

	/**
	 * Updates the rent information based on the provided id and RentDTO.
	 * 
	 * @param id      The id of the rent that is being updated.
	 * @param rentDTO The RentDTO containing the updated details of the rent.
	 * @return ResponseEntity containing the updated RentDTO if successful, or an
	 *         error message with HttpStatus.BAD_REQUEST status (400) if the rentDTO
	 *         is not valid, or if an exception occurs.
	 * @throws RuntimeException if:
	 *                          <ul>
	 *                          <li>There is no rent for the given id.</li>
	 *                          <li>There is no lease for the given leaseId.</li>
	 *                          <li>There is no utility lease for the given
	 *                          utilityLeaseId.</li>
	 *                          <li>The Rent with the provided leaseId already
	 *                          exists.</li>
	 *                          </ul>
	 */
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

			Rent rent = convertFromDTO(rentDTO);
			Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
			if (!violations.isEmpty()) {
				Map<String, String> errors = new HashMap<>();
				for (ConstraintViolation<Rent> violation : violations) {
					errors.put(violation.getPropertyPath().toString(), violation.getMessage());
				}
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
			}

			Optional<Rent> existingRent = rentRepository.findByLease_LeaseId(rentDTO.getLeaseId());
			if (existingRent.isPresent()) {
				if (existingRent.get().getRentId() != id) {
					throw new RuntimeException("This rent already exists.");
				}
			}

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

	/**
	 * Deletes the rent with the specified id.
	 * 
	 * @param id The id of the rent that is being deleted.
	 * @return ResponseEntity containing the deleted RentDTO if successful, or an
	 *         error message with HttpStatus.BAD_REQUEST status (400) if an
	 *         exception occurs.
	 * @throws RuntimeException if there is no rent with the given id.
	 */
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

	/**
	 * Converts a Rent entity to a RentDTO.
	 * 
	 * @param rent The Rent entity that is being converted.
	 * @return The corresponding RentDTO.
	 */
	private RentDTO convertToDTO(Rent rent) {
		List<Integer> utilityLeaseIds = rent.getUtilityLeases().stream()
				.map(utilityLease -> utilityLease.getUtilityLeaseId()).collect(Collectors.toList());

		RentDTO rentDTO = new RentDTO();
		rentDTO.setLeaseId(rent.getLease().getLeaseId());
		rentDTO.setUtilityLeaseIds(utilityLeaseIds);
		rentDTO.setTotalRent(rent.getTotalRent());

		return rentDTO;
	}

	/**
	 * Converts a RentDTO to a Rent entity.
	 * 
	 * @param rentDTO The RentDTO that is being converted.
	 * @return The corresponding Rent entity.
	 */
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
