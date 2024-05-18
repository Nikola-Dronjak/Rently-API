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

class ResidenceTest {

	Owner owner;

	List<String> photos;

	Residence residence;

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

		residence = new Residence(1, "Apartement 1", "Jove Ilica 154", "An apartement", (double) 500, 100, true, 0,
				photos, owner, null, 1, 1, HeatingType.Central, true, true);
	}

	@AfterEach
	void tearDown() throws Exception {
		owner = null;

		photos = null;

		residence = null;
	}

	@Test
	public void testNumberOfBedroomsNull() {
		residence.setNumberOfBedrooms(null);

		Set<ConstraintViolation<Property>> violations = validator.validate(residence);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(
				violation -> violation.getMessage().equals("The number of bedrooms for the residence is required.")));
	}

	@Test
	public void testNumberOfBedroomsInvalid() {
		residence.setNumberOfBedrooms(-1);

		Set<ConstraintViolation<Property>> violations = validator.validate(residence);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
				.anyMatch(violation -> violation.getMessage().equals("The residence has to have at least 1 bedroom.")));
	}

	@Test
	public void testNumberOfBathroomsNull() {
		residence.setNumberOfBathrooms(null);

		Set<ConstraintViolation<Property>> violations = validator.validate(residence);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(
				violation -> violation.getMessage().equals("The number of bathrooms for the residence is required.")));
	}

	@Test
	public void testNumberOfBathroomsInvalid() {
		residence.setNumberOfBathrooms(-1);

		Set<ConstraintViolation<Property>> violations = validator.validate(residence);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(
				violation -> violation.getMessage().equals("The residence has to have at least 1 bathroom.")));
	}

	@Test
	public void testHeatingTypeNull() {
		residence.setHeatingType(null);

		Set<ConstraintViolation<Property>> violations = validator.validate(residence);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(
				violation -> violation.getMessage().equals("The heating type of the residence is required.")));
	}

	@Test
	public void testIsPetFriendlyNull() {
		residence.setPetFriendly(null);

		Set<ConstraintViolation<Property>> violations = validator.validate(residence);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("You have to specify whether the property is pet friendly or not.")));
	}

	@Test
	public void testIsFurnishedNull() {
		residence.setFurnished(null);

		Set<ConstraintViolation<Property>> violations = validator.validate(residence);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("You have to specify whether the property is furnished or not.")));
	}

	@Test
	public void testValidResidence() {
		Set<ConstraintViolation<Property>> violations = validator.validate(residence);
		assertTrue(violations.isEmpty());
	}

	@Test
	public void testEqualsSameObject() {
		Residence newResidence = residence;

		assertTrue(residence.equals(newResidence));
	}

	@Test
	public void testEqualsNull() {
		assertFalse(residence.equals(null));
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEqualsDifferentTypes() {
		assertFalse(residence.equals(new String("")));
	}

	@ParameterizedTest
	@CsvSource({
			"2, Apartement 1, Jove Ilica 154, An apartement, 500.0, 100, true, 0, 'photo1,photo2,photo3', 'owner', '', 1, 1, 'Central', true, true, false",
			"1, Apartement 2, Jove Ilica 154, An apartement, 500.0, 100, true, 0, 'photo1,photo2,photo3', 'owner', '', 1, 1, 'Central', true, true, false",
			"1, Apartement 1, Jove Ilica 155, An apartement, 500.0, 100, true, 0, 'photo1,photo2,photo3', 'owner', '', 1, 1, 'Central', true, true, false",
			"1, Apartement 1, Jove Ilica 154, An Apartement, 500.0, 100, true, 0, 'photo1,photo2,photo3', 'owner', '', 1, 1, 'Central', true, true, false",
			"1, Apartement 1, Jove Ilica 154, An apartement, 400.0, 100, true, 0, 'photo1,photo2,photo3', 'owner', '', 1, 1, 'Central', true, true, false",
			"1, Apartement 1, Jove Ilica 154, An apartement, 500.0, 200, true, 0, 'photo1,photo2,photo3', 'owner', '', 1, 1, 'Central', true, true, false",
			"1, Apartement 1, Jove Ilica 154, An apartement, 500.0, 100, false, 0, 'photo1,photo2,photo3', 'owner', '', 1, 1, 'Central', true, true, false",
			"1, Apartement 1, Jove Ilica 154, An apartement, 500.0, 100, true, 10, 'photo1,photo2,photo3', 'owner', '', 1, 1, 'Central', true, true, false",
			"1, Apartement 1, Jove Ilica 154, An apartement, 500.0, 100, true, 0, 'photo1,photo2', 'owner', '', 1, 1, 'Central', true, true, false",
			"1, Apartement 1, Jove Ilica 154, An apartement, 500.0, 100, true, 0, 'photo1,photo2,photo3', '', '', 1, 1, 'Central', true, true, false",
			"1, Apartement 1, Jove Ilica 154, An apartement, 500.0, 100, true, 0, 'photo1,photo2,photo3', 'owner', 'test', 1, 1, 'Central', true, true, false",
			"1, Apartement 1, Jove Ilica 154, An apartement, 500.0, 100, true, 0, 'photo1,photo2,photo3', 'owner', '', 2, 1, 'Central', true, true, false",
			"1, Apartement 1, Jove Ilica 154, An apartement, 500.0, 100, true, 0, 'photo1,photo2,photo3', 'owner', '', 1, 2, 'Central', true, true, false",
			"1, Apartement 1, Jove Ilica 154, An apartement, 500.0, 100, true, 0, 'photo1,photo2,photo3', 'owner', '', 1, 1, 'Gas', true, true, false",
			"1, Apartement 1, Jove Ilica 154, An apartement, 500.0, 100, true, 0, 'photo1,photo2,photo3', 'owner', '', 1, 1, 'Central', false, true, false",
			"1, Apartement 1, Jove Ilica 154, An apartement, 500.0, 100, true, 0, 'photo1,photo2,photo3', 'owner', '', 1, 1, 'Central', true, false, false",
			"1, Apartement 1, Jove Ilica 154, An apartement, 500.0, 100, true, 0, 'photo1,photo2,photo3', 'owner', '', 1, 1, 'Central', true, true, true" })
	void testEquals(int propertyId, String name, String address, String description, double rentalRate, int size,
			boolean isAvailable, int numberOfParkingSpots, String photos, String ownerInfo, String leases,
			int numberOfBedrooms, int numberOfBathrooms, String heatingType, boolean isPetFriendly, boolean isFurnished,
			boolean eq) {
		List<String> photosList = photos.equals("") ? null : Arrays.asList(photos.split(","));
		Owner newOwner = ownerInfo.equals("") ? null
				: new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");
		List<Lease> leasesList = leases.equals("") ? null : new ArrayList<>();
		HeatingType heatingTypeEnum = HeatingType.valueOf(heatingType);
		Residence newResidence = new Residence(propertyId, name, address, description, rentalRate, size, isAvailable,
				numberOfParkingSpots, photosList, newOwner, leasesList, numberOfBedrooms, numberOfBathrooms,
				heatingTypeEnum, isPetFriendly, isFurnished);

		assertEquals(eq, residence.equals(newResidence));
	}

}
