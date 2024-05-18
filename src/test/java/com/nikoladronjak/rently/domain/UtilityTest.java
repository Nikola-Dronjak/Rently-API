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

class UtilityTest {

	Utility utility;

	private static Validator validator;

	@BeforeEach
	void setUp() throws Exception {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

		utility = new Utility(1, "Microphone", "Wireless microphone.", null);
	}

	@AfterEach
	void tearDown() throws Exception {
		utility = null;
	}

	@Test
	public void testNameNull() {
		utility.setName(null);

		Set<ConstraintViolation<Utility>> violations = validator.validate(utility);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
				.anyMatch(violation -> violation.getMessage().equals("The name of the utility is required.")));
	}

	@Test
	public void testNameLength() {
		utility.setName("A");

		Set<ConstraintViolation<Utility>> violations = validator.validate(utility);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("The name of the utility has to have at least 5 characters.")));
	}

	@Test
	public void testDescriptionNull() {
		utility.setDescription(null);

		Set<ConstraintViolation<Utility>> violations = validator.validate(utility);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
				.anyMatch(violation -> violation.getMessage().equals("The description of the utility is required.")));
	}

	@Test
	public void testValidUtility() {
		Set<ConstraintViolation<Utility>> violations = validator.validate(utility);
		assertTrue(violations.isEmpty());
	}

	@Test
	public void testEqualsSameObject() {
		Utility newUtility = utility;

		assertTrue(utility.equals(newUtility));
	}

	@Test
	public void testEqualsNull() {
		assertFalse(utility.equals(null));
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEqualsDifferentTypes() {
		assertFalse(utility.equals(new String("")));
	}

	@ParameterizedTest
	@CsvSource({ "2, Microphone, Wireless microphone., '', false", "1, microphone, Wireless microphone., '', false",
			"1, Microphone, wireless microphone., '', false", "1, Microphone, Wireless microphone., 'test', false",
			"1, Microphone, Wireless microphone., '', true" })
	void testEquals(int utilityId, String name, String description, String utilityLeases, boolean eq) {
		List<UtilityLease> utilityLeasesList = utilityLeases.equals("") ? null : new ArrayList<>();
		Utility newUtility = new Utility(utilityId, name, description, utilityLeasesList);

		assertEquals(eq, utility.equals(newUtility));
	}

}
