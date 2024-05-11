package com.nikoladronjak.rently.service;

import java.util.List;
import java.util.Optional;
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

@Service
public class ResidenceService {

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private EventSpaceRepository eventSpaceRepository;

	@Autowired
	private OfficeSpaceRepository officeSpaceRepository;

	@Autowired
	private LeaseRepository leaseRepository;

	@Autowired
	private ResidenceRepository residenceRepository;

	public ResponseEntity<?> getAll() {
		try {
			List<Residence> residences = residenceRepository.findAll();
			List<ResidenceDTO> residenceDTOs = residences.stream().map(this::convertToDTO).collect(Collectors.toList());
			return ResponseEntity.ok(residenceDTOs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

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

	public ResponseEntity<?> add(ResidenceDTO residenceDTO) {
		try {
			if (ownerRepository.findById(residenceDTO.getOwnerId()).isEmpty())
				throw new RuntimeException("There is no owner with the given id.");

			if (residenceRepository.findByAddress(residenceDTO.getAddress()).isPresent()
					|| eventSpaceRepository.findByAddress(residenceDTO.getAddress()).isPresent()
					|| officeSpaceRepository.findByAddress(residenceDTO.getAddress()).isPresent())
				throw new RuntimeException("This property already exists.");

			Residence residence = convertFromDTO(residenceDTO);
			Residence newResidence = residenceRepository.save(residence);
			ResidenceDTO newResidenceDTO = convertToDTO(newResidence);
			return ResponseEntity.ok(newResidenceDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	public ResponseEntity<?> update(Integer id, ResidenceDTO residenceDTO) {
		try {
			Optional<Residence> residenceFromDb = residenceRepository.findById(id);
			if (!residenceFromDb.isPresent())
				throw new RuntimeException("There is no residence with the given id.");

			if (ownerRepository.findById(residenceDTO.getOwnerId()).isEmpty())
				throw new RuntimeException("There is no owner with the given id.");

			if (!residenceFromDb.get().getAddress().equals(residenceDTO.getAddress())) {
				if (residenceRepository.findByAddress(residenceDTO.getAddress()).isPresent()
						|| eventSpaceRepository.findByAddress(residenceDTO.getAddress()).isPresent()
						|| officeSpaceRepository.findByAddress(residenceDTO.getAddress()).isPresent()) {
					throw new RuntimeException("This property already exists.");
				}
			}

			Residence residence = convertFromDTO(residenceDTO);
			residence.setPropertyId(id);
			Residence updatedResidence = residenceRepository.save(residence);
			ResidenceDTO updatedResidenceDTO = convertToDTO(updatedResidence);
			return ResponseEntity.ok(updatedResidenceDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

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
		residence.setOwner(ownerRepository.findById(residenceDTO.getOwnerId()).get());

		return residence;
	}
}
