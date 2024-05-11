package com.nikoladronjak.rently.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import com.nikoladronjak.rently.domain.Customer;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CustomerRepositoryTest {

	Customer customer1;

	Customer customer2;

	@Autowired
	private CustomerRepository customerRepository;

	@BeforeEach
	void setUp() throws Exception {
		customer1 = new Customer(1, "Pera", "Peric", "pera@gmail.com", "pera123", null);
		customer2 = new Customer(2, "Mika", "Mikic", "mika@gmail.com", "mika123", null);
	}

	@AfterEach
	void tearDown() throws Exception {
		customer1 = null;
		customer2 = null;
	}

	@Test
	void testFindAllEmpty() {
		List<Customer> customers = customerRepository.findAll();

		assertEquals(0, customers.size());
	}

	@Test
	void testFindAll() {
		customerRepository.save(customer1);
		customerRepository.save(customer2);

		List<Customer> customers = customerRepository.findAll();

		assertEquals(2, customers.size());
		assertEquals(customer1, customers.get(0));
		assertEquals(customer2, customers.get(1));
	}

	@Test
	void testFindByIdBadId() {
		customerRepository.save(customer1);

		Optional<Customer> customer = customerRepository.findById(2);

		assertTrue(customer.isEmpty());
	}

	@Test
	void testFindById() {
		customerRepository.save(customer1);

		Optional<Customer> customer = customerRepository.findById(1);

		assertTrue(customer.isPresent());
		assertEquals(customer1, customer.get());
	}

	@Test
	void testFindByEmailBadEmail() {
		customerRepository.save(customer1);

		Optional<Customer> customer = customerRepository.findByEmail("mika@gmail.com");

		assertTrue(customer.isEmpty());
	}

	@Test
	void testFindByEmail() {
		customerRepository.save(customer1);

		Optional<Customer> customer = customerRepository.findByEmail("pera@gmail.com");

		assertTrue(customer.isPresent());
		assertEquals(customer1, customer.get());
	}

	@Test
	void testSave() {
		Customer savedCustomer = customerRepository.save(customer1);

		assertEquals(customer1, savedCustomer);
	}

	@Test
	void testDeleteById() {
		customerRepository.save(customer1);

		customerRepository.deleteById(1);

		Optional<Customer> deletedCustomer = customerRepository.findById(1);
		assertFalse(deletedCustomer.isPresent());
	}

}
