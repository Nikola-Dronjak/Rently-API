package com.nikoladronjak.rently.service;

import java.util.List;
import java.util.Optional;
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

@Service
public class UtilityLeaseService {

	@Autowired
	private UtilityRepository utilityRepository;

	@Autowired
	private EventSpaceRepository eventSpaceRepository;

	@Autowired
	private OfficeSpaceRepository officeSpaceRepository;

	@Autowired
	private RentRepository rentRepository;

	@Autowired
	private UtilityLeaseRepository utilityLeaseRepository;

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

			Optional<UtilityLease> existingUtilityLease = utilityLeaseRepository
					.findByUtility_UtilityIdAndProperty_PropertyId(utilityLeaseDTO.getUtilityId(),
							utilityLeaseDTO.getPropertyId());
			if (existingUtilityLease.isPresent())
				throw new RuntimeException("This utility lease already exists.");

			UtilityLease utilityLease = convertFromDTO(utilityLeaseDTO);
			UtilityLease newUtilityLease = utilityLeaseRepository.save(utilityLease);
			UtilityLeaseDTO newUtilityLeaseDTO = convertToDTO(newUtilityLease);
			return ResponseEntity.ok(newUtilityLeaseDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

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

			Optional<UtilityLease> existingUtilityLease = utilityLeaseRepository
					.findByUtility_UtilityIdAndProperty_PropertyId(utilityLeaseDTO.getUtilityId(),
							utilityLeaseDTO.getPropertyId());
			if (existingUtilityLease.isPresent()) {
				if (existingUtilityLease.get().getUtilityLeaseId() != id) {
					throw new RuntimeException("This utility lease already exists.");
				}
			}

			UtilityLease utilityLease = convertFromDTO(utilityLeaseDTO);
			utilityLease.setUtilityLeaseId(id);
			UtilityLease updatedUtilityLease = utilityLeaseRepository.save(utilityLease);
			UtilityLeaseDTO updatedUtilityLeaseDTO = convertToDTO(updatedUtilityLease);
			return ResponseEntity.ok(updatedUtilityLeaseDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

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

	private UtilityLeaseDTO convertToDTO(UtilityLease utilityLease) {
		UtilityLeaseDTO utilityLeaseDTO = new UtilityLeaseDTO();
		utilityLeaseDTO.setUtilityId(utilityLease.getUtility().getUtilityId());
		utilityLeaseDTO.setPropertyId(utilityLease.getProperty().getPropertyId());
		utilityLeaseDTO.setRentalRate(utilityLease.getRentalRate());

		return utilityLeaseDTO;
	}

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