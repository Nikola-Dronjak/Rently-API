package com.nikoladronjak.rently.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.GregorianCalendar;
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

class LeaseTest {

	Owner owner;

	List<String> photos;

	Residence residence;

	Customer customer;

	Lease lease;

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

		residence = new Residence(1, "Lux Apartment", "Jove Ilica 154", "", (double) 400, 70, true, 2, photos, owner,
				null, 2, 2, HeatingType.Central, true, true);

		customer = new Customer(1, "Mika", "Mikic", "mika@gmail.com", "mika123", null);

		lease = new Lease(1, 400, new GregorianCalendar(2025, 10, 15), new GregorianCalendar(2025, 11, 31), residence,
				customer, null);
	}

	@AfterEach
	void tearDown() throws Exception {
		owner = null;

		photos = null;

		residence = null;

		customer = null;

		lease = null;
	}

	@Test
	public void testStartDateNull() {
		lease.setStartDate(null);

		Set<ConstraintViolation<Lease>> violations = validator.validate(lease);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
				.anyMatch(violation -> violation.getMessage().equals("The start date of the lease is required.")));
	}

	@Test
	public void testStartDatePast() {
		lease.setStartDate(new GregorianCalendar(2020, 1, 1));

		Set<ConstraintViolation<Lease>> violations = validator.validate(lease);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("The start date of the lease has to be in the present or in the future.")));
	}

	@Test
	public void testEndDateNull() {
		lease.setEndDate(null);

		Set<ConstraintViolation<Lease>> violations = validator.validate(lease);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream()
				.anyMatch(violation -> violation.getMessage().equals("The end date of the lease is required.")));
	}

	@Test
	public void testEndDatePast() {
		lease.setEndDate(new GregorianCalendar(2020, 1, 1));

		Set<ConstraintViolation<Lease>> violations = validator.validate(lease);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("The end date of the lease has to be in the present or in the future.")));
	}

	@Test
	public void testPropertyNull() {
		lease.setProperty(null);

		Set<ConstraintViolation<Lease>> violations = validator.validate(lease);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(
				violation -> violation.getMessage().equals("You have to specify the property which is being leased.")));
	}

	@Test
	public void testCustomerNull() {
		lease.setCustomer(null);

		Set<ConstraintViolation<Lease>> violations = validator.validate(lease);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("You have to specify the customer who is leasing the property.")));
	}

	@Test
	public void testValidLease() {
		Set<ConstraintViolation<Lease>> violations = validator.validate(lease);
		assertTrue(violations.isEmpty());
	}

	@Test
	public void testEqualsSameObject() {
		Lease newLease = lease;

		assertTrue(lease.equals(newLease));
	}

	@Test
	public void testEqualsNull() {
		assertFalse(lease.equals(null));
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEqualsDifferentTypes() {
		assertFalse(lease.equals(new String("")));
	}

	@ParameterizedTest
	@CsvSource({ "2, 400, 2025-10-15, 2025-11-31, 'residence', 'customer', '', false",
			"1, 300, 2025-10-15, 2025-11-31, 'residence', 'customer', '', false",
			"1, 400, 2025-1-1, 2025-11-31, 'residence', 'customer', '', false",
			"1, 400, 2025-10-15, 2025-1-2, 'residence', 'customer', '', false",
			"1, 400, 2025-10-15, 2025-11-31, '', 'customer', '', false",
			"1, 400, 2025-10-15, 2025-11-31, 'residence', '', '', false",
			"1, 400, 2025-10-15, 2025-11-31, 'residence', 'customer', 'test', false",
			"1, 400, 2025-10-15, 2025-11-31, 'residence', 'customer', '', true" })
	void testEquals(int leaseId, double rentalRate, String start, String end, String residenceInfo, String customerInfo,
			String rents, boolean eq) {
		Residence newResidence = residenceInfo.equals("") ? null
				: new Residence(1, "Lux Apartment", "Jove Ilica 154", "", (double) 400, 70, true, 2, photos, owner,
						null, 2, 2, HeatingType.Central, true, true);
		Customer newCustomer = customerInfo.equals("") ? null
				: new Customer(1, "Mika", "Mikic", "mika@gmail.com", "mika123", null);
		GregorianCalendar startDate = start.equals("2025-10-15") ? new GregorianCalendar(2025, 10, 15)
				: new GregorianCalendar(2025, 1, 1);
		GregorianCalendar endDate = end.equals("2025-11-31") ? new GregorianCalendar(2025, 11, 31)
				: new GregorianCalendar(2025, 1, 2);
		List<Rent> rentsList = rents.equals("") ? null : new ArrayList<>();
		Lease newLease = new Lease(leaseId, rentalRate, startDate, endDate, newResidence, newCustomer, rentsList);

		assertEquals(eq, lease.equals(newLease));
	}

}
