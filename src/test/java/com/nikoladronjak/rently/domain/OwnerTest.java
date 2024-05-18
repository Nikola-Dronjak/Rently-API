package com.nikoladronjak.rently.domain;

import static org.junit.jupiter.api.Assertions.*;

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

class OwnerTest {

	Owner owner;

	private static Validator validator;

	@BeforeEach
	void setUp() throws Exception {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

		owner = new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");
	}

	@AfterEach
	void tearDown() throws Exception {
		owner = null;
	}

	@Test
	public void testFirstNameNull() {
		owner.setFirstName(null);

		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
				.anyMatch(violation -> violation.getMessage().equals("The first name of the owner is required.")));
	}

	@Test
	public void testFirstNameLength() {
		owner.setFirstName("A");

		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("The first name of the owner has to have at least 2 characters.")));
	}

	@Test
	public void testLastNameNull() {
		owner.setLastName(null);

		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
				.anyMatch(violation -> violation.getMessage().equals("The last name of the owner is required.")));
	}

	@Test
	public void testLastNameLength() {
		owner.setLastName("A");

		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("The last name of the owner has to have at least 2 characters.")));
	}

	@Test
	public void testEmailNull() {
		owner.setEmail(null);

		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
				.anyMatch(violation -> violation.getMessage().equals("The email address of the owner is required.")));
	}

	@Test
	public void testEmailInvalid() {
		owner.setEmail("A");

		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
				.anyMatch(violation -> violation.getMessage().equals("The email address of the owner must be valid.")));
	}

	@Test
	public void testPasswordNull() {
		owner.setPassword(null);

		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
				.anyMatch(violation -> violation.getMessage().equals("The password of the owner is required.")));
	}

	@Test
	public void testPasswordLength() {
		owner.setPassword("A");

		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("The password of the owner has to have at least 5 characters.")));
	}

	@Test
	public void testPhoneNumberNull() {
		owner.setPhoneNumber(null);

		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
				.anyMatch(violation -> violation.getMessage().equals("The phone number of the owner is required.")));
	}

	@Test
	public void testPhoneNumberLength() {
		owner.setPhoneNumber("1");

		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("The phone number of the owner has to have at least 10 characters.")));
	}

	@Test
	public void testValidOwner() {
		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertTrue(violations.isEmpty());
	}

	@Test
	public void testEqualsSameObject() {
		Owner newOwner = owner;

		assertTrue(owner.equals(newOwner));
	}

	@Test
	public void testEqualsNull() {
		assertFalse(owner.equals(null));
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEqualsDifferentTypes() {
		assertFalse(owner.equals(new String("")));
	}

	@ParameterizedTest
	@CsvSource({ "2, Pera, Peric, pera@gmail.com, pera123, 1234567890, false",
			"1, Mika, Peric, pera@gmail.com, pera123, 1234567890, false",
			"1, Pera, Mikic, pera@gmail.com, pera123, 1234567890, false",
			"1, Pera, Peric, mika@gmail.com, pera123, 1234567890, false",
			"1, Pera, Peric, pera@gmail.com, mika123, 1234567890, false",
			"1, Pera, Peric, pera@gmail.com, pera123, 0987654321, false",
			"1, Pera, Peric, pera@gmail.com, pera123, 1234567890, true" })
	void testEquals(int ownerId, String fristName, String lastName, String email, String password, String phoneNumber,
			boolean eq) {
		Owner newOwner = new Owner(ownerId, fristName, lastName, email, password, phoneNumber);

		assertEquals(eq, owner.equals(newOwner));
	}

}
