package com.nikoladronjak.rently.service;

import java.util.List;
import java.util.Optional;
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

@Service
public class OfficeSpaceService {

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private ResidenceRepository residenceRepository;

	@Autowired
	private EventSpaceRepository eventSpaceRepository;

	@Autowired
	private LeaseRepository leaseRepository;

	@Autowired
	private UtilityLeaseRepository utilityLeaseRepository;

	@Autowired
	private OfficeSpaceRepository officeSpaceRepository;

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

	public ResponseEntity<?> add(OfficeSpaceDTO officeSpaceDTO) {
		try {
			if (ownerRepository.findById(officeSpaceDTO.getOwnerId()).isEmpty())
				throw new RuntimeException("There is no owner with the given id.");

			if (residenceRepository.findByAddress(officeSpaceDTO.getAddress()).isPresent()
					|| eventSpaceRepository.findByAddress(officeSpaceDTO.getAddress()).isPresent()
					|| officeSpaceRepository.findByAddress(officeSpaceDTO.getAddress()).isPresent())
				throw new RuntimeException("This property already exists.");

			OfficeSpace officeSpace = convertFromDTO(officeSpaceDTO);
			OfficeSpace newOfficeSpace = officeSpaceRepository.save(officeSpace);
			OfficeSpaceDTO newOfficeSpaceDTO = convertToDTO(newOfficeSpace);
			return ResponseEntity.ok(newOfficeSpaceDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	public ResponseEntity<?> update(Integer id, OfficeSpaceDTO officeSpaceDTO) {
		try {
			Optional<OfficeSpace> officeSpaceFromDb = officeSpaceRepository.findById(id);
			if (!officeSpaceFromDb.isPresent())
				throw new RuntimeException("There is no office space with the given id.");

			if (ownerRepository.findById(officeSpaceDTO.getOwnerId()).isEmpty())
				throw new RuntimeException("There is no owner with the given id.");

			if (!officeSpaceFromDb.get().getAddress().equals(officeSpaceDTO.getAddress())) {
				if (residenceRepository.findByAddress(officeSpaceDTO.getAddress()).isPresent()
						|| eventSpaceRepository.findByAddress(officeSpaceDTO.getAddress()).isPresent()
						|| officeSpaceRepository.findByAddress(officeSpaceDTO.getAddress()).isPresent()) {
					throw new RuntimeException("This property already exists.");
				}
			}

			OfficeSpace officeSpace = convertFromDTO(officeSpaceDTO);
			officeSpace.setPropertyId(id);
			OfficeSpace updatedOfficeSpace = officeSpaceRepository.save(officeSpace);
			OfficeSpaceDTO updatedOfficeSpaceDTO = convertToDTO(updatedOfficeSpace);
			return ResponseEntity.ok(updatedOfficeSpaceDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

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
		officeSpace.setOwner(ownerRepository.findById(officeSpaceDTO.getOwnerId()).get());

		return officeSpace;
	}
}
