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

class PropertyTest {

	Owner owner;

	List<String> photos;

	Property property;

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

		property = new Property(1, "Property 1", "Jove Ilica 154", "A property", (double) 500, 100, true, 0, photos,
				owner, null);
	}

	@AfterEach
	void tearDown() throws Exception {
		owner = null;

		photos = null;

		property = null;
	}

	@Test
	public void testNameNull() {
		property.setName(null);

		Set<ConstraintViolation<Property>> violations = validator.validate(property);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
				.anyMatch(violation -> violation.getMessage().equals("The name of the property is required.")));
	}

	@Test
	public void testNameLength() {
		property.setName("A");

		Set<ConstraintViolation<Property>> violations = validator.validate(property);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("The name of the property has to have at least 5 characters.")));
	}

	@Test
	public void testAddressNull() {
		property.setAddress(null);

		Set<ConstraintViolation<Property>> violations = validator.validate(property);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
				.anyMatch(violation -> violation.getMessage().equals("The address of the property is required.")));
	}

	@Test
	public void testAddressLength() {
		property.setAddress("A");

		Set<ConstraintViolation<Property>> violations = validator.validate(property);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("The address of the property has to have at least 5 characters.")));
	}

	@Test
	public void testDescpiptionNull() {
		property.setDescription(null);

		Set<ConstraintViolation<Property>> violations = validator.validate(property);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
				.anyMatch(violation -> violation.getMessage().equals("The description of the property is required.")));
	}

	@Test
	public void testRentalRateNull() {
		property.setRentalRate(null);

		Set<ConstraintViolation<Property>> violations = validator.validate(property);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
				.anyMatch(violation -> violation.getMessage().equals("The rental rate of the property is required.")));
	}

	@Test
	public void testRentalRateNotPositive() {
		property.setRentalRate((double) 0);

		Set<ConstraintViolation<Property>> violations = validator.validate(property);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("The rental rate of the property has to be a positive value.")));
	}

	@Test
	public void testSizeNull() {
		property.setSize(null);

		Set<ConstraintViolation<Property>> violations = validator.validate(property);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
				.anyMatch(violation -> violation.getMessage().equals("The size of the property is required.")));
	}

	@Test
	public void testSizeNotPositive() {
		property.setSize(0);

		Set<ConstraintViolation<Property>> violations = validator.validate(property);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(
				violation -> violation.getMessage().equals("The size of the property has to be a positive value.")));
	}

	@Test
	public void testIsAvailableNull() {
		property.setAvailable(null);

		Set<ConstraintViolation<Property>> violations = validator.validate(property);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("You have to specify wether the property is available or not.")));
	}

	@Test
	public void testNumberOfParkingSpotsNull() {
		property.setNumberOfParkingSpots(null);

		Set<ConstraintViolation<Property>> violations = validator.validate(property);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("The number of parking spots for the property is required.")));
	}

	@Test
	public void testNumberOfParkingSpotsInvalid() {
		property.setNumberOfParkingSpots(-1);

		Set<ConstraintViolation<Property>> violations = validator.validate(property);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("The number of parking spots for the property has to be a positive value.")));
	}

	@Test
	public void testPhotosNull() {
		property.setPhotos(null);

		Set<ConstraintViolation<Property>> violations = validator.validate(property);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
				.anyMatch(violation -> violation.getMessage().equals("The photos of the property are required.")));
	}

	@Test
	public void testPhotosInvalidAmount() {
		photos.add("photo4");
		photos.add("photo5");
		photos.add("photo6");
		photos.add("photo7");
		photos.add("photo8");
		photos.add("photo9");
		photos.add("photo10");
		photos.add("photo11");
		photos.add("photo12");
		photos.add("photo13");
		photos.add("photo14");
		photos.add("photo15");
		photos.add("photo16");

		property.setPhotos(photos);

		Set<ConstraintViolation<Property>> violations = validator.validate(property);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage().equals(
				"There has to be atleast 1 photo of the property and there cant be more than 15 photos of the property.")));
	}

	@Test
	public void testOwnerNull() {
		property.setOwner(null);

		Set<ConstraintViolation<Property>> violations = validator.validate(property);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(
				violation -> violation.getMessage().equals("You have to specify the owner of the property.")));
	}

	@Test
	public void testValidProperty() {
		Set<ConstraintViolation<Property>> violations = validator.validate(property);
		assertTrue(violations.isEmpty());
	}

	@Test
	public void testEqualsSameObject() {
		Property newProperty = property;

		assertTrue(property.equals(newProperty));
	}

	@Test
	public void testEqualsNull() {
		assertFalse(property.equals(null));
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEqualsDifferentTypes() {
		assertFalse(property.equals(new String("")));
	}

	@ParameterizedTest
	@CsvSource({
			"2, Property 1, Jove Ilica 154, A property, 500.0, 100, true, 0, 'photo1,photo2,photo3', 'owner', '', false",
			"1, Property 2, Jove Ilica 154, A property, 500.0, 100, true, 0, 'photo1,photo2,photo3', 'owner', '', false",
			"1, Property 1, Jove Ilica 155, A property, 500.0, 100, true, 0, 'photo1,photo2,photo3', 'owner', '', false",
			"1, Property 1, Jove Ilica 154, A Property, 500.0, 100, true, 0, 'photo1,photo2,photo3', 'owner', '', false",
			"1, Property 1, Jove Ilica 154, A property, 400.0, 100, true, 0, 'photo1,photo2,photo3', 'owner', '', false",
			"1, Property 1, Jove Ilica 154, A property, 500.0, 200, true, 0, 'photo1,photo2,photo3', 'owner', '', false",
			"1, Property 1, Jove Ilica 154, A property, 500.0, 100, false, 0, 'photo1,photo2,photo3', 'owner', '', false",
			"1, Property 1, Jove Ilica 154, A property, 500.0, 100, true, 10, 'photo1,photo2,photo3', 'owner', '', false",
			"1, Property 1, Jove Ilica 154, A property, 500.0, 100, true, 0, 'photo1,photo2', 'owner', '', false",
			"1, Property 1, Jove Ilica 154, A property, 500.0, 100, true, 0, 'photo1,photo2,photo3', '', '', false",
			"1, Property 1, Jove Ilica 154, A property, 500.0, 100, true, 0, 'photo1,photo2,photo3', 'owner', 'test', false",
			"1, Property 1, Jove Ilica 154, A property, 500.0, 100, true, 0, 'photo1,photo2,photo3', 'owner', '', true" })
	void testEquals(int propertyId, String name, String address, String description, double rentalRate, int size,
			boolean isAvailable, int numberOfParkingSpots, String photos, String ownerInfo, String leases, boolean eq) {
		List<String> photosList = photos.equals("") ? null : Arrays.asList(photos.split(","));
		Owner newOwner = ownerInfo.equals("") ? null
				: new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");
		List<Lease> leasesList = leases.equals("") ? null : new ArrayList<>();
		Property newProperty = new Property(propertyId, name, address, description, rentalRate, size, isAvailable,
				numberOfParkingSpots, photosList, newOwner, leasesList);

		assertEquals(eq, property.equals(newProperty));
	}

}
