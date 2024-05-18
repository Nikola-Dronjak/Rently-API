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
import com.nikoladronjak.rently.domain.Residence;
import com.nikoladronjak.rently.dto.ResidenceDTO;
import com.nikoladronjak.rently.repository.EventSpaceRepository;
import com.nikoladronjak.rently.repository.LeaseRepository;
import com.nikoladronjak.rently.repository.OfficeSpaceRepository;
import com.nikoladronjak.rently.repository.OwnerRepository;
import com.nikoladronjak.rently.repository.ResidenceRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * Represents a service class responsible for handling the business logic
 * related to Residence entities. This class manages operations such as
 * retrieval, adding, modification and deletion of Residence entities.
 * Additionally, it supports conversion between Residence entities and
 * ResidenceDTOs.
 * 
 * @author Nikola Dronjak
 */
@Service
public class ResidenceService {

	/**
	 * Repository for accessing data related to owners.
	 */
	@Autowired
	private OwnerRepository ownerRepository;

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
	 * Repository for accessing data related to leases.
	 */
	@Autowired
	private LeaseRepository leaseRepository;

	/**
	 * Repository for accessing data related to residences.
	 */
	@Autowired
	private ResidenceRepository residenceRepository;

	/**
	 * Validator for validating Residence entities.
	 */
	private final Validator validator;

	/**
	 * Default constructor for ResidenceService. Initializes the validator using a
	 * ValidatorFactory.
	 */
	public ResidenceService() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		this.validator = factory.getValidator();
	}

	/**
	 * Retrieves all residences from the database and converts them to
	 * ResidenceDTOs.
	 * 
	 * @return ResponseEntity containing a list of ResidenceDTOs if successful, or
	 *         an error message with HttpStatus.INTERNAL_SERVER_ERROR status (500)
	 *         if an exception occurs.
	 */
	public ResponseEntity<?> getAll() {
		try {
			List<Residence> residences = residenceRepository.findAll();
			List<ResidenceDTO> residenceDTOs = residences.stream().map(this::convertToDTO).collect(Collectors.toList());
			return ResponseEntity.ok(residenceDTOs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	/**
	 * Retrieves a residence from the database by the specified id and converts it
	 * to a ResidenceDTO.
	 * 
	 * @param id The id of the residence that is being queried.
	 * @return ResponseEntity containing the ResidenceDTO if successful, or an error
	 *         message with HttpStatus.BAD_REQUEST status (400) if an exception
	 *         occurs.
	 * @throws RuntimeException if there is no residence with the given id.
	 */
	public ResponseEntity<?> getById(Integer id) {
		try {
			Optional<Residence> residenceFromDb = residenceRepository.findById(id);
			if (!residenceFromDb.isPresent())
				throw new RuntimeException("There is no residence with the given id.");

			ResidenceDTO residenceDTO = convertToDTO(residenceFromDb.get());
			return ResponseEntity.ok(residenceDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Adds a new residence to the database based on the provided ResidenceDTO.
	 * 
	 * @param residenceDTO The ResidenceDTO containing the details of the residence
	 *                     that is being added.
	 * @return ResponseEntity containing the newly created ResidenceDTO if
	 *         successful, or an error message with HttpStatus.BAD_REQUEST status
	 *         (400) if the residenceDTO is not valid, or if an exception occurs.
	 * @throws RuntimeException if there is no owner with the given ownerId, or if a
	 *                          property with the provided address already exists.
	 */
	public ResponseEntity<?> add(ResidenceDTO residenceDTO) {
		try {
			if (ownerRepository.findById(residenceDTO.getOwnerId()).isEmpty())
				throw new RuntimeException("There is no owner with the given id.");

			Residence residence = convertFromDTO(residenceDTO);
			Set<ConstraintViolation<Residence>> violations = validator.validate(residence);
			if (!violations.isEmpty()) {
				Map<String, String> errors = new HashMap<>();
				for (ConstraintViolation<Residence> violation : violations) {
					errors.put(violation.getPropertyPath().toString(), violation.getMessage());
				}
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
			}

			if (residenceRepository.findByAddress(residenceDTO.getAddress()).isPresent()
					|| eventSpaceRepository.findByAddress(residenceDTO.getAddress()).isPresent()
					|| officeSpaceRepository.findByAddress(residenceDTO.getAddress()).isPresent())
				throw new RuntimeException("This property already exists.");

			Residence newResidence = residenceRepository.save(residence);
			ResidenceDTO newResidenceDTO = convertToDTO(newResidence);
			return ResponseEntity.ok(newResidenceDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Updates the residence information based on the provided id and ResidenceDTO.
	 * 
	 * @param id           The id of the residence that is being updated.
	 * @param residenceDTO The ResidenceDTO containing the updated details of the
	 *                     residence.
	 * @return ResponseEntity containing the updated ResidenceDTO if successful, or
	 *         an error message with HttpStatus.BAD_REQUEST status (400) if the
	 *         residenceDTO is not valid, or if an exception occurs.
	 * @throws RuntimeException if:
	 *                          <ul>
	 *                          <li>There is no residence with the given id.</li>
	 *                          <li>There is no owner with the given ownerId</li>
	 *                          <li>The property with the provided address already
	 *                          exists.</li>
	 *                          </ul>
	 */
	public ResponseEntity<?> update(Integer id, ResidenceDTO residenceDTO) {
		try {
			Optional<Residence> residenceFromDb = residenceRepository.findById(id);
			if (!residenceFromDb.isPresent())
				throw new RuntimeException("There is no residence with the given id.");

			if (ownerRepository.findById(residenceDTO.getOwnerId()).isEmpty())
				throw new RuntimeException("There is no owner with the given id.");

			Residence residence = convertFromDTO(residenceDTO);
			Set<ConstraintViolation<Residence>> violations = validator.validate(residence);
			if (!violations.isEmpty()) {
				Map<String, String> errors = new HashMap<>();
				for (ConstraintViolation<Residence> violation : violations) {
					errors.put(violation.getPropertyPath().toString(), violation.getMessage());
				}
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
			}

			if (!residenceFromDb.get().getAddress().equals(residenceDTO.getAddress())) {
				if (residenceRepository.findByAddress(residenceDTO.getAddress()).isPresent()
						|| eventSpaceRepository.findByAddress(residenceDTO.getAddress()).isPresent()
						|| officeSpaceRepository.findByAddress(residenceDTO.getAddress()).isPresent()) {
					throw new RuntimeException("This property already exists.");
				}
			}

			residence.setPropertyId(id);
			Residence updatedResidence = residenceRepository.save(residence);
			ResidenceDTO updatedResidenceDTO = convertToDTO(updatedResidence);
			return ResponseEntity.ok(updatedResidenceDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Deletes the residence with the specified id.
	 * 
	 * @param id The id of the residence that is being deleted.
	 * @return ResponseEntity containing the deleted ResidenceDTO if successful, or
	 *         an error message with HttpStatus.BAD_REQUEST status (400) if an
	 *         exception occurs.
	 * @throws RuntimeException if there is no residence with the given id, or if
	 *                          there are leases associated with the residence.
	 */
	public ResponseEntity<?> delete(Integer id) {
		try {
			Optional<Residence> residenceFromDb = residenceRepository.findById(id);
			if (!residenceFromDb.isPresent())
				throw new RuntimeException("There is no residence with the given id.");

			List<Lease> leasesFromDb = leaseRepository.findAllByProperty_PropertyId(id);
			if (!leasesFromDb.isEmpty())
				throw new RuntimeException(
						"You cannot delete this residence since there are leases associated with it.");

			residenceRepository.deleteById(id);
			ResidenceDTO deletedResidenceDTO = convertToDTO(residenceFromDb.get());
			return ResponseEntity.ok(deletedResidenceDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Converts a Residence entity to a ResidenceDTO.
	 * 
	 * @param residence The Residence entity that is being converted.
	 * @return The corresponding ResidenceDTO.
	 */
	private ResidenceDTO convertToDTO(Residence residence) {
		ResidenceDTO residenceDTO = new ResidenceDTO();
		residenceDTO.setPropertyId(residence.getPropertyId());
		residenceDTO.setName(residence.getName());
		residenceDTO.setAddress(residence.getAddress());
		residenceDTO.setDescription(residence.getDescription());
		residenceDTO.setRentalRate(residence.getRentalRate());
		residenceDTO.setSize(residence.getSize());
		residenceDTO.setIsAvailable(residence.isAvailable());
		residenceDTO.setNumberOfParkingSpots(residence.getNumberOfParkingSpots());
		residenceDTO.setPhotos(residence.getPhotos());
		residenceDTO.setNumberOfBedrooms(residence.getNumberOfBedrooms());
		residenceDTO.setNumberOfBathrooms(residence.getNumberOfBathrooms());
		residenceDTO.setHeatingType(residence.getHeatingType());
		residenceDTO.setIsPetFriendly(residence.isPetFriendly());
		residenceDTO.setIsFurnished(residence.isFurnished());
		residenceDTO.setOwnerId(residence.getOwner().getOwnerId());

		return residenceDTO;
	}

	/**
	 * Converts a ResidenceDTO to a Residence entity.
	 * 
	 * @param residenceDTO The ResidenceDTO that is being converted.
	 * @return The corresponding Residence entity.
	 */
	private Residence convertFromDTO(ResidenceDTO residenceDTO) {
		Residence residence = new Residence();
		if (residenceDTO.getPropertyId() != null) {
			residence.setPropertyId(residenceDTO.getPropertyId());
		}
		residence.setName(residenceDTO.getName());
		residence.setAddress(residenceDTO.getAddress());
		residence.setDescription(residenceDTO.getDescription());
		residence.setRentalRate(residenceDTO.getRentalRate());
		residence.setSize(residenceDTO.getSize());
		residence.setAvailable(residenceDTO.getIsAvailable());
		residence.setNumberOfParkingSpots(residenceDTO.getNumberOfParkingSpots());
		residence.setPhotos(residenceDTO.getPhotos());
		residence.setNumberOfBedrooms(residenceDTO.getNumberOfBedrooms());
		residence.setNumberOfBathrooms(residenceDTO.getNumberOfBathrooms());
		residence.setHeatingType(residenceDTO.getHeatingType());
		residence.setPetFriendly(residenceDTO.getIsPetFriendly());
		residence.setFurnished(residenceDTO.getIsFurnished());
		if (residenceDTO.getOwnerId() != null) {
			residence.setOwner(ownerRepository.findById(residenceDTO.getOwnerId()).get());
		}

		return residence;
	}
}
