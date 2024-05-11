package com.nikoladronjak.rently.service;

import java.util.List;
import java.util.Optional;
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

@Service
public class EventSpaceService {

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private OfficeSpaceRepository officeSpaceRepository;

	@Autowired
	private ResidenceRepository residenceRepository;

	@Autowired
	private LeaseRepository leaseRepository;

	@Autowired
	private UtilityLeaseRepository utilityLeaseRepository;

	@Autowired
	private EventSpaceRepository eventSpaceRepository;

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

	public ResponseEntity<?> add(EventSpaceDTO eventSpaceDTO) {
		try {
			if (ownerRepository.findById(eventSpaceDTO.getOwnerId()).isEmpty())
				throw new RuntimeException("There is no owner with the given id.");

			if (residenceRepository.findByAddress(eventSpaceDTO.getAddress()).isPresent()
					|| eventSpaceRepository.findByAddress(eventSpaceDTO.getAddress()).isPresent()
					|| officeSpaceRepository.findByAddress(eventSpaceDTO.getAddress()).isPresent())
				throw new RuntimeException("This property already exists.");

			EventSpace eventSpace = convertFromDTO(eventSpaceDTO);
			EventSpace newEventSpace = eventSpaceRepository.save(eventSpace);
			EventSpaceDTO newEventSpaceDTO = convertToDTO(newEventSpace);
			return ResponseEntity.ok(newEventSpaceDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	public ResponseEntity<?> update(Integer id, EventSpaceDTO eventSpaceDTO) {
		try {
			Optional<EventSpace> eventSpaceFromDb = eventSpaceRepository.findById(id);
			if (!eventSpaceFromDb.isPresent())
				throw new RuntimeException("There is no event space with the given id.");

			if (ownerRepository.findById(eventSpaceDTO.getOwnerId()).isEmpty())
				throw new RuntimeException("There is no owner with the given id.");

			if (!eventSpaceFromDb.get().getAddress().equals(eventSpaceDTO.getAddress())) {
				if (residenceRepository.findByAddress(eventSpaceDTO.getAddress()).isPresent()
						|| eventSpaceRepository.findByAddress(eventSpaceDTO.getAddress()).isPresent()
						|| officeSpaceRepository.findByAddress(eventSpaceDTO.getAddress()).isPresent()) {
					throw new RuntimeException("This property already exists.");
				}
			}

			EventSpace eventSpace = convertFromDTO(eventSpaceDTO);
			eventSpace.setPropertyId(id);
			EventSpace updatedEventSpace = eventSpaceRepository.save(eventSpace);
			EventSpaceDTO updatedEventSpaceDTO = convertToDTO(updatedEventSpace);
			return ResponseEntity.ok(updatedEventSpaceDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

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
		eventSpace.setOwner(ownerRepository.findById(eventSpaceDTO.getOwnerId()).get());

		return eventSpace;
	}
}
