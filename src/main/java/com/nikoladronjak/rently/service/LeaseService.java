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
import com.nikoladronjak.rently.domain.Property;
import com.nikoladronjak.rently.domain.Rent;
import com.nikoladronjak.rently.domain.Residence;
import com.nikoladronjak.rently.domain.Lease;
import com.nikoladronjak.rently.dto.LeaseDTO;
import com.nikoladronjak.rently.repository.CustomerRepository;
import com.nikoladronjak.rently.repository.EventSpaceRepository;
import com.nikoladronjak.rently.repository.LeaseRepository;
import com.nikoladronjak.rently.repository.OfficeSpaceRepository;
import com.nikoladronjak.rently.repository.RentRepository;
import com.nikoladronjak.rently.repository.ResidenceRepository;

@Service
public class LeaseService {

	@Autowired
	private ResidenceRepository residenceRepository;

	@Autowired
	private EventSpaceRepository eventSpaceRepository;

	@Autowired
	private OfficeSpaceRepository officeSpaceRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private RentRepository rentRepository;

	@Autowired
	private LeaseRepository leaseRepository;

	public ResponseEntity<?> getAll() {
		try {
			List<Lease> leases = leaseRepository.findAll();
			List<LeaseDTO> leaseDTOs = leases.stream().map(this::convertToDTO).collect(Collectors.toList());
			return ResponseEntity.ok(leaseDTOs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	public ResponseEntity<?> getAllByPropertyId(Integer propertyId) {
		try {
			List<Lease> leases = leaseRepository.findAllByProperty_PropertyId(propertyId);
			List<LeaseDTO> leaseDTOs = leases.stream().map(this::convertToDTO).collect(Collectors.toList());
			return ResponseEntity.ok(leaseDTOs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	public ResponseEntity<?> getAllByCustomerId(Integer customerId) {
		try {
			List<Lease> leases = leaseRepository.findAllByCustomer_CustomerId(customerId);
			List<LeaseDTO> leaseDTOs = leases.stream().map(this::convertToDTO).collect(Collectors.toList());
			return ResponseEntity.ok(leaseDTOs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	public ResponseEntity<?> getById(Integer id) {
		try {
			Optional<Lease> leaseFromDb = leaseRepository.findById(id);
			if (!leaseFromDb.isPresent())
				throw new RuntimeException("There is no lease with the given id.");

			LeaseDTO leaseDTO = convertToDTO(leaseFromDb.get());
			return ResponseEntity.ok(leaseDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	public ResponseEntity<?> add(LeaseDTO leaseDTO) {
		try {
			Optional<Residence> residenceFromDb = residenceRepository.findById(leaseDTO.getPropertyId());
			Optional<EventSpace> eventSpaceFromDb = eventSpaceRepository.findById(leaseDTO.getPropertyId());
			Optional<OfficeSpace> officeSpaceFromDb = officeSpaceRepository.findById(leaseDTO.getPropertyId());
			if ((!residenceFromDb.isPresent() && eventSpaceFromDb.isEmpty() && officeSpaceFromDb.isEmpty())
					|| (!eventSpaceFromDb.isPresent() && residenceFromDb.isEmpty() && officeSpaceFromDb.isEmpty())
					|| (!officeSpaceFromDb.isPresent() && residenceFromDb.isEmpty() && eventSpaceFromDb.isEmpty()))
				throw new RuntimeException(
						"The property has to be either a residence, an event space or an office space.");

			if (residenceFromDb.isPresent() && !residenceFromDb.get().isAvailable())
				throw new RuntimeException("This property is currently unavailable.");

			if (eventSpaceFromDb.isPresent() && !eventSpaceFromDb.get().isAvailable())
				throw new RuntimeException("This property is currently unavailable.");

			if (officeSpaceFromDb.isPresent() && !officeSpaceFromDb.get().isAvailable())
				throw new RuntimeException("This property is currently unavailable.");

			Optional<Customer> customerFromDb = customerRepository.findById(leaseDTO.getCustomerId());
			if (!customerFromDb.isPresent())
				throw new RuntimeException("There is no customer for the given customerId.");

			if (leaseDTO.getStartDate().after(leaseDTO.getEndDate()))
				throw new RuntimeException("The start date of the lease has to be before the end date of the lease.");

			if (leaseDTO.getEndDate().before(leaseDTO.getStartDate()))
				throw new RuntimeException("The end date of the lease has to be after the start date of the lease.");

			Optional<Lease> existingLease = leaseRepository.findByProperty_PropertyIdAndCustomer_CustomerId(
					leaseDTO.getPropertyId(), leaseDTO.getCustomerId());
			if (existingLease.isPresent())
				throw new RuntimeException("This lease already exists.");

			Lease lease = convertFromDTO(leaseDTO);
			if (residenceFromDb.isPresent()) {
				lease.setRentalRate(residenceFromDb.get().getRentalRate());
				Residence residence = residenceFromDb.get();
				residence.setAvailable(false);
				residenceRepository.save(residence);
			}

			if (eventSpaceFromDb.isPresent()) {
				lease.setRentalRate(eventSpaceFromDb.get().getRentalRate());
				EventSpace eventSpace = eventSpaceFromDb.get();
				eventSpace.setAvailable(false);
				eventSpaceRepository.save(eventSpace);
			}

			if (officeSpaceFromDb.isPresent()) {
				lease.setRentalRate(officeSpaceFromDb.get().getRentalRate());
				OfficeSpace officeSpace = officeSpaceFromDb.get();
				officeSpace.setAvailable(false);
				officeSpaceRepository.save(officeSpace);
			}
			Lease newLease = leaseRepository.save(lease);
			LeaseDTO newLeaseDTO = convertToDTO(newLease);
			return ResponseEntity.ok(newLeaseDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	public ResponseEntity<?> update(Integer id, LeaseDTO leaseDTO) {
		try {
			Optional<Lease> leaseFromDb = leaseRepository.findById(id);
			if (!leaseFromDb.isPresent())
				throw new RuntimeException("There is no lease with the given id.");

			Optional<Residence> residenceFromDb = residenceRepository.findById(leaseDTO.getPropertyId());
			Optional<EventSpace> eventSpaceFromDb = eventSpaceRepository.findById(leaseDTO.getPropertyId());
			Optional<OfficeSpace> officeSpaceFromDb = officeSpaceRepository.findById(leaseDTO.getPropertyId());
			if ((!residenceFromDb.isPresent() && eventSpaceFromDb.isEmpty() && officeSpaceFromDb.isEmpty())
					|| (!eventSpaceFromDb.isPresent() && residenceFromDb.isEmpty() && officeSpaceFromDb.isEmpty())
					|| (!officeSpaceFromDb.isPresent() && residenceFromDb.isEmpty() && eventSpaceFromDb.isEmpty()))
				throw new RuntimeException(
						"The property has to be either a residence, an event space or an office space.");

			Optional<Customer> customerFromDb = customerRepository.findById(leaseDTO.getCustomerId());
			if (!customerFromDb.isPresent())
				throw new RuntimeException("There is no customer for the given customerId.");

			if (leaseDTO.getStartDate().after(leaseDTO.getEndDate()))
				throw new RuntimeException("The start date of the lease has to be before the end date of the lease.");

			if (leaseDTO.getEndDate().before(leaseDTO.getStartDate()))
				throw new RuntimeException("The end date of the lease has to be after the start date of the lease.");

			Optional<Lease> existingLease = leaseRepository.findByProperty_PropertyIdAndCustomer_CustomerId(
					leaseDTO.getPropertyId(), leaseDTO.getCustomerId());
			if (existingLease.isPresent()) {
				if (existingLease.get().getLeaseId() != id) {
					throw new RuntimeException("This lease already exists.");
				}
			}

			Lease lease = convertFromDTO(leaseDTO);
			lease.setLeaseId(id);
			Lease updatedLease = leaseRepository.save(lease);
			LeaseDTO updatedLeaseDTO = convertToDTO(updatedLease);
			return ResponseEntity.ok(updatedLeaseDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	public ResponseEntity<?> delete(Integer id) {
		try {
			Optional<Lease> leaseFromDb = leaseRepository.findById(id);
			if (!leaseFromDb.isPresent())
				throw new RuntimeException("There is no lease with the given id.");

			Optional<Rent> rentFromDb = rentRepository.findByLeaseLeaseId(id);
			if (rentFromDb.isPresent())
				throw new RuntimeException("You cannot delete this lease since there are rents associated with it.");

			leaseRepository.deleteById(id);
			LeaseDTO deletedLeaseDTO = convertToDTO(leaseFromDb.get());
			return ResponseEntity.ok(deletedLeaseDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	private LeaseDTO convertToDTO(Lease lease) {
		LeaseDTO leaseDTO = new LeaseDTO();
		leaseDTO.setPropertyId(lease.getProperty().getPropertyId());
		leaseDTO.setCustomerId(lease.getCustomer().getCustomerId());
		leaseDTO.setRentalRate(lease.getRentalRate());
		leaseDTO.setStartDate(lease.getStartDate());
		leaseDTO.setEndDate(lease.getEndDate());

		return leaseDTO;
	}

	private Lease convertFromDTO(LeaseDTO leaseDTO) {
		Property property = new Property();
		property.setPropertyId(leaseDTO.getPropertyId());

		Customer customer = new Customer();
		customer.setCustomerId(leaseDTO.getCustomerId());

		Lease lease = new Lease();
		lease.setProperty(property);
		lease.setCustomer(customer);
		if (leaseDTO.getRentalRate() != null) {
			lease.setRentalRate(leaseDTO.getRentalRate());
		}
		lease.setStartDate(leaseDTO.getStartDate());
		lease.setEndDate(leaseDTO.getEndDate());

		return lease;
	}
}
