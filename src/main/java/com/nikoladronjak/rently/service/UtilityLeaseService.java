package com.nikoladronjak.rently.service;

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

import com.nikoladronjak.rently.domain.EventSpace;
import com.nikoladronjak.rently.domain.OfficeSpace;
import com.nikoladronjak.rently.domain.Property;
import com.nikoladronjak.rently.domain.Rent;
import com.nikoladronjak.rently.domain.Utility;
import com.nikoladronjak.rently.domain.UtilityLease;
import com.nikoladronjak.rently.dto.UtilityLeaseDTO;
import com.nikoladronjak.rently.repository.EventSpaceRepository;
import com.nikoladronjak.rently.repository.OfficeSpaceRepository;
import com.nikoladronjak.rently.repository.RentRepository;
import com.nikoladronjak.rently.repository.UtilityLeaseRepository;
import com.nikoladronjak.rently.repository.UtilityRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * Represents a service class responsible for handling the business logic
 * related to UtilityLease entities. This class manages operations such as
 * retrieval, adding, modification and deletion of UtilityLease entities.
 * Additionally, it supports conversion between UtilityLease entities and
 * UtilityLeaseDTOs.
 * 
 * @author Nikola Dronjak
 */
@Service
public class UtilityLeaseService {

	/**
	 * Repository for accessing data related to utilities.
	 */
	@Autowired
	private UtilityRepository utilityRepository;

	/**
	 * Repository for accessing data related to event spaces.
	 */
	@Autowired
	private EventSpaceRepository eventSpaceRepository;

	/**
	 * Repository for accessing data related to office spaces.
	 */
	@Autowired
	private OfficeSpaceRepository officeSpaceRepository;

	/**
	 * Repository for accessing data related to rents.
	 */
	@Autowired
	private RentRepository rentRepository;

	/**
	 * Repository for accessing data related to utility leases.
	 */
	@Autowired
	private UtilityLeaseRepository utilityLeaseRepository;

	/**
	 * Validator for validating UtilityLease entities.
	 */
	private final Validator validator;

	/**
	 * Default constructor for UtilityLeaseService. Initializes the validator using
	 * a ValidatorFactory.
	 */
	public UtilityLeaseService() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		this.validator = factory.getValidator();
	}

	/**
	 * Retrieves all utility leases from the database and converts them to
	 * UtilityLeaseDTOs.
	 * 
	 * @return ResponseEntity containing a list of UtilityLeaseDTOs if successful,
	 *         or an error message with HttpStatus.INTERNAL_SERVER_ERROR status
	 *         (500) if an exception occurs.
	 */
	public ResponseEntity<?> getAll() {
		try {
			List<UtilityLease> utilityLeases = utilityLeaseRepository.findAll();
			List<UtilityLeaseDTO> utilityLeaseDTOs = utilityLeases.stream().map(this::convertToDTO)
					.collect(Collectors.toList());
			return ResponseEntity.ok(utilityLeaseDTOs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	/**
	 * Retrieves all utility leases associated with a specific utilityId from the
	 * database and converts them to UtilityLeaseDTOs.
	 * 
	 * @param utilityId The id of the utility for which the utility leases are being
	 *                  queried.
	 * @return ResponseEntity containing a list of UtilityLeaseDTOs if successful,
	 *         or an error message with HttpStatus.INTERNAL_SERVER_ERROR status
	 *         (500) if an exception occurs.
	 */
	public ResponseEntity<?> getAllByUtilityId(Integer utilityId) {
		try {
			List<UtilityLease> utilityLeases = utilityLeaseRepository.findAllByUtility_UtilityId(utilityId);
			List<UtilityLeaseDTO> utilityLeaseDTOs = utilityLeases.stream().map(this::convertToDTO)
					.collect(Collectors.toList());
			return ResponseEntity.ok(utilityLeaseDTOs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	/**
	 * Retrieves all utility leases associated with a specific propertyId from the
	 * database and converts them to UtilityLeaseDTOs.
	 * 
	 * @param propertyId The id of the property for which the utility leases are
	 *                   being queried.
	 * @return ResponseEntity containing a list of UtilityLeaseDTOs if successful,
	 *         or an error message with HttpStatus.INTERNAL_SERVER_ERROR status
	 *         (500) if an exception occurs.
	 */
	public ResponseEntity<?> getAllByPropertyId(Integer propertyId) {
		try {
			List<UtilityLease> utilityLeases = utilityLeaseRepository.findAllByProperty_PropertyId(propertyId);
			List<UtilityLeaseDTO> utilityLeaseDTOs = utilityLeases.stream().map(this::convertToDTO)
					.collect(Collectors.toList());
			return ResponseEntity.ok(utilityLeaseDTOs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	/**
	 * Retrieves a utility lease from the database by the specified id and converts
	 * it to a UtilityLeaseDTO.
	 * 
	 * @param id The id of the utility lease that is being queried.
	 * @return ResponseEntity containing the UtilityLeaseDTO if successful, or an
	 *         error message with HttpStatus.BAD_REQUEST status (400) if an
	 *         exception occurs.
	 * @throws RuntimeException if there is no utility lease with the given id.
	 */
	public ResponseEntity<?> getById(Integer id) {
		try {
			Optional<UtilityLease> utilityLeaseFromDb = utilityLeaseRepository.findById(id);
			if (!utilityLeaseFromDb.isPresent())
				throw new RuntimeException("There is no utility lease with the given id.");

			UtilityLeaseDTO utilityLeaseDTO = convertToDTO(utilityLeaseFromDb.get());
			return ResponseEntity.ok(utilityLeaseDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Adds a new utility lease to the database based on the provided
	 * UtilityLeaseDTO.
	 * 
	 * @param utilityLeaseDTO The UtilityLeaseDTO containing the details of the
	 *                        utility lease that is being added.
	 * @return ResponseEntity containing the newly created UtilityLeaseDTO if
	 *         successful, or an error message with HttpStatus.BAD_REQUEST status
	 *         (400) if the utilityLeaseDTO is not valid, or if an exception occurs.
	 * @throws RuntimeException if:
	 *                          <ul>
	 *                          <li>There is no utility for the given
	 *                          utilityId.</li>
	 *                          <li>The property is not an event space or a office
	 *                          space</li>
	 *                          <li>The utility lease with the provided utilityId
	 *                          and propertyId already exists.</li>
	 *                          </ul>
	 */
	public ResponseEntity<?> add(UtilityLeaseDTO utilityLeaseDTO) {
		try {
			Optional<Utility> utilityFromDb = utilityRepository.findById(utilityLeaseDTO.getUtilityId());
			if (!utilityFromDb.isPresent())
				throw new RuntimeException("There is no utility for the given utilityId.");

			Optional<EventSpace> eventSpaceFromDb = eventSpaceRepository.findById(utilityLeaseDTO.getPropertyId());
			Optional<OfficeSpace> officeSpaceFromDb = officeSpaceRepository.findById(utilityLeaseDTO.getPropertyId());
			if (!eventSpaceFromDb.isPresent() && officeSpaceFromDb.isEmpty()
					|| !officeSpaceFromDb.isPresent() && eventSpaceFromDb.isEmpty())
				throw new RuntimeException("The property has to be either an event space or an office space.");

			UtilityLease utilityLease = convertFromDTO(utilityLeaseDTO);
			Set<ConstraintViolation<UtilityLease>> violations = validator.validate(utilityLease);
			if (!violations.isEmpty()) {
				Map<String, String> errors = new HashMap<>();
				for (ConstraintViolation<UtilityLease> violation : violations) {
					errors.put(violation.getPropertyPath().toString(), violation.getMessage());
				}
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
			}

			Optional<UtilityLease> existingUtilityLease = utilityLeaseRepository
					.findByUtility_UtilityIdAndProperty_PropertyId(utilityLeaseDTO.getUtilityId(),
							utilityLeaseDTO.getPropertyId());
			if (existingUtilityLease.isPresent())
				throw new RuntimeException("This utility lease already exists.");

			UtilityLease newUtilityLease = utilityLeaseRepository.save(utilityLease);
			UtilityLeaseDTO newUtilityLeaseDTO = convertToDTO(newUtilityLease);
			return ResponseEntity.ok(newUtilityLeaseDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Updates the utility lease information based on the provided id and
	 * UtilityLeaseDTO.
	 * 
	 * @param id              The id of the utility lease that is being updated.
	 * @param utilityLeaseDTO The UtilityLeaseDTO containing the updated details of
	 *                        the utility lease.
	 * @return ResponseEntity containing the updated UtilityLeaseDTO if successful,
	 *         or an error message with HttpStatus.BAD_REQUEST status (400) if the
	 *         utilityLeaseDTO is not valid, or if an exception occurs.
	 * @throws RuntimeException if:
	 *                          <ul>
	 *                          <li>There is no utility lease for the given id.</li>
	 *                          <li>There is no utility for the given
	 *                          utilityId.</li>
	 *                          <li>The property is not an event space or a office
	 *                          space</li>
	 *                          <li>The utility lease with the provided utilityId
	 *                          and propertyId already exists.</li>
	 *                          </ul>
	 */
	public ResponseEntity<?> update(Integer id, UtilityLeaseDTO utilityLeaseDTO) {
		try {
			Optional<UtilityLease> utilityLeaseFromDb = utilityLeaseRepository.findById(id);
			if (!utilityLeaseFromDb.isPresent())
				throw new RuntimeException("There is no utility lease with the given id.");

			Optional<Utility> utilityFromDb = utilityRepository.findById(utilityLeaseDTO.getUtilityId());
			if (!utilityFromDb.isPresent())
				throw new RuntimeException("There is no utility for the given utilityId.");

			Optional<EventSpace> eventSpaceFromDb = eventSpaceRepository.findById(utilityLeaseDTO.getPropertyId());
			Optional<OfficeSpace> officeSpaceFromDb = officeSpaceRepository.findById(utilityLeaseDTO.getPropertyId());
			if (!eventSpaceFromDb.isPresent() && officeSpaceFromDb.isEmpty()
					|| !officeSpaceFromDb.isPresent() && eventSpaceFromDb.isEmpty())
				throw new RuntimeException("The property has to be either an event space or an office space.");

			UtilityLease utilityLease = convertFromDTO(utilityLeaseDTO);
			Set<ConstraintViolation<UtilityLease>> violations = validator.validate(utilityLease);
			if (!violations.isEmpty()) {
				Map<String, String> errors = new HashMap<>();
				for (ConstraintViolation<UtilityLease> violation : violations) {
					errors.put(violation.getPropertyPath().toString(), violation.getMessage());
				}
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
			}

			Optional<UtilityLease> existingUtilityLease = utilityLeaseRepository
					.findByUtility_UtilityIdAndProperty_PropertyId(utilityLeaseDTO.getUtilityId(),
							utilityLeaseDTO.getPropertyId());
			if (existingUtilityLease.isPresent()) {
				if (existingUtilityLease.get().getUtilityLeaseId() != id) {
					throw new RuntimeException("This utility lease already exists.");
				}
			}

			utilityLease.setUtilityLeaseId(id);
			UtilityLease updatedUtilityLease = utilityLeaseRepository.save(utilityLease);
			UtilityLeaseDTO updatedUtilityLeaseDTO = convertToDTO(updatedUtilityLease);
			return ResponseEntity.ok(updatedUtilityLeaseDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Deletes the utility lease with the specified id.
	 * 
	 * @param id The id of the utility lease that is being deleted.
	 * @return ResponseEntity containing the deleted UtilityLeaseDTO if successful,
	 *         or an error message with HttpStatus.BAD_REQUEST status (400) if an
	 *         exception occurs.
	 * @throws RuntimeException if there is no utility lease with the given id, or
	 *                          if there are rents associated with the utility
	 *                          lease.
	 */
	public ResponseEntity<?> delete(Integer id) {
		try {
			Optional<UtilityLease> utilityLeaseFromDb = utilityLeaseRepository.findById(id);
			if (!utilityLeaseFromDb.isPresent())
				throw new RuntimeException("There is no utility lease with the given id.");

			List<Rent> rentsFromDb = rentRepository.findAllByUtilityLeases_UtilityLeaseId(id);
			if (!rentsFromDb.isEmpty())
				throw new RuntimeException(
						"You cannot delete this utility lease since there are rents associated with it.");

			utilityLeaseRepository.deleteById(id);
			UtilityLeaseDTO deletedUtilityLeaseDTO = convertToDTO(utilityLeaseFromDb.get());
			return ResponseEntity.ok(deletedUtilityLeaseDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Converts a UtilityLease entity to a UtilityLeaseDTO.
	 * 
	 * @param utilityLease The UtilityLease entity that is being converted.
	 * @return The corresponding UtilityLeaseDTO.
	 */
	private UtilityLeaseDTO convertToDTO(UtilityLease utilityLease) {
		UtilityLeaseDTO utilityLeaseDTO = new UtilityLeaseDTO();
		utilityLeaseDTO.setUtilityId(utilityLease.getUtility().getUtilityId());
		utilityLeaseDTO.setPropertyId(utilityLease.getProperty().getPropertyId());
		utilityLeaseDTO.setRentalRate(utilityLease.getRentalRate());

		return utilityLeaseDTO;
	}

	/**
	 * Converts a UtilityLeaseDTO to a UtilityLease entity.
	 * 
	 * @param utilityLeaseDTO The UtilityLeaseDTO that is being converted.
	 * @return The corresponding UtilityLease entity.
	 */
	private UtilityLease convertFromDTO(UtilityLeaseDTO utilityLeaseDTO) {
		Utility utility = new Utility();
		utility.setUtilityId(utilityLeaseDTO.getUtilityId());

		Property property = new Property();
		property.setPropertyId(utilityLeaseDTO.getPropertyId());

		UtilityLease utilityLease = new UtilityLease();
		utilityLease.setUtility(utility);
		utilityLease.setProperty(property);
		utilityLease.setRentalRate(utilityLeaseDTO.getRentalRate());

		return utilityLease;
	}
}