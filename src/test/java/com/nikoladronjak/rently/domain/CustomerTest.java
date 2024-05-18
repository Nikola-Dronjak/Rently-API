package com.nikoladronjak.rently.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class CustomerTest {

	Customer customer;

	private static Validator validator;

	@BeforeEach
	void setUp() throws Exception {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

		customer = new Customer(1, "Pera", "Peric", "pera@gmail.com", "pera123", null);
	}

	@AfterEach
	void tearDown() throws Exception {
		customer = null;
	}

	@Test
	public void testFirstNameNull() {
		customer.setFirstName(null);

		Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
				.anyMatch(violation -> violation.getMessage().equals("The first name of the customer is required.")));
	}

	@Test
	public void testFirstNameLength() {
		customer.setFirstName("A");

		Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("The first name of the customer has to have at least 2 characters.")));
	}

	@Test
	public void testLastNameNull() {
		customer.setLastName(null);

		Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
				.anyMatch(violation -> violation.getMessage().equals("The last name of the customer is required.")));
	}

	@Test
	public void testLastNameLength() {
		customer.setLastName("A");

		Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("The last name of the customer has to have at least 2 characters.")));
	}

	@Test
	public void testEmailNull() {
		customer.setEmail(null);

		Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(
				violation -> violation.getMessage().equals("The email address of the customer is required.")));
	}

	@Test
	public void testEmailInvalid() {
		customer.setEmail("A");

		Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(
				violation -> violation.getMessage().equals("The email address of the customer must be valid.")));
	}

	@Test
	public void testPasswordNull() {
		customer.setPassword(null);

		Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
				.anyMatch(violation -> violation.getMessage().equals("The password of the customer is required.")));
	}

	@Test
	public void testPasswordLength() {
		customer.setPassword("A");

		Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("The password of the customer has to have at least 5 characters.")));
	}

	@Test
	public void testValidCustomer() {
		Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
		assertTrue(violations.isEmpty());
	}

	@Test
	public void testEqualsSameObject() {
		Customer newCustomer = customer;

		assertTrue(customer.equals(newCustomer));
	}

	@Test
	public void testEqualsNull() {
		assertFalse(customer.equals(null));
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEqualsDifferentTypes() {
		assertFalse(customer.equals(new String("")));
	}

	@ParameterizedTest
	@CsvSource({ "2, Pera, Peric, pera@gmail.com, pera123, '', false",
			"1, Mika, Peric, pera@gmail.com, pera123, '', false", "1, Pera, Mikic, pera@gmail.com, pera123, '', false",
			"1, Pera, Peric, mika@gmail.com, pera123, '', false", "1, Pera, Peric, pera@gmail.com, mika123, '', false",
			"1, Pera, Peric, pera@gmail.com, pera123, 'test', false",
			"1, Pera, Peric, pera@gmail.com, pera123, '', true" })
	void testEquals(int customerId, String firstName, String lastName, String email, String password, String leases,
			boolean eq) {
		List<Lease> leasesList = leases.equals("") ? null : new ArrayList<>();
		Customer newCustomer = new Customer(customerId, firstName, lastName, email, password, leasesList);

		assertEquals(eq, customer.equals(newCustomer));
	}

}
