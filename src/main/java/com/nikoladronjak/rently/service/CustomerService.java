package com.nikoladronjak.rently.service;

import java.util.List;
import java.util.Optional;
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

@Service
public class CustomerService {

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private LeaseRepository leaseRepository;

	@Autowired
	private CustomerRepository customerRepository;

	public ResponseEntity<?> getAll() {
		try {
			List<Customer> customers = customerRepository.findAll();
			List<CustomerDTO> customerDTOs = customers.stream().map(this::convertToDTO).collect(Collectors.toList());
			return ResponseEntity.ok(customerDTOs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

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

	public ResponseEntity<?> add(CustomerDTO customerDTO) {
		try {
			if (customerRepository.findByEmail(customerDTO.getEmail()).isPresent()
					|| ownerRepository.findByEmail(customerDTO.getEmail()).isPresent())
				throw new RuntimeException("This user already exists.");

			Customer customer = convertFromDTO(customerDTO);
			Customer newCustomer = customerRepository.save(customer);
			CustomerDTO newCustomerDTO = convertToDTO(newCustomer);
			return ResponseEntity.ok(newCustomerDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	public ResponseEntity<?> update(Integer id, CustomerDTO customerDTO) {
		try {
			Optional<Customer> customerFromDb = customerRepository.findById(id);
			if (!customerFromDb.isPresent())
				throw new RuntimeException("There is no customer with the given id.");

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

			Customer customer = convertFromDTO(customerDTO);
			customer.setCustomerId(id);
			Customer updatedCustomer = customerRepository.save(customer);
			CustomerDTO updatedCustomerDTO = convertToDTO(updatedCustomer);
			return ResponseEntity.ok(updatedCustomerDTO);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

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

	private CustomerDTO convertToDTO(Customer customer) {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setFirstName(customer.getFirstName());
		customerDTO.setLastName(customer.getLastName());
		customerDTO.setEmail(customer.getEmail());
		customerDTO.setPassword(customer.getPassword());

		return customerDTO;
	}

	private Customer convertFromDTO(CustomerDTO customerDTO) {
		Customer customer = new Customer();
		customer.setFirstName(customerDTO.getFirstName());
		customer.setLastName(customerDTO.getLastName());
		customer.setEmail(customerDTO.getEmail());
		customer.setPassword(customerDTO.getPassword());

		return customer;
	}
}
