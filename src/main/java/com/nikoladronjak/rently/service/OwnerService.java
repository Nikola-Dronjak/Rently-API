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

import com.nikoladronjak.rently.domain.Customer;
import com.nikoladronjak.rently.domain.EventSpace;
import com.nikoladronjak.rently.domain.OfficeSpace;
import com.nikoladronjak.rently.domain.Owner;
import com.nikoladronjak.rently.domain.Residence;
import com.nikoladronjak.rently.dto.OwnerDTO;
import com.nikoladronjak.rently.repository.CustomerRepository;
import com.nikoladronjak.rently.repository.EventSpaceRepository;
import com.nikoladronjak.rently.repository.OfficeSpaceRepository;
import com.nikoladronjak.rently.repository.OwnerRepository;
import com.nikoladronjak.rently.repository.ResidenceRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * Represents a service class responsible for handling the business logic
 * related to Owner entities. This class manages operations such as retrieval,
 * adding, modification and deletion of Owner entities. Additionally, it
 * supports conversion between Owner entities and OwnerDTOs.
 * 
 * @author Nikola Dronjak
 */
@Service
public class OwnerService {

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
	 * Repository for accessing data related to office spaces.
	 */
	@Autowired
	private OfficeSpaceRepository officeSpaceRepository;

	/**
	 * Repository for accessing data related to customers.
	 */
	@Autowired
	private CustomerRepository customerRepository;

	/**
	 * Repository for accessing data related to owners.
	 */
	@Autowired
	private OwnerRepository ownerRepository;

	/**
	 * Validator for validating Owner entities.
	 */
	private final Validator validator;

	/**
	 * Default constructor for OwnerService. Initializes the validator using a
	 * ValidatorFactory.
	 */
	public OwnerService() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		this.validator = factory.getValidator();
	}

	/**
	 * Retrieves all owners from the database and converts them to OwnerDTOs.
	 * 
	 * @return ResponseEntity containing a list of OwnerDTOs if successful, or an
	 *         error message with HttpStatus.INTERNAL_SERVER_ERROR status (500) if
	 *         an exception occurs.
	 */
	public ResponseEntity<?> getAll() {
		try {
			List<Owner> owners = ownerRepository.findAll();
			List<OwnerDTO> ownerDTOs = owners.stream().map(this::convertToDTO).collect(Collectors.toList());
			return ResponseEntity.ok(ownerDTOs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	/**
	 * Retrieves an owner from the database by the specified id and converts it to
	 * an OwnerDTO.
	 * 
	 * @param id The id of the owner that is being queried.
	 * @return ResponseEntity containing the OwnerDTO if successful, or an error
	 *         message with HttpStatus.BAD_REQUEST status (400) if an exception
	 *         occurs.
	 * @throws RuntimeException if there is no owner with the given id.
	 */
	public ResponseEntity<?> getById(Integer id) {
		try {
			Optional<Owner> ownerFromDb = ownerRepository.findById(id);
			if (!ownerFromDb.isPresent())
				throw new RuntimeException("There is no owner with the given id.");

			OwnerDTO ownerDTO = convertToDTO(ownerFromDb.get());
			return ResponseEntity.ok(ownerDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Adds a new owner to the database based on the provided OwnerDTO.
	 * 
	 * @param ownerDTO The OwnerDTO containing the details of the owner that is
	 *                 being added.
	 * @return ResponseEntity containing the newly created OwnerDTO if successful,
	 *         or an error message with HttpStatus.BAD_REQUEST status (400) if the
	 *         ownerDTO is not valid, or if an exception occurs.
	 * @throws RuntimeException if the owner or customer with the provided email
	 *                          already exists.
	 */
	public ResponseEntity<?> add(OwnerDTO ownerDTO) {
		try {
			Owner owner = convertFromDTO(ownerDTO);
			Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
			if (!violations.isEmpty()) {
				Map<String, String> errors = new HashMap<>();
				for (ConstraintViolation<Owner> violation : violations) {
					errors.put(violation.getPropertyPath().toString(), violation.getMessage());
				}
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
			}

			if (ownerRepository.findByEmail(ownerDTO.getEmail()).isPresent()
					|| customerRepository.findByEmail(ownerDTO.getEmail()).isPresent())
				throw new RuntimeException("This user already exists.");

			Owner newOwner = ownerRepository.save(owner);
			OwnerDTO newOwnerDTO = convertToDTO(newOwner);
			return ResponseEntity.ok(newOwnerDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Updates the owner information based on the provided id and OwnerDTO.
	 * 
	 * @param id       The id of the owner that is being updated.
	 * @param ownerDTO The OwnerDTO containing the updated details of the owner.
	 * @return ResponseEntity containing the updated OwnerDTO if successful, or an
	 *         error message with HttpStatus.BAD_REQUEST status (400) if the
	 *         ownerDTO is not valid, or if an exception occurs.
	 * @throws RuntimeException if there is no owner with the given id, or if the
	 *                          owner or customer with the provided email already
	 *                          exists.
	 */
	public ResponseEntity<?> update(Integer id, OwnerDTO ownerDTO) {
		try {
			Optional<Owner> ownerFromDb = ownerRepository.findById(id);
			if (!ownerFromDb.isPresent())
				throw new RuntimeException("There is no owner with the given id.");

			Owner owner = convertFromDTO(ownerDTO);
			Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
			if (!violations.isEmpty()) {
				Map<String, String> errors = new HashMap<>();
				for (ConstraintViolation<Owner> violation : violations) {
					errors.put(violation.getPropertyPath().toString(), violation.getMessage());
				}
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
			}

			if (!ownerFromDb.get().getEmail().equals(ownerDTO.getEmail())) {
				Optional<Owner> existingOwner = ownerRepository.findByEmail(ownerDTO.getEmail());
				if (existingOwner.isPresent() && existingOwner.get().getOwnerId() != id) {
					throw new RuntimeException("This user already exists.");
				}
				Optional<Customer> existingCustomer = customerRepository.findByEmail(ownerDTO.getEmail());
				if (existingCustomer.isPresent()) {
					throw new RuntimeException("This user already exists.");
				}
			}

			owner.setOwnerId(id);
			Owner updatedOwner = ownerRepository.save(owner);
			OwnerDTO updatedOwnerDTO = convertToDTO(updatedOwner);
			return ResponseEntity.ok(updatedOwnerDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Deletes the owner with the specified id.
	 * 
	 * @param id The id of the owner that is being deleted.
	 * @return ResponseEntity containing the deleted OwnerDTO if successful, or an
	 *         error message with HttpStatus.BAD_REQUEST status (400) if an
	 *         exception occurs.
	 * @throws RuntimeException if there is no owner with the given id, or if there
	 *                          are properties associated with the owner, such as
	 *                          residences, event spaces or office spaces.
	 */
	public ResponseEntity<?> delete(Integer id) {
		try {
			Optional<Owner> ownerFromDb = ownerRepository.findById(id);
			if (!ownerFromDb.isPresent())
				throw new RuntimeException("There is no owner with the given id.");

			List<Residence> residencesFromDb = residenceRepository.findAllByOwner_OwnerId(id);
			List<EventSpace> eventSpacesFromDb = eventSpaceRepository.findAllByOwner_OwnerId(id);
			List<OfficeSpace> officeSpacesFromDb = officeSpaceRepository.findAllByOwner_OwnerId(id);
			if (!residencesFromDb.isEmpty() || !eventSpacesFromDb.isEmpty() || !officeSpacesFromDb.isEmpty())
				throw new RuntimeException(
						"You cannot delete this owner since there are properties associated with him.");

			ownerRepository.deleteById(id);
			OwnerDTO deletedOwnerDTO = convertToDTO(ownerFromDb.get());
			return ResponseEntity.ok(deletedOwnerDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Converts an Owner entity to an OwnerDTO.
	 * 
	 * @param owner The Owner entity that is being converted.
	 * @return The corresponding OwnerDTO.
	 */
	private OwnerDTO convertToDTO(Owner owner) {
		OwnerDTO ownerDTO = new OwnerDTO();
		ownerDTO.setFirstName(owner.getFirstName());
		ownerDTO.setLastName(owner.getLastName());
		ownerDTO.setEmail(owner.getEmail());
		ownerDTO.setPassword(owner.getPassword());
		ownerDTO.setPhoneNumber(owner.getPhoneNumber());

		return ownerDTO;
	}

	/**
	 * Converts an OwnerDTO to an Owner entity.
	 * 
	 * @param ownerDTO The OwnerDTO that is being converted.
	 * @return The corresponding Owner entity.
	 */
	private Owner convertFromDTO(OwnerDTO ownerDTO) {
		Owner owner = new Owner();
		owner.setFirstName(ownerDTO.getFirstName());
		owner.setLastName(ownerDTO.getLastName());
		owner.setEmail(ownerDTO.getEmail());
		owner.setPassword(ownerDTO.getPassword());
		owner.setPhoneNumber(ownerDTO.getPhoneNumber());

		return owner;
	}
}
