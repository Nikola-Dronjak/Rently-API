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

/**
 * Represents a service class responsible for handling the business logic
 * related to Lease entities. This class manages operations such as retrieval,
 * adding, modification and deletion of Lease entities. Additionally, it
 * supports conversion between Lease entities and LeaseDTOs.
 * 
 * @author Nikola Dronjak
 */
@Service
public class LeaseService {

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
	 * Repository for accessing data related to rents.
	 */
	@Autowired
	private RentRepository rentRepository;

	/**
	 * Repository for accessing data related to leases.
	 */
	@Autowired
	private LeaseRepository leaseRepository;

	/**
	 * Retrieves all leases from the database and converts them to LeaseDTOs.
	 * 
	 * @return ResponseEntity containing a list of LeaseDTOs if successful, or an
	 *         error message with HttpStatus.INTERNAL_SERVER_ERROR status (500) if
	 *         an exception occurs.
	 */
	public ResponseEntity<?> getAll() {
		try {
			List<Lease> leases = leaseRepository.findAll();
			List<LeaseDTO> leaseDTOs = leases.stream().map(this::convertToDTO).collect(Collectors.toList());
			return ResponseEntity.ok(leaseDTOs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	/**
	 * Retrieves all leases associated with a specific propertyId from the database
	 * and converts them to LeaseDTOs.
	 * 
	 * @param propertyId The id of the property for which the leases are being
	 *                   queried.
	 * @return ResponseEntity containing a list of LeaseDTOs if successful, or an
	 *         error message with HttpStatus.INTERNAL_SERVER_ERROR status (500) if
	 *         an exception occurs.
	 */
	public ResponseEntity<?> getAllByPropertyId(Integer propertyId) {
		try {
			List<Lease> leases = leaseRepository.findAllByProperty_PropertyId(propertyId);
			List<LeaseDTO> leaseDTOs = leases.stream().map(this::convertToDTO).collect(Collectors.toList());
			return ResponseEntity.ok(leaseDTOs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	/**
	 * Retrieves all leases associated with a specific customerId from the database
	 * and converts them to LeaseDTOs.
	 * 
	 * @param customerId The id of the customer for which the leases are being
	 *                   queried.
	 * @return ResponseEntity containing a list of LeaseDTOs if successful, or an
	 *         error message with HttpStatus.INTERNAL_SERVER_ERROR status (500) if
	 *         an exception occurs.
	 */
	public ResponseEntity<?> getAllByCustomerId(Integer customerId) {
		try {
			List<Lease> leases = leaseRepository.findAllByCustomer_CustomerId(customerId);
			List<LeaseDTO> leaseDTOs = leases.stream().map(this::convertToDTO).collect(Collectors.toList());
			return ResponseEntity.ok(leaseDTOs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	/**
	 * Retrieves a lease from the database by the specified id and converts it to a
	 * LeaseDTO.
	 * 
	 * @param id The id of the lease that is being queried.
	 * @return ResponseEntity containing the LeaseDTO if successful, or an error
	 *         message with HttpStatus.BAD_REQUEST status (400) if an exception
	 *         occurs.
	 * @throws RuntimeException if there is no lease with the given id.
	 */
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

	/**
	 * Adds a new lease to the database based on the provided LeaseDTO.
	 * 
	 * @param leaseDTO The LeaseDTO containing the details of the lease that is
	 *                 being added.
	 * @return ResponseEntity containing the newly created LeaseDTO if successful,
	 *         or an error message with HttpStatus.BAD_REQUEST status (400) if an
	 *         exception occurs.
	 * @throws RuntimeException if:
	 *                          <ul>
	 *                          <li>There is no property for the given
	 *                          propertyId.</li>
	 *                          <li>The property is not available.</li>
	 *                          <li>There is no customer for the given
	 *                          customerId.</li>
	 *                          <li>The start date of the lease is after the end
	 *                          date.</li>
	 *                          <li>The end date of the lease is before the start
	 *                          date.</li>
	 *                          <li>The lease with the provided propertyId and
	 *                          customerId already exists.</li>
	 *                          </ul>
	 */
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

	/**
	 * Updates the lease information based on the provided id and LeaseDTO.
	 * 
	 * @param id       The id of the lease that is being updated.
	 * @param leaseDTO The LeaseDTO containing the updated details of the lease.
	 * @return ResponseEntity containing the updated LeaseDTO if successful, or an
	 *         error message with HttpStatus.BAD_REQUEST status (400) if an
	 *         exception occurs.
	 * @throws RuntimeException if:
	 *                          <ul>
	 *                          <li>There is no lease with the given id.</li>
	 *                          <li>There is no property for the given
	 *                          propertyId.</li>
	 *                          <li>There is no customer for the given
	 *                          customerId.</li>
	 *                          <li>The start date of the lease is after the end
	 *                          date.</li>
	 *                          <li>The end date of the lease is before the start
	 *                          date.</li>
	 *                          <li>The lease with the provided propertyId and
	 *                          customerId already exists.</li>
	 *                          </ul>
	 */
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

	/**
	 * Deletes the lease with the specified id.
	 * 
	 * @param id The id of the lease that is being deleted.
	 * @return ResponseEntity containing the deleted LeaseDTO if successful, or an
	 *         error message with HttpStatus.BAD_REQUEST status (400) if an
	 *         exception occurs.
	 * @throws RuntimeException if there is no lease with the given id, or if there
	 *                          are rents associated with the lease.
	 */
	public ResponseEntity<?> delete(Integer id) {
		try {
			Optional<Lease> leaseFromDb = leaseRepository.findById(id);
			if (!leaseFromDb.isPresent())
				throw new RuntimeException("There is no lease with the given id.");

			List<Rent> rentsFromDb = rentRepository.findAllByLease_LeaseId(id);
			if (!rentsFromDb.isEmpty())
				throw new RuntimeException("You cannot delete this lease since there are rents associated with it.");

			leaseRepository.deleteById(id);
			LeaseDTO deletedLeaseDTO = convertToDTO(leaseFromDb.get());
			return ResponseEntity.ok(deletedLeaseDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Converts a Lease entity to a LeaseDTO.
	 * 
	 * @param lease The Lease entity that is being converted.
	 * @return The corresponding LeaseDTO.
	 */
	private LeaseDTO convertToDTO(Lease lease) {
		LeaseDTO leaseDTO = new LeaseDTO();
		leaseDTO.setPropertyId(lease.getProperty().getPropertyId());
		leaseDTO.setCustomerId(lease.getCustomer().getCustomerId());
		leaseDTO.setRentalRate(lease.getRentalRate());
		leaseDTO.setStartDate(lease.getStartDate());
		leaseDTO.setEndDate(lease.getEndDate());

		return leaseDTO;
	}

	/**
	 * Converts a LeaseDTO to a Lease entity.
	 * 
	 * @param leaseDTO The LeaseDTO that is being converted.
	 * @return The corresponding Lease entity.
	 */
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
