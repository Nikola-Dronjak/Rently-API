package com.nikoladronjak.rently.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nikoladronjak.rently.domain.Customer;
import com.nikoladronjak.rently.domain.Lease;
import com.nikoladronjak.rently.domain.Owner;
import com.nikoladronjak.rently.dto.CustomerDTO;
import com.nikoladronjak.rently.repository.CustomerRepository;
import com.nikoladronjak.rently.repository.LeaseRepository;
import com.nikoladronjak.rently.repository.OwnerRepository;

@SpringBootTest
class CustomerServiceTest {

	Owner owner1;

	Lease lease;

	List<Lease> leases;

	Customer customer1;

	Customer customer2;

	@Mock
	private OwnerRepository ownerRepository;

	@Mock
	private LeaseRepository leaseRepository;

	@Mock
	private CustomerRepository customerRepository;

	@InjectMocks
	private CustomerService customerService;

	@BeforeEach
	void setUp() throws Exception {
		owner1 = new Owner(1, "Jovan", "Jovanovic", "jovan@gmail.com", "jovan123", "1234567890");

		lease = new Lease(1, 0, null, null, null, customer1, null);

		leases = new ArrayList<Lease>();
		leases.add(lease);

		customer1 = new Customer(1, "Pera", "Peric", "pera@gmail.com", "pera123", null);
		customer2 = new Customer(2, "Mika", "Mikic", "mika@gmail.com", "mika123", null);
	}

	@AfterEach
	void tearDown() throws Exception {
		owner1 = null;

		lease = null;

		leases = null;

		customer1 = null;
		customer2 = null;
	}

	@Test
	void testGetAllError() {
		when(customerRepository.findAll()).thenThrow(new RuntimeException("Something went wrong"));

		ResponseEntity<?> response = customerService.getAll();

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("Something went wrong", response.getBody());
	}

	@Test
	void testGetAll() {
		List<Customer> customers = new ArrayList<Customer>();
		customers.add(customer1);
		customers.add(customer2);
		when(customerRepository.findAll()).thenReturn(customers);

		ResponseEntity<?> response = customerService.getAll();
		List<CustomerDTO> customerDTOs = customers.stream().map(this::convertToDTO).collect(Collectors.toList());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(customerDTOs, response.getBody());
	}

	@Test
	void testGetByIdBadId() {
		when(customerRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = customerService.getById(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no customer with the given id.", response.getBody());
	}

	@Test
	void testGetById() {
		when(customerRepository.findById(1)).thenReturn(Optional.of(customer1));

		ResponseEntity<?> response = customerService.getById(1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(customer1), response.getBody());
	}

	@Test
	void testAddDuplicateCustomer() {
		when(customerRepository.findByEmail(customer1.getEmail())).thenReturn(Optional.of(customer1));

		ResponseEntity<?> response = customerService.add(convertToDTO(customer1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This user already exists.", response.getBody());
	}

	@Test
	void testAddDuplicateOwner() {
		when(ownerRepository.findByEmail(customer1.getEmail())).thenReturn(Optional.of(owner1));

		ResponseEntity<?> response = customerService.add(convertToDTO(customer1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This user already exists.", response.getBody());
	}

	@Test
	void testAdd() {
		when(customerRepository.findByEmail(owner1.getEmail())).thenReturn(Optional.empty());
		when(ownerRepository.findByEmail(owner1.getEmail())).thenReturn(Optional.empty());
		when(customerRepository.save(any(Customer.class))).thenReturn(customer1);

		ResponseEntity<?> response = customerService.add(convertToDTO(customer1));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(customer1), response.getBody());
	}

	@Test
	void testUpdateBadId() {
		when(customerRepository.findById(customer1.getCustomerId())).thenReturn(Optional.empty());

		ResponseEntity<?> response = customerService.update(1, convertToDTO(customer1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no customer with the given id.", response.getBody());
	}

	@Test
	void testUpdateDuplicateCustomer() {
		when(customerRepository.findById(customer1.getCustomerId())).thenReturn(Optional.of(customer1));
		when(customerRepository.findByEmail(customer1.getEmail())).thenReturn(Optional.of(customer1));
		when(customerRepository.save(any(Customer.class))).thenThrow(new RuntimeException("This user already exists."));

		ResponseEntity<?> response = customerService.update(customer1.getCustomerId(), convertToDTO(customer1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This user already exists.", response.getBody());
	}

	@Test
	void testUpdateDuplicateOwner() {
		when(customerRepository.findById(customer1.getCustomerId())).thenReturn(Optional.of(customer1));
		when(ownerRepository.findByEmail(customer1.getEmail())).thenReturn(Optional.of(owner1));
		when(customerRepository.save(any(Customer.class))).thenThrow(new RuntimeException("This user already exists."));

		ResponseEntity<?> response = customerService.update(customer1.getCustomerId(), convertToDTO(customer1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This user already exists.", response.getBody());
	}

	@Test
	void testUpdate() {
		when(customerRepository.findById(customer1.getCustomerId())).thenReturn(Optional.of(customer1));
		when(customerRepository.findByEmail(owner1.getEmail())).thenReturn(Optional.empty());
		when(ownerRepository.findByEmail(owner1.getEmail())).thenReturn(Optional.empty());
		when(customerRepository.save(any(Customer.class))).thenReturn(customer1);

		ResponseEntity<?> response = customerService.update(1, convertToDTO(customer1));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(customer1), response.getBody());
	}

	@Test
	void testDeleteBadId() {
		when(customerRepository.findById(customer1.getCustomerId())).thenReturn(Optional.empty());

		ResponseEntity<?> response = customerService.delete(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no customer with the given id.", response.getBody());
	}

	@Test
	void testDeleteAssociatedLease() {
		when(customerRepository.findById(customer1.getCustomerId())).thenReturn(Optional.of(customer1));
		when(leaseRepository.findAllByCustomer_CustomerId(customer1.getCustomerId())).thenReturn(leases);

		ResponseEntity<?> response = customerService.delete(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("You cannot delete this customer since there are leases associated with him.", response.getBody());
	}

	@Test
	void testDelete() {
		when(customerRepository.findById(customer1.getCustomerId())).thenReturn(Optional.of(customer1));
		when(leaseRepository.findAllByCustomer_CustomerId(customer1.getCustomerId()))
				.thenReturn(new ArrayList<Lease>());

		ResponseEntity<?> response = customerService.delete(1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(customer1), response.getBody());
	}

	private CustomerDTO convertToDTO(Customer customer) {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setFirstName(customer.getFirstName());
		customerDTO.setLastName(customer.getLastName());
		customerDTO.setEmail(customer.getEmail());
		customerDTO.setPassword(customer.getPassword());

		return customerDTO;
	}

}
