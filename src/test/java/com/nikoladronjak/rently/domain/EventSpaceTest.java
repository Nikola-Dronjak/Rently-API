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

class EventSpaceTest {

	Owner owner;

	List<String> photos;

	EventSpace eventSpace;

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

		eventSpace = new EventSpace(1, "Event Space 1", "Jove Ilica 154", "An event space", (double) 300, 200, true, 20,
				photos, owner, null, 50, true, true, null);
	}

	@AfterEach
	void tearDown() throws Exception {
		owner = null;

		photos = null;

		eventSpace = null;
	}

	@Test
	public void testCapacityNull() {
		eventSpace.setCapacity(null);

		Set<ConstraintViolation<EventSpace>> violations = validator.validate(eventSpace);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
				.anyMatch(violation -> violation.getMessage().equals("The size of the event space is required.")));
	}

	@Test
	public void testCapacityInvalid() {
		eventSpace.setCapacity(-1);

		Set<ConstraintViolation<EventSpace>> violations = validator.validate(eventSpace);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(
				violation -> violation.getMessage().equals("The size of the event space has to be a positive value.")));
	}

	@Test
	public void testHasKitchenNull() {
		eventSpace.setHasKitchen(null);

		Set<ConstraintViolation<EventSpace>> violations = validator.validate(eventSpace);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("You have to specify whether the event space has a kitchen or not.")));
	}

	@Test
	public void testHasBarNull() {
		eventSpace.setHasBar(null);

		Set<ConstraintViolation<EventSpace>> violations = validator.validate(eventSpace);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("You have to specify whether the event space has a bar or not.")));
	}

	@Test
	public void testValidEventSpace() {
		Set<ConstraintViolation<EventSpace>> violations = validator.validate(eventSpace);
		assertTrue(violations.isEmpty());
	}

	@Test
	public void testEqualsSameObject() {
		EventSpace newEventSpace = eventSpace;

		assertTrue(eventSpace.equals(newEventSpace));
	}

	@Test
	public void testEqualsNull() {
		assertFalse(eventSpace.equals(null));
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEqualsDifferentTypes() {
		assertFalse(eventSpace.equals(new String("")));
	}

	@ParameterizedTest
	@CsvSource({
			"2, Event Space 1, Jove Ilica 154, An event space, 300.0, 200, true, 20, 'photo1,photo2,photo3', 'owner', '', 50, true, true, '', false",
			"1, Event Space 2, Jove Ilica 154, An event space, 300.0, 200, true, 20, 'photo1,photo2,photo3', 'owner', '', 50, true, true, '', false",
			"1, Event Space 1, Jove Ilica 155, An event space, 300.0, 200, true, 20, 'photo1,photo2,photo3', 'owner', '', 50, true, true, '', false",
			"1, Event Space 1, Jove Ilica 154, An Event space, 300.0, 200, true, 20, 'photo1,photo2,photo3', 'owner', '', 50, true, true, '', false",
			"1, Event Space 1, Jove Ilica 154, An event space, 400.0, 200, true, 20, 'photo1,photo2,photo3', 'owner', '', 50, true, true, '', false",
			"1, Event Space 1, Jove Ilica 154, An event space, 300.0, 300, true, 20, 'photo1,photo2,photo3', 'owner', '', 50, true, true, '', false",
			"1, Event Space 1, Jove Ilica 154, An event space, 300.0, 200, false, 20, 'photo1,photo2,photo3', 'owner', '', 50, true, true, '', false",
			"1, Event Space 1, Jove Ilica 154, An event space, 300.0, 200, true, 10, 'photo1,photo2,photo3', 'owner', '', 50, true, true, '', false",
			"1, Event Space 1, Jove Ilica 154, An event space, 300.0, 200, true, 20, 'photo1,photo2', 'owner', '', 50, true, true, '', false",
			"1, Event Space 1, Jove Ilica 154, An event space, 300.0, 200, true, 20, 'photo1,photo2,photo3', '', '', 50, true, true, '', false",
			"1, Event Space 1, Jove Ilica 154, An event space, 300.0, 200, true, 20, 'photo1,photo2,photo3', 'owner', 'test', 50, true, true, '', false",
			"1, Event Space 1, Jove Ilica 154, An event space, 300.0, 200, true, 20, 'photo1,photo2,photo3', 'owner', '', 60, true, true, '', false",
			"1, Event Space 1, Jove Ilica 154, An event space, 300.0, 200, true, 20, 'photo1,photo2,photo3', 'owner', '', 50, false, true, '', false",
			"1, Event Space 1, Jove Ilica 154, An event space, 300.0, 200, true, 20, 'photo1,photo2,photo3', 'owner', '', 50, true, false, '', false",
			"1, Event Space 1, Jove Ilica 154, An event space, 300.0, 200, true, 20, 'photo1,photo2,photo3', 'owner', '', 50, true, true, 'test', false",
			"1, Event Space 1, Jove Ilica 154, An event space, 300.0, 200, true, 20, 'photo1,photo2,photo3', 'owner', '', 50, true, true, '', true" })
	void testEquals(int propertyId, String name, String address, String description, double rentalRate, int size,
			boolean isAvailable, int numberOfParkingSpots, String photos, String ownerInfo, String leases, int capacity,
			boolean hasKitchen, boolean hasBar, String utilityLeases, boolean eq) {
		List<String> photosList = photos.equals("") ? null : Arrays.asList(photos.split(","));
		Owner newOwner = ownerInfo.equals("") ? null
				: new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");
		List<Lease> leasesList = leases.equals("") ? null : new ArrayList<>();
		List<UtilityLease> utilityLeasesList = utilityLeases.equals("") ? null : new ArrayList<>();
		EventSpace newEventSpace = new EventSpace(propertyId, name, address, description, rentalRate, size, isAvailable,
				numberOfParkingSpots, photosList, newOwner, leasesList, capacity, hasKitchen, hasBar,
				utilityLeasesList);

		assertEquals(eq, eventSpace.equals(newEventSpace));
	}

}
