package com.nikoladronjak.rently.service;

import java.util.List;
import java.util.Optional;
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

@Service
public class OwnerService {

	@Autowired
	private ResidenceRepository residenceRepository;

	@Autowired
	private EventSpaceRepository eventSpaceRepository;

	@Autowired
	private OfficeSpaceRepository officeSpaceRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private OwnerRepository ownerRepository;

	public ResponseEntity<?> getAll() {
		try {
			List<Owner> owners = ownerRepository.findAll();
			List<OwnerDTO> ownerDTOs = owners.stream().map(this::convertToDTO).collect(Collectors.toList());
			return ResponseEntity.ok(ownerDTOs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

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

	public ResponseEntity<?> add(OwnerDTO ownerDTO) {
		try {
			if (ownerRepository.findByEmail(ownerDTO.getEmail()).isPresent()
					|| customerRepository.findByEmail(ownerDTO.getEmail()).isPresent())
				throw new RuntimeException("This user already exists.");

			Owner owner = convertFromDTO(ownerDTO);
			Owner newOwner = ownerRepository.save(owner);
			OwnerDTO newOwnerDTO = convertToDTO(newOwner);
			return ResponseEntity.ok(newOwnerDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	public ResponseEntity<?> update(Integer id, OwnerDTO ownerDTO) {
		try {
			Optional<Owner> ownerFromDb = ownerRepository.findById(id);
			if (!ownerFromDb.isPresent())
				throw new RuntimeException("There is no owner with the given id.");

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

			Owner owner = convertFromDTO(ownerDTO);
			owner.setOwnerId(id);
			Owner updatedOwner = ownerRepository.save(owner);
			OwnerDTO updatedOwnerDTO = convertToDTO(updatedOwner);
			return ResponseEntity.ok(updatedOwnerDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

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

	private OwnerDTO convertToDTO(Owner owner) {
		OwnerDTO ownerDTO = new OwnerDTO();
		ownerDTO.setFirstName(owner.getFirstName());
		ownerDTO.setLastName(owner.getLastName());
		ownerDTO.setEmail(owner.getEmail());
		ownerDTO.setPassword(owner.getPassword());
		ownerDTO.setPhoneNumber(owner.getPhoneNumber());

		return ownerDTO;
	}

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
