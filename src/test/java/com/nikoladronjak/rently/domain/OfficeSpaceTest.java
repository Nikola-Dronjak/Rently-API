package com.nikoladronjak.rently.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
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

class OfficeSpaceTest {

	Owner owner;

	List<String> photos;

	OfficeSpace officeSpace;

	private static Validator validator;

	@BeforeEach
	void setUp() throws Exception {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

		owner = new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");

		photos = new ArrayList<String>();
		photos.add("photo1");
		photos.add("photo2");
		photos.add("photo3");

		officeSpace = new OfficeSpace(1, "Office Space 1", "Jove Ilica 154", "A office space", (double) 400, 100, true,
				30, photos, owner, null, 50, null);
	}

	@AfterEach
	void tearDown() throws Exception {
		owner = null;

		photos = null;

		officeSpace = null;
	}

	@Test
	public void testCapacityNull() {
		officeSpace.setCapacity(null);

		Set<ConstraintViolation<OfficeSpace>> violations = validator.validate(officeSpace);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
				.anyMatch(violation -> violation.getMessage().equals("The size of the office space is required.")));
	}

	@Test
	public void testCapacityInvalid() {
		officeSpace.setCapacity(-1);

		Set<ConstraintViolation<OfficeSpace>> violations = validator.validate(officeSpace);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("The size of the office space has to be a positive value.")));
	}

	@Test
	public void testValidOfficeSpace() {
		Set<ConstraintViolation<OfficeSpace>> violations = validator.validate(officeSpace);
		assertTrue(violations.isEmpty());
	}

	@Test
	public void testEqualsSameObject() {
		OfficeSpace newOfficeSpace = officeSpace;

		assertTrue(officeSpace.equals(newOfficeSpace));
	}

	@Test
	public void testEqualsNull() {
		assertFalse(officeSpace.equals(null));
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEqualsDifferentTypes() {
		assertFalse(officeSpace.equals(new String("")));
	}

	@ParameterizedTest
	@CsvSource({
			"2, Office Space 1, Jove Ilica 154, A office space, 400.0, 100, true, 30, 'photo1,photo2,photo3', 'owner', '', 50, '', false",
			"1, Office Space 2, Jove Ilica 154, A office space, 400.0, 100, true, 30, 'photo1,photo2,photo3', 'owner', '', 50, '', false",
			"1, Office Space 1, Jove Ilica 155, A office space, 400.0, 100, true, 30, 'photo1,photo2,photo3', 'owner', '', 50, '', false",
			"1, Office Space 1, Jove Ilica 154, A Office space, 400.0, 100, true, 30, 'photo1,photo2,photo3', 'owner', '', 50, '', false",
			"1, Office Space 1, Jove Ilica 154, A office space, 500.0, 100, true, 30, 'photo1,photo2,photo3', 'owner', '', 50, '', false",
			"1, Office Space 1, Jove Ilica 154, A office space, 400.0, 200, true, 30, 'photo1,photo2,photo3', 'owner', '', 50, '', false",
			"1, Office Space 1, Jove Ilica 154, A office space, 400.0, 100, false, 30, 'photo1,photo2,photo3', 'owner', '', 50, '', false",
			"1, Office Space 1, Jove Ilica 154, A office space, 400.0, 100, true, 20, 'photo1,photo2,photo3', 'owner', '', 50, '', false",
			"1, Office Space 1, Jove Ilica 154, A office space, 400.0, 100, true, 30, 'photo1,photo2', 'owner', '', 50, '', false",
			"1, Office Space 1, Jove Ilica 154, A office space, 400.0, 100, true, 30, 'photo1,photo2,photo3', '', '', 50, '', false",
			"1, Office Space 1, Jove Ilica 154, A office space, 400.0, 100, true, 30, 'photo1,photo2,photo3', 'owner', 'test', 50, '', false",
			"1, Office Space 1, Jove Ilica 154, A office space, 400.0, 100, true, 30, 'photo1,photo2,photo3', 'owner', '', 60, '', false",
			"1, Office Space 1, Jove Ilica 154, A office space, 400.0, 100, true, 30, 'photo1,photo2,photo3', 'owner', '', 50, '', true" })
	void testEquals(int propertyId, String name, String address, String description, double rentalRate, int size,
			boolean isAvailable, int numberOfParkingSpots, String photos, String ownerInfo, String leases, int capacity,
			String utilityLeases, boolean eq) {
		List<String> photosList = photos.equals("") ? null : Arrays.asList(photos.split(","));
		Owner newOwner = ownerInfo.equals("") ? null
				: new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");
		List<Lease> leasesList = leases.equals("") ? null : new ArrayList<>();
		List<UtilityLease> utilityLeasesList = utilityLeases.equals("") ? null : new ArrayList<>();
		OfficeSpace newOfficeSpace = new OfficeSpace(propertyId, name, address, description, rentalRate, size,
				isAvailable, numberOfParkingSpots, photosList, newOwner, leasesList, capacity, utilityLeasesList);

		assertEquals(eq, officeSpace.equals(newOfficeSpace));
	}

}
