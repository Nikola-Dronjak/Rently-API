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

class UtilityLeaseTest {

	Utility utility;

	Owner owner;

	List<String> photos;

	OfficeSpace officeSpace;

	UtilityLease utilityLease;

	private static Validator validator;

	@BeforeEach
	void setUp() throws Exception {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

		utility = new Utility(1, "Microphone", "", null);

		owner = new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");

		photos = new ArrayList<String>();
		photos.add("photo1");
		photos.add("photo2");
		photos.add("photo3");

		officeSpace = new OfficeSpace(1, "Office Space 1", "Jove Ilica 154", "", (double) 300, 150, true, 30, photos,
				owner, null, 100, null);

		utilityLease = new UtilityLease(1, (double) 50, utility, officeSpace, null);
	}

	@AfterEach
	void tearDown() throws Exception {
		utility = null;

		officeSpace = null;

		owner = null;

		photos = null;

		utilityLease = null;
	}

	@Test
	public void testRentalRateNull() {
		utilityLease.setRentalRate(null);

		Set<ConstraintViolation<UtilityLease>> violations = validator.validate(utilityLease);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("The rental rate of the utility being leased is required.")));
	}

	@Test
	public void testRentalRateInvalid() {
		utilityLease.setRentalRate((double) -1);

		Set<ConstraintViolation<UtilityLease>> violations = validator.validate(utilityLease);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("The rental rate of the utility being leased has to be a positive value.")));
	}

	@Test
	public void testUtilityNull() {
		utilityLease.setUtility(null);

		Set<ConstraintViolation<UtilityLease>> violations = validator.validate(utilityLease);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(
				violation -> violation.getMessage().equals("You have to specify the utility which is being leased.")));
	}

	@Test
	public void testPropertyNull() {
		utilityLease.setProperty(null);

		Set<ConstraintViolation<UtilityLease>> violations = validator.validate(utilityLease);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("You have to specify the property for which the utility is being leased.")));
	}

	@Test
	public void testValidUtilityLease() {
		Set<ConstraintViolation<UtilityLease>> violations = validator.validate(utilityLease);
		assertTrue(violations.isEmpty());
	}

	@Test
	public void testEqualsSameObject() {
		UtilityLease newUtilityLease = utilityLease;

		assertTrue(utilityLease.equals(newUtilityLease));
	}

	@Test
	public void testEqualsNull() {
		assertFalse(utilityLease.equals(null));
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEqualsDifferentTypes() {
		assertFalse(utilityLease.equals(new String("")));
	}

	@ParameterizedTest
	@CsvSource({ "2, 50, 'utility', 'officeSpace', '', false", "1, 60, 'utility', 'officeSpace', '', false",
			"1, 50, '', 'officeSpace', '', false", "1, 50, 'utility', '', '', false",
			"1, 50, 'utility', 'officeSpace', 'test', false", "1, 50, 'utility', 'officeSpace', '', true" })
	void testEquals(int utilityLeaseId, double rentalRate, String utilityInfo, String propertyInfro, String rents,
			boolean eq) {
		Utility newUtility = utilityInfo.equals("") ? null : new Utility(1, "Microphone", "", null);
		OfficeSpace newOfficeSpace = propertyInfro.equals("") ? null
				: new OfficeSpace(1, "Office Space 1", "Jove Ilica 154", "", (double) 300, 150, true, 30, photos, owner,
						null, 100, null);
		List<Rent> rentsList = rents.equals("") ? null : new ArrayList<>();
		UtilityLease newUtilityLease = new UtilityLease(utilityLeaseId, rentalRate, newUtility, newOfficeSpace,
				rentsList);

		assertEquals(eq, utilityLease.equals(newUtilityLease));
	}

}
