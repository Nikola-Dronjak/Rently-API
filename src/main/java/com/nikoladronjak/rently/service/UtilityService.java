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

@Service
public class UtilityService {

	@Autowired
	private UtilityLeaseRepository utilityLeaseRepository;

	@Autowired
	private UtilityRepository utilityRepository;

	public ResponseEntity<?> getAll() {
		try {
			List<Utility> utilities = utilityRepository.findAll();
			List<UtilityDTO> utilityDTOs = utilities.stream().map(this::convertToDTO).collect(Collectors.toList());
			return ResponseEntity.ok(utilityDTOs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

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

	private UtilityDTO convertToDTO(Utility utility) {
		UtilityDTO utilityDTO = new UtilityDTO();
		utilityDTO.setName(utility.getName());
		utilityDTO.setDescription(utility.getDescription());

		return utilityDTO;
	}

	private Utility convertFromDTO(UtilityDTO utilityDTO) {
		Utility utility = new Utility();
		utility.setName(utilityDTO.getName());
		utility.setDescription(utilityDTO.getDescription());

		return utility;
	}
}
