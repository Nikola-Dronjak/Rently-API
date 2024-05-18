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

import com.nikoladronjak.rently.domain.Customer;
import com.nikoladronjak.rently.domain.Lease;
import com.nikoladronjak.rently.domain.Owner;
import com.nikoladronjak.rently.dto.CustomerDTO;
import com.nikoladronjak.rently.repository.CustomerRepository;
import com.nikoladronjak.rently.repository.LeaseRepository;
import com.nikoladronjak.rently.repository.OwnerRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * Represents a service class responsible for handling the business logic
 * related to Customer entities. This class manages operations such as
 * retrieval, adding, modification and deletion of Customer entities.
 * Additionally, it supports conversion between Customer entities and
 * CustomerDTOs.
 * 
 * @author Nikola Dronjak
 */
@Service
public class CustomerService {

	/**
	 * Repository for accessing data related to owners.
	 */
	@Autowired
	private OwnerRepository ownerRepository;

	/**
	 * Repository for accessing data related to leases.
	 */
	@Autowired
	private LeaseRepository leaseRepository;

	/**
	 * Repository for accessing data related to customers.
	 */
	@Autowired
	private CustomerRepository customerRepository;

	/**
	 * Validator for validating Customer entities.
	 */
	private final Validator validator;

	/**
	 * Default constructor for CustomerService. Initializes the validator using a
	 * ValidatorFactory.
	 */
	public CustomerService() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		this.validator = factory.getValidator();
	}

	/**
	 * Retrieves all customers from the database and converts them to CustomerDTOs.
	 * 
	 * @return ResponseEntity containing a list of CustomerDTOs if successful, or an
	 *         error message with HttpStatus.INTERNAL_SERVER_ERROR status (500) if
	 *         an exception occurs.
	 */
	public ResponseEntity<?> getAll() {
		try {
			List<Customer> customers = customerRepository.findAll();
			List<CustomerDTO> customerDTOs = customers.stream().map(this::convertToDTO).collect(Collectors.toList());
			return ResponseEntity.ok(customerDTOs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	/**
	 * Retrieves a customer from the database by the specified id and converts it to
	 * a CustomerDTO.
	 * 
	 * @param id The id of the customer that is being queried.
	 * @return ResponseEntity containing the CustomerDTO if successful, or an error
	 *         message with HttpStatus.BAD_REQUEST status (400) if an exception
	 *         occurs.
	 * @throws RuntimeException if there is no customer with the given id.
	 */
	public ResponseEntity<?> getById(Integer id) {
		try {
			Optional<Customer> customerFromDb = customerRepository.findById(id);
			if (!customerFromDb.isPresent())
				throw new RuntimeException("There is no customer with the given id.");

			CustomerDTO customerDTO = convertToDTO(customerFromDb.get());
			return ResponseEntity.ok(customerDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Adds a new customer to the database based on the provided CustomerDTO.
	 * 
	 * @param customerDTO The CustomerDTO containing the details of the customer
	 *                    that is being added.
	 * @return ResponseEntity containing the newly created CustomerDTO if
	 *         successful, or an error message with HttpStatus.BAD_REQUEST status
	 *         (400) if the customerDTO is not valid, or if an exception occurs.
	 * @throws RuntimeException if the customer or owner with the provided email
	 *                          already exists.
	 */
	public ResponseEntity<?> add(CustomerDTO customerDTO) {
		try {
			Customer customer = convertFromDTO(customerDTO);
			Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
			if (!violations.isEmpty()) {
				Map<String, String> errors = new HashMap<>();
				for (ConstraintViolation<Customer> violation : violations) {
					errors.put(violation.getPropertyPath().toString(), violation.getMessage());
				}
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
			}

			if (customerRepository.findByEmail(customerDTO.getEmail()).isPresent()
					|| ownerRepository.findByEmail(customerDTO.getEmail()).isPresent())
				throw new RuntimeException("This user already exists.");

			Customer newCustomer = customerRepository.save(customer);
			CustomerDTO newCustomerDTO = convertToDTO(newCustomer);
			return ResponseEntity.ok(newCustomerDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Updates the customer information based on the provided id and CustomerDTO.
	 * 
	 * @param id          The id of the customer that is being updated.
	 * @param customerDTO The CustomerDTO containing the updated details of the
	 *                    customer.
	 * @return ResponseEntity containing the updated CustomerDTO if successful, or
	 *         an error message with HttpStatus.BAD_REQUEST status (400) if the
	 *         customerDTO is not valid, or if an exception occurs.
	 * @throws RuntimeException if there is no customer with the given id, or if the
	 *                          customer or owner with the provided email already
	 *                          exists.
	 */
	public ResponseEntity<?> update(Integer id, CustomerDTO customerDTO) {
		try {
			Optional<Customer> customerFromDb = customerRepository.findById(id);
			if (!customerFromDb.isPresent())
				throw new RuntimeException("There is no customer with the given id.");

			Customer customer = convertFromDTO(customerDTO);
			Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
			if (!violations.isEmpty()) {
				Map<String, String> errors = new HashMap<>();
				for (ConstraintViolation<Customer> violation : violations) {
					errors.put(violation.getPropertyPath().toString(), violation.getMessage());
				}
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
			}

			if (!customerFromDb.get().getEmail().equals(customerDTO.getEmail())) {
				Optional<Customer> existingCustomer = customerRepository.findByEmail(customerDTO.getEmail());
				if (existingCustomer.isPresent() && existingCustomer.get().getCustomerId() != id) {
					throw new RuntimeException("This user already exists.");
				}
				Optional<Owner> existingOwner = ownerRepository.findByEmail(customerDTO.getEmail());
				if (existingOwner.isPresent()) {
					throw new RuntimeException("This user already exists.");
				}
			}

			customer.setCustomerId(id);
			Customer updatedCustomer = customerRepository.save(customer);
			CustomerDTO updatedCustomerDTO = convertToDTO(updatedCustomer);
			return ResponseEntity.ok(updatedCustomerDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Deletes the customer with the specified id.
	 * 
	 * @param id The id of the customer that is being deleted.
	 * @return ResponseEntity containing the deleted CustomerDTO if successful, or
	 *         an error message with HttpStatus.BAD_REQUEST status (400) if an
	 *         exception occurs.
	 * @throws RuntimeException if there is no customer with the given id, or if
	 *                          there are leases associated with the customer.
	 */
	public ResponseEntity<?> delete(Integer id) {
		try {
			Optional<Customer> customerFromDb = customerRepository.findById(id);
			if (!customerFromDb.isPresent())
				throw new RuntimeException("There is no customer with the given id.");

			List<Lease> leasesFromDb = leaseRepository.findAllByCustomer_CustomerId(id);
			if (!leasesFromDb.isEmpty())
				throw new RuntimeException(
						"You cannot delete this customer since there are leases associated with him.");

			customerRepository.deleteById(id);
			CustomerDTO deletedCustomerDTO = convertToDTO(customerFromDb.get());
			return ResponseEntity.ok(deletedCustomerDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Converts a Customer entity to a CustomerDTO.
	 * 
	 * @param customer The Customer entity that is being converted.
	 * @return The corresponding CustomerDTO.
	 */
	private CustomerDTO convertToDTO(Customer customer) {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setFirstName(customer.getFirstName());
		customerDTO.setLastName(customer.getLastName());
		customerDTO.setEmail(customer.getEmail());
		customerDTO.setPassword(customer.getPassword());

		return customerDTO;
	}

	/**
	 * Converts a CustomerDTO to a Customer entity.
	 * 
	 * @param customerDTO The CustomerDTO that is being converted.
	 * @return The corresponding Customer entity.
	 */
	private Customer convertFromDTO(CustomerDTO customerDTO) {
		Customer customer = new Customer();
		customer.setFirstName(customerDTO.getFirstName());
		customer.setLastName(customerDTO.getLastName());
		customer.setEmail(customerDTO.getEmail());
		customer.setPassword(customerDTO.getPassword());

		return customer;
	}
}
