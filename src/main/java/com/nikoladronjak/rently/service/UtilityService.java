package com.nikoladronjak.rently.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nikoladronjak.rently.domain.Utility;
import com.nikoladronjak.rently.domain.UtilityLease;
import com.nikoladronjak.rently.dto.UtilityDTO;
import com.nikoladronjak.rently.repository.UtilityLeaseRepository;
import com.nikoladronjak.rently.repository.UtilityRepository;

/**
 * Represents a service class responsible for handling the business logic
 * related to Utility entities. This class manages operations such as retrieval,
 * adding, modification and deletion of Utility entities. Additionally, it
 * supports conversion between Utility entities and UtilityDTOs.
 * 
 * @author Nikola Dronjak
 */
@Service
public class UtilityService {

	/**
	 * Repository for accessing data related to utility leases.
	 */
	@Autowired
	private UtilityLeaseRepository utilityLeaseRepository;

	/**
	 * Repository for accessing data related to utilities.
	 */
	@Autowired
	private UtilityRepository utilityRepository;

	/**
	 * Retrieves all utilities from the database and converts them to UtilityDTOs.
	 * 
	 * @return ResponseEntity containing a list of UtilityDTOs if successful, or an
	 *         error message with HttpStatus.INTERNAL_SERVER_ERROR status (500) if
	 *         an exception occurs.
	 */
	public ResponseEntity<?> getAll() {
		try {
			List<Utility> utilities = utilityRepository.findAll();
			List<UtilityDTO> utilityDTOs = utilities.stream().map(this::convertToDTO).collect(Collectors.toList());
			return ResponseEntity.ok(utilityDTOs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	/**
	 * Retrieves a utility from the database by the specified id and converts it to
	 * a UtilityDTO.
	 * 
	 * @param id The id of the utility that is being queried.
	 * @return ResponseEntity containing the UtilityDTO if successful, or an error
	 *         message with HttpStatus.BAD_REQUEST status (400) if an exception
	 *         occurs.
	 * @throws RuntimeException if there is no utility with the given id.
	 */
	public ResponseEntity<?> getById(Integer id) {
		try {
			Optional<Utility> utilityFromDb = utilityRepository.findById(id);
			if (!utilityFromDb.isPresent())
				throw new RuntimeException("There is no utility with the given id.");

			UtilityDTO utilityDTO = convertToDTO(utilityFromDb.get());
			return ResponseEntity.ok(utilityDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Adds a new utility to the database based on the provided UtilityDTO.
	 * 
	 * @param utilityDTO The UtilityDTO containing the details of the utility that
	 *                   is being added.
	 * @return ResponseEntity containing the newly created UtilityDTO if successful,
	 *         or an error message with HttpStatus.BAD_REQUEST status (400) if an
	 *         exception occurs.
	 * @throws RuntimeException if a utility with the provided name already exists.
	 */
	public ResponseEntity<?> add(UtilityDTO utilityDTO) {
		try {
			if (utilityRepository.findByName(utilityDTO.getName()).isPresent())
				throw new RuntimeException("This utility already exists.");

			Utility utility = convertFromDTO(utilityDTO);
			Utility newUtility = utilityRepository.save(utility);
			UtilityDTO newUtilityDTO = convertToDTO(newUtility);
			return ResponseEntity.ok(newUtilityDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Updates the utility information based on the provided id and UtilityDTO.
	 * 
	 * @param id         The id of the utility that is being updated.
	 * @param utilityDTO The UtilityDTO containing the updated details of the
	 *                   utility.
	 * @return ResponseEntity containing the updated UtilityDTO if successful, or an
	 *         error message with HttpStatus.BAD_REQUEST status (400) if an
	 *         exception occurs.
	 * @throws RuntimeException if there is no utility with the given id, or if a
	 *                          utility with the provided name already exists.
	 */
	public ResponseEntity<?> update(Integer id, UtilityDTO utilityDTO) {
		try {
			Optional<Utility> utilityFromDb = utilityRepository.findById(id);
			if (!utilityFromDb.isPresent())
				throw new RuntimeException("There is no utility with the given id.");

			if (!utilityFromDb.get().getName().equals(utilityDTO.getName())) {
				Optional<Utility> existingUtility = utilityRepository.findByName(utilityDTO.getName());
				if (existingUtility.isPresent() && existingUtility.get().getUtilityId() != id) {
					throw new RuntimeException("This utility already exists.");
				}
			}

			Utility utility = convertFromDTO(utilityDTO);
			utility.setUtilityId(id);
			Utility updatedUtility = utilityRepository.save(utility);
			UtilityDTO updatedUtilityDTO = convertToDTO(updatedUtility);
			return ResponseEntity.ok(updatedUtilityDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Deletes the utility with the specified id.
	 * 
	 * @param id The id of the utility that is being deleted.
	 * @return ResponseEntity containing the deleted UtilityDTO if successful, or an
	 *         error message with HttpStatus.BAD_REQUEST status (400) if an
	 *         exception occurs.
	 * @throws RuntimeException if there is no utility with the given id, or if
	 *                          there are utility leases associated with the
	 *                          utility.
	 */
	public ResponseEntity<?> delete(Integer id) {
		try {
			Optional<Utility> utilityFromDb = utilityRepository.findById(id);
			if (!utilityFromDb.isPresent())
				throw new RuntimeException("There is no utility with the given id.");

			List<UtilityLease> utilityLeasesFromDb = utilityLeaseRepository.findAllByUtility_UtilityId(id);
			if (!utilityLeasesFromDb.isEmpty())
				throw new RuntimeException(
						"You cannot delete this utility since there are utility leases associated with it.");

			utilityRepository.deleteById(id);
			UtilityDTO deletedUtilityDTO = convertToDTO(utilityFromDb.get());
			return ResponseEntity.ok(deletedUtilityDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Converts a Utility entity to a UtilityDTO.
	 * 
	 * @param utility The Utility entity that is being converted.
	 * @return The corresponding UtilityDTO.
	 */
	private UtilityDTO convertToDTO(Utility utility) {
		UtilityDTO utilityDTO = new UtilityDTO();
		utilityDTO.setName(utility.getName());
		utilityDTO.setDescription(utility.getDescription());

		return utilityDTO;
	}

	/**
	 * Converts a UtilityDTO to a Utility entity.
	 * 
	 * @param utilityDTO The UtilityDTO that is being converted.
	 * @return The corresponding Utility entity.
	 */
	private Utility convertFromDTO(UtilityDTO utilityDTO) {
		Utility utility = new Utility();
		utility.setName(utilityDTO.getName());
		utility.setDescription(utilityDTO.getDescription());

		return utility;
	}
}
