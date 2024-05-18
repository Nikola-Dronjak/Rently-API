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

import com.nikoladronjak.rently.domain.Lease;
import com.nikoladronjak.rently.domain.OfficeSpace;
import com.nikoladronjak.rently.domain.UtilityLease;
import com.nikoladronjak.rently.dto.OfficeSpaceDTO;
import com.nikoladronjak.rently.repository.EventSpaceRepository;
import com.nikoladronjak.rently.repository.LeaseRepository;
import com.nikoladronjak.rently.repository.OfficeSpaceRepository;
import com.nikoladronjak.rently.repository.OwnerRepository;
import com.nikoladronjak.rently.repository.ResidenceRepository;
import com.nikoladronjak.rently.repository.UtilityLeaseRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * Represents a service class responsible for handling the business logic
 * related to OfficeSpace entities. This class manages operations such as
 * retrieval, adding, modification and deletion of OfficeSpace entities.
 * Additionally, it supports conversion between OfficeSpace entities and
 * OfficeSpaceDTOs.
 * 
 * @author Nikola Dronjak
 */
@Service
public class OfficeSpaceService {

	/**
	 * Repository for accessing data related to owners.
	 */
	@Autowired
	private OwnerRepository ownerRepository;

	/**
	 * Repository for accessing data related to residences.
	 */
	@Autowired
	private ResidenceRepository residenceRepository;

	/**
	 * Repository for accessing data related to event spaces.
	 */
	@Autowired
	private EventSpaceRepository eventSpaceRepository;

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
	 * Repository for accessing data related to office spaces.
	 */
	@Autowired
	private OfficeSpaceRepository officeSpaceRepository;

	/**
	 * Validator for validating OfficeSpace entities.
	 */
	private final Validator validator;

	/**
	 * Default constructor for OfficeSpaceService. Initializes the validator using a
	 * ValidatorFactory.
	 */
	public OfficeSpaceService() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		this.validator = factory.getValidator();
	}

	/**
	 * Retrieves all office spaces from the database and converts them to
	 * OfficeSpaceDTOs.
	 * 
	 * @return ResponseEntity containing a list of OfficeSpaceDTOs if successful, or
	 *         an error message with HttpStatus.INTERNAL_SERVER_ERROR status (500)
	 *         if an exception occurs.
	 */
	public ResponseEntity<?> getAll() {
		try {
			List<OfficeSpace> officeSpaces = officeSpaceRepository.findAll();
			List<OfficeSpaceDTO> officeSpaceDTOs = officeSpaces.stream().map(this::convertToDTO)
					.collect(Collectors.toList());
			return ResponseEntity.ok(officeSpaceDTOs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	/**
	 * Retrieves a office space from the database by the specified id and converts
	 * it to an OfficeSpaceDTO.
	 * 
	 * @param id The id of the office space that is being queried.
	 * @return ResponseEntity containing the OfficeSpaceDTO if successful, or an
	 *         error message with HttpStatus.BAD_REQUEST status (400) if an
	 *         exception occurs.
	 * @throws RuntimeException if there is no office space with the given id.
	 */
	public ResponseEntity<?> getById(Integer id) {
		try {
			Optional<OfficeSpace> officeSpaceFromDb = officeSpaceRepository.findById(id);
			if (!officeSpaceFromDb.isPresent())
				throw new RuntimeException("There is no office space with the given id.");

			OfficeSpaceDTO officeSpaceDTO = convertToDTO(officeSpaceFromDb.get());
			return ResponseEntity.ok(officeSpaceDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Adds a new office space to the database based on the provided OfficeSpaceDTO.
	 * 
	 * @param officeSpaceDTO The OfficeSpaceDTO containing the details of the office
	 *                       space that is being added.
	 * @return ResponseEntity containing the newly created OfficeSpaceDTO if
	 *         successful, or an error message with HttpStatus.BAD_REQUEST status
	 *         (400) if the officeSpaceDTO is not valid, or if an exception occurs.
	 * @throws RuntimeException if there is no owner with the given ownerId, or if a
	 *                          property with the provided address already exists.
	 */
	public ResponseEntity<?> add(OfficeSpaceDTO officeSpaceDTO) {
		try {
			if (ownerRepository.findById(officeSpaceDTO.getOwnerId()).isEmpty())
				throw new RuntimeException("There is no owner with the given id.");

			OfficeSpace officeSpace = convertFromDTO(officeSpaceDTO);
			Set<ConstraintViolation<OfficeSpace>> violations = validator.validate(officeSpace);
			if (!violations.isEmpty()) {
				Map<String, String> errors = new HashMap<>();
				for (ConstraintViolation<OfficeSpace> violation : violations) {
					errors.put(violation.getPropertyPath().toString(), violation.getMessage());
				}
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
			}

			if (residenceRepository.findByAddress(officeSpaceDTO.getAddress()).isPresent()
					|| eventSpaceRepository.findByAddress(officeSpaceDTO.getAddress()).isPresent()
					|| officeSpaceRepository.findByAddress(officeSpaceDTO.getAddress()).isPresent())
				throw new RuntimeException("This property already exists.");

			OfficeSpace newOfficeSpace = officeSpaceRepository.save(officeSpace);
			OfficeSpaceDTO newOfficeSpaceDTO = convertToDTO(newOfficeSpace);
			return ResponseEntity.ok(newOfficeSpaceDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Updates the office space information based on the provided id and
	 * OfficeSpaceDTO.
	 * 
	 * @param id             The id of the office space that is being updated.
	 * @param officeSpaceDTO The OfficeSpaceDTO containing the updated details of
	 *                       the office space.
	 * @return ResponseEntity containing the updated OfficeSpaceDTO if successful,
	 *         or an error message with HttpStatus.BAD_REQUEST status (400) if the
	 *         officeSpaceDTO is not valid, or if an exception occurs.
	 * @throws RuntimeException if:
	 *                          <ul>
	 *                          <li>There is no office space with the given id.</li>
	 *                          <li>There is no owner with the given ownerId</li>
	 *                          <li>The property with the provided address already
	 *                          exists.</li>
	 *                          </ul>
	 */
	public ResponseEntity<?> update(Integer id, OfficeSpaceDTO officeSpaceDTO) {
		try {
			Optional<OfficeSpace> officeSpaceFromDb = officeSpaceRepository.findById(id);
			if (!officeSpaceFromDb.isPresent())
				throw new RuntimeException("There is no office space with the given id.");

			if (ownerRepository.findById(officeSpaceDTO.getOwnerId()).isEmpty())
				throw new RuntimeException("There is no owner with the given id.");

			OfficeSpace officeSpace = convertFromDTO(officeSpaceDTO);
			Set<ConstraintViolation<OfficeSpace>> violations = validator.validate(officeSpace);
			if (!violations.isEmpty()) {
				Map<String, String> errors = new HashMap<>();
				for (ConstraintViolation<OfficeSpace> violation : violations) {
					errors.put(violation.getPropertyPath().toString(), violation.getMessage());
				}
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
			}

			if (!officeSpaceFromDb.get().getAddress().equals(officeSpaceDTO.getAddress())) {
				if (residenceRepository.findByAddress(officeSpaceDTO.getAddress()).isPresent()
						|| eventSpaceRepository.findByAddress(officeSpaceDTO.getAddress()).isPresent()
						|| officeSpaceRepository.findByAddress(officeSpaceDTO.getAddress()).isPresent()) {
					throw new RuntimeException("This property already exists.");
				}
			}

			officeSpace.setPropertyId(id);
			OfficeSpace updatedOfficeSpace = officeSpaceRepository.save(officeSpace);
			OfficeSpaceDTO updatedOfficeSpaceDTO = convertToDTO(updatedOfficeSpace);
			return ResponseEntity.ok(updatedOfficeSpaceDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Deletes the office space with the specified id.
	 * 
	 * @param id The id of the office space that is being deleted.
	 * @return ResponseEntity containing the deleted OfficeSpaceDTO if successful,
	 *         or an error message with HttpStatus.BAD_REQUEST status (400) if an
	 *         exception occurs.
	 * @throws RuntimeException if:
	 *                          <ul>
	 *                          <li>There is no office space with the given id.</li>
	 *                          <li>There are leases associated with the office
	 *                          space.</li>
	 *                          <li>There are utility leases associated with the
	 *                          office space.</li>
	 *                          </ul>
	 */
	public ResponseEntity<?> delete(Integer id) {
		try {
			Optional<OfficeSpace> officeSpaceFromDb = officeSpaceRepository.findById(id);
			if (!officeSpaceFromDb.isPresent())
				throw new RuntimeException("There is no office space with the given id.");

			List<Lease> leasesFromDb = leaseRepository.findAllByProperty_PropertyId(id);
			if (!leasesFromDb.isEmpty())
				throw new RuntimeException(
						"You cannot delete this office space since there are leases associated with it.");

			List<UtilityLease> utilityLeasesFromDb = utilityLeaseRepository.findAllByProperty_PropertyId(id);
			if (!utilityLeasesFromDb.isEmpty())
				throw new RuntimeException(
						"You cannot delete this office space since there are utility leases associated with it.");

			officeSpaceRepository.deleteById(id);
			OfficeSpaceDTO deletedOfficeSpaceDTO = convertToDTO(officeSpaceFromDb.get());
			return ResponseEntity.ok(deletedOfficeSpaceDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Converts a OfficeSpace entity to a OfficeSpaceDTO.
	 * 
	 * @param officeSpace The OfficeSpace entity that is being converted.
	 * @return The corresponding OfficeSpaceDTO.
	 */
	private OfficeSpaceDTO convertToDTO(OfficeSpace officeSpace) {
		OfficeSpaceDTO officeSpaceDTO = new OfficeSpaceDTO();
		officeSpaceDTO.setPropertyId(officeSpace.getPropertyId());
		officeSpaceDTO.setName(officeSpace.getName());
		officeSpaceDTO.setAddress(officeSpace.getAddress());
		officeSpaceDTO.setDescription(officeSpace.getDescription());
		officeSpaceDTO.setRentalRate(officeSpace.getRentalRate());
		officeSpaceDTO.setSize(officeSpace.getSize());
		officeSpaceDTO.setIsAvailable(officeSpace.isAvailable());
		officeSpaceDTO.setNumberOfParkingSpots(officeSpace.getNumberOfParkingSpots());
		officeSpaceDTO.setPhotos(officeSpace.getPhotos());
		officeSpaceDTO.setCapacity(officeSpace.getCapacity());
		officeSpaceDTO.setOwnerId(officeSpace.getOwner().getOwnerId());

		return officeSpaceDTO;
	}

	/**
	 * Converts a OfficeSpaceDTO to a OfficeSpace entity.
	 * 
	 * @param officeSpaceDTO The OfficeSpaceDTO that is being converted.
	 * @return The corresponding OfficeSpace entity.
	 */
	private OfficeSpace convertFromDTO(OfficeSpaceDTO officeSpaceDTO) {
		OfficeSpace officeSpace = new OfficeSpace();
		if (officeSpaceDTO.getPropertyId() != null) {
			officeSpace.setPropertyId(officeSpaceDTO.getPropertyId());
		}
		officeSpace.setName(officeSpaceDTO.getName());
		officeSpace.setAddress(officeSpaceDTO.getAddress());
		officeSpace.setDescription(officeSpaceDTO.getDescription());
		officeSpace.setRentalRate(officeSpaceDTO.getRentalRate());
		officeSpace.setSize(officeSpaceDTO.getSize());
		officeSpace.setAvailable(officeSpaceDTO.getIsAvailable());
		officeSpace.setNumberOfParkingSpots(officeSpaceDTO.getNumberOfParkingSpots());
		officeSpace.setPhotos(officeSpaceDTO.getPhotos());
		officeSpace.setCapacity(officeSpaceDTO.getCapacity());
		if (officeSpaceDTO.getOwnerId() != null) {
			officeSpace.setOwner(ownerRepository.findById(officeSpaceDTO.getOwnerId()).get());
		}

		return officeSpace;
	}
}
