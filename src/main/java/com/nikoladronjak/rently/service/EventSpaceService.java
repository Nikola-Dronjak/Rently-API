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
import com.nikoladronjak.rently.domain.Lease;
import com.nikoladronjak.rently.domain.UtilityLease;
import com.nikoladronjak.rently.dto.EventSpaceDTO;
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
 * related to EventSpace entities. This class manages operations such as
 * retrieval, adding, modification and deletion of EventSpace entities.
 * Additionally, it supports conversion between EventSpace entities and
 * EventSpaceDTOs.
 * 
 * @author Nikola Dronjak
 */
@Service
public class EventSpaceService {

	/**
	 * Repository for accessing data related to owners.
	 */
	@Autowired
	private OwnerRepository ownerRepository;

	/**
	 * Repository for accessing data related to office spaces.
	 */
	@Autowired
	private OfficeSpaceRepository officeSpaceRepository;

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
	 * Repository for accessing data related to event spaces.
	 */
	@Autowired
	private EventSpaceRepository eventSpaceRepository;

	/**
	 * Validator for validating EventSpace entities.
	 */
	private final Validator validator;

	/**
	 * Default constructor for EventSpaceService. Initializes the validator using a
	 * ValidatorFactory.
	 */
	public EventSpaceService() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		this.validator = factory.getValidator();
	}

	/**
	 * Retrieves all event spaces from the database and converts them to
	 * EventSpaceDTOs.
	 * 
	 * @return ResponseEntity containing a list of EventSpaceDTOs if successful, or
	 *         an error message with HttpStatus.INTERNAL_SERVER_ERROR status (500)
	 *         if an exception occurs.
	 */
	public ResponseEntity<?> getAll() {
		try {
			List<EventSpace> eventSpaces = eventSpaceRepository.findAll();
			List<EventSpaceDTO> eventSpaceDTOs = eventSpaces.stream().map(this::convertToDTO)
					.collect(Collectors.toList());
			return ResponseEntity.ok(eventSpaceDTOs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	/**
	 * Retrieves an event space from the database by the specified id and converts
	 * it to an EventSpaceDTO.
	 * 
	 * @param id The id of the event space that is being queried.
	 * @return ResponseEntity containing the EventSpaceDTO if successful, or an
	 *         error message with HttpStatus.BAD_REQUEST status (400) if an
	 *         exception occurs.
	 * @throws RuntimeException if there is no event space with the given id.
	 */
	public ResponseEntity<?> getById(Integer id) {
		try {
			Optional<EventSpace> eventSpaceFromDb = eventSpaceRepository.findById(id);
			if (!eventSpaceFromDb.isPresent())
				throw new RuntimeException("There is no event space with the given id.");

			EventSpaceDTO eventSpaceDTO = convertToDTO(eventSpaceFromDb.get());
			return ResponseEntity.ok(eventSpaceDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Adds a new event space to the database based on the provided EventSpaceDTO.
	 * 
	 * @param eventSpaceDTO The EventSpaceDTO containing the details of the event
	 *                      space that is being added.
	 * @return ResponseEntity containing the newly created EventSpaceDTO if
	 *         successful, or an error message with HttpStatus.BAD_REQUEST status
	 *         (400) if the eventSpaceDTO is not valid, or if an exception occurs.
	 * @throws RuntimeException if there is no owner with the given ownerId, or if a
	 *                          property with the provided address already exists.
	 */
	public ResponseEntity<?> add(EventSpaceDTO eventSpaceDTO) {
		try {
			if (ownerRepository.findById(eventSpaceDTO.getOwnerId()).isEmpty())
				throw new RuntimeException("There is no owner with the given id.");

			EventSpace eventSpace = convertFromDTO(eventSpaceDTO);
			Set<ConstraintViolation<EventSpace>> violations = validator.validate(eventSpace);
			if (!violations.isEmpty()) {
				Map<String, String> errors = new HashMap<>();
				for (ConstraintViolation<EventSpace> violation : violations) {
					errors.put(violation.getPropertyPath().toString(), violation.getMessage());
				}
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
			}

			if (residenceRepository.findByAddress(eventSpaceDTO.getAddress()).isPresent()
					|| eventSpaceRepository.findByAddress(eventSpaceDTO.getAddress()).isPresent()
					|| officeSpaceRepository.findByAddress(eventSpaceDTO.getAddress()).isPresent())
				throw new RuntimeException("This property already exists.");

			EventSpace newEventSpace = eventSpaceRepository.save(eventSpace);
			EventSpaceDTO newEventSpaceDTO = convertToDTO(newEventSpace);
			return ResponseEntity.ok(newEventSpaceDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Updates the event space information based on the provided id and
	 * EventSpaceDTO.
	 * 
	 * @param id            The id of the event space that is being updated.
	 * @param eventSpaceDTO The EventSpaceDTO containing the updated details of the
	 *                      event space.
	 * @return ResponseEntity containing the newly created EventSpaceDTO if
	 *         successful, or an error message with HttpStatus.BAD_REQUEST status
	 *         (400) if the eventSpaceDTO is not valid, or if an exception occurs
	 * @throws RuntimeException if:
	 *                          <ul>
	 *                          <li>There is no event space with the given id.</li>
	 *                          <li>There is no owner with the given ownerId</li>
	 *                          <li>The property with the provided address already
	 *                          exists.</li>
	 *                          </ul>
	 */
	public ResponseEntity<?> update(Integer id, EventSpaceDTO eventSpaceDTO) {
		try {
			Optional<EventSpace> eventSpaceFromDb = eventSpaceRepository.findById(id);
			if (!eventSpaceFromDb.isPresent())
				throw new RuntimeException("There is no event space with the given id.");

			if (ownerRepository.findById(eventSpaceDTO.getOwnerId()).isEmpty())
				throw new RuntimeException("There is no owner with the given id.");

			EventSpace eventSpace = convertFromDTO(eventSpaceDTO);
			Set<ConstraintViolation<EventSpace>> violations = validator.validate(eventSpace);
			if (!violations.isEmpty()) {
				Map<String, String> errors = new HashMap<>();
				for (ConstraintViolation<EventSpace> violation : violations) {
					errors.put(violation.getPropertyPath().toString(), violation.getMessage());
				}
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
			}

			if (!eventSpaceFromDb.get().getAddress().equals(eventSpaceDTO.getAddress())) {
				if (residenceRepository.findByAddress(eventSpaceDTO.getAddress()).isPresent()
						|| eventSpaceRepository.findByAddress(eventSpaceDTO.getAddress()).isPresent()
						|| officeSpaceRepository.findByAddress(eventSpaceDTO.getAddress()).isPresent()) {
					throw new RuntimeException("This property already exists.");
				}
			}

			eventSpace.setPropertyId(id);
			EventSpace updatedEventSpace = eventSpaceRepository.save(eventSpace);
			EventSpaceDTO updatedEventSpaceDTO = convertToDTO(updatedEventSpace);
			return ResponseEntity.ok(updatedEventSpaceDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Deletes the event space with the specified id.
	 * 
	 * @param id The id of the event space that is being deleted.
	 * @return ResponseEntity containing the deleted EventDTO if successful, or an
	 *         error message with HttpStatus.BAD_REQUEST status (400) if an
	 *         exception occurs.
	 * @throws RuntimeException if:
	 *                          <ul>
	 *                          <li>There is no event space with the given id.</li>
	 *                          <li>There are leases associated with the event
	 *                          space.</li>
	 *                          <li>There are utility leases associated with the
	 *                          event space.</li>
	 *                          </ul>
	 */
	public ResponseEntity<?> delete(Integer id) {
		try {
			Optional<EventSpace> eventSpaceFromDb = eventSpaceRepository.findById(id);
			if (!eventSpaceFromDb.isPresent())
				throw new RuntimeException("There is no event space with the given id.");

			List<Lease> leasesFromDb = leaseRepository.findAllByProperty_PropertyId(id);
			if (!leasesFromDb.isEmpty())
				throw new RuntimeException(
						"You cannot delete this event space since there are leases associated with it.");

			List<UtilityLease> utilityLeasesFromDb = utilityLeaseRepository.findAllByProperty_PropertyId(id);
			if (!utilityLeasesFromDb.isEmpty())
				throw new RuntimeException(
						"You cannot delete this event space since there are utility leases associated with it.");

			eventSpaceRepository.deleteById(id);
			EventSpaceDTO deletedEventSpaceDTO = convertToDTO(eventSpaceFromDb.get());
			return ResponseEntity.ok(deletedEventSpaceDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Converts an EventSpace entity to an EventSpaceDTO.
	 * 
	 * @param eventSpace The EventSpace entity that is being converted.
	 * @return The corresponding EventSpaceDTO.
	 */
	private EventSpaceDTO convertToDTO(EventSpace eventSpace) {
		EventSpaceDTO eventSpaceDTO = new EventSpaceDTO();
		eventSpaceDTO.setPropertyId(eventSpace.getPropertyId());
		eventSpaceDTO.setName(eventSpace.getName());
		eventSpaceDTO.setAddress(eventSpace.getAddress());
		eventSpaceDTO.setDescription(eventSpace.getDescription());
		eventSpaceDTO.setRentalRate(eventSpace.getRentalRate());
		eventSpaceDTO.setSize(eventSpace.getSize());
		eventSpaceDTO.setIsAvailable(eventSpace.isAvailable());
		eventSpaceDTO.setNumberOfParkingSpots(eventSpace.getNumberOfParkingSpots());
		eventSpaceDTO.setPhotos(eventSpace.getPhotos());
		eventSpaceDTO.setCapacity(eventSpace.getCapacity());
		eventSpaceDTO.setHasKitchen(eventSpace.isHasKitchen());
		eventSpaceDTO.setHasBar(eventSpace.isHasBar());
		eventSpaceDTO.setOwnerId(eventSpace.getOwner().getOwnerId());

		return eventSpaceDTO;
	}

	/**
	 * Converts an EventSpaceDTO to an EventSpace entity.
	 * 
	 * @param eventSpaceDTO The EventSpaceDTO that is being converted.
	 * @return The corresponding EventSpace entity.
	 */
	private EventSpace convertFromDTO(EventSpaceDTO eventSpaceDTO) {
		EventSpace eventSpace = new EventSpace();
		if (eventSpaceDTO.getPropertyId() != null) {
			eventSpace.setPropertyId(eventSpaceDTO.getPropertyId());
		}
		eventSpace.setName(eventSpaceDTO.getName());
		eventSpace.setAddress(eventSpaceDTO.getAddress());
		eventSpace.setDescription(eventSpaceDTO.getDescription());
		eventSpace.setRentalRate(eventSpaceDTO.getRentalRate());
		eventSpace.setSize(eventSpaceDTO.getSize());
		eventSpace.setAvailable(eventSpaceDTO.getIsAvailable());
		eventSpace.setNumberOfParkingSpots(eventSpaceDTO.getNumberOfParkingSpots());
		eventSpace.setPhotos(eventSpaceDTO.getPhotos());
		eventSpace.setCapacity(eventSpaceDTO.getCapacity());
		eventSpace.setHasKitchen(eventSpaceDTO.getHasKitchen());
		eventSpace.setHasBar(eventSpaceDTO.getHasBar());
		if (eventSpaceDTO.getOwnerId() != null) {
			eventSpace.setOwner(ownerRepository.findById(eventSpaceDTO.getOwnerId()).get());
		}

		return eventSpace;
	}
}
