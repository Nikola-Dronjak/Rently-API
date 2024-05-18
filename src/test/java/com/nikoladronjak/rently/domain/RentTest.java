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
import jakarta.validation.ValidatorFactory;;

class RentTest {

	Utility utility1;

	Utility utility2;

	Owner owner;

	List<String> photos;

	Customer customer;

	OfficeSpace officeSpace;

	Lease lease;

	UtilityLease utilityLease1;

	UtilityLease utilityLease2;

	List<UtilityLease> utilityLeases;

	Rent rent;

	private static Validator validator;

	@BeforeEach
	void setUp() throws Exception {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

		utility1 = new Utility(1, "Microphone", "", null);
		utility2 = new Utility(2, "Projector", "", null);

		owner = new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");

		photos = new ArrayList<String>();
		photos.add("photo1");
		photos.add("photo2");
		photos.add("photo3");

		customer = new Customer(1, "Mika", "Mikic", "mika@gmail.com", "mika123", null);

		officeSpace = new OfficeSpace(1, "Office Space 1", "Jove Ilica 154", "", (double) 300, 150, true, 30, photos,
				owner, null, 100, null);

		lease = new Lease(1, 200, new GregorianCalendar(2024, 11, 12), new GregorianCalendar(2025, 11, 12), officeSpace,
				customer, null);

		utilityLease1 = new UtilityLease(1, (double) 40, utility1, officeSpace, null);
		utilityLease2 = new UtilityLease(2, (double) 60, utility2, officeSpace, null);

		utilityLeases = new ArrayList<UtilityLease>();
		utilityLeases.add(utilityLease1);
		utilityLeases.add(utilityLease2);

		rent = new Rent(1, 300, utilityLeases, lease);
	}

	@AfterEach
	void tearDown() throws Exception {
		utility1 = null;
		utility2 = null;

		owner = null;

		photos = null;

		customer = null;

		officeSpace = null;

		lease = null;

		utilityLease1 = null;
		utilityLease2 = null;

		utilityLeases = null;

		rent = null;
	}

	@Test
	public void testLeaseNull() {
		rent.setLease(null);

		Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("You have to specify the lease from which the rent is derived.")));
	}

	@Test
	public void testUtilityLeasesNull() {
		rent.setUtilityLeases(null);

		Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
		assertFalse(violations.isEmpty());
		assertTrue(violations.stream().anyMatch(violation -> violation.getMessage()
				.equals("You have to specify the utility leases which are part of the rent.")));
	}

	@Test
	public void testValidLease() {
		Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
		assertTrue(violations.isEmpty());
	}

	@Test
	public void testEqualsSameObject() {
		Rent newRent = rent;

		assertTrue(rent.equals(newRent));
	}

	@Test
	public void testEqualsNull() {
		assertFalse(rent.equals(null));
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEqualsDifferentTypes() {
		assertFalse(rent.equals(new String("")));
	}

	@ParameterizedTest
	@CsvSource({ "2, 300, 'utilityLeases', 'lease', false", "1, 200, 'utilityLeases', 'lease', false",
			"1, 300, '', 'lease', false", "1, 300, 'utilityLeases', '', false",
			"1, 300, 'utilityLeases', 'lease', true", })
	void testEquals(int rentId, double totalRentalRate, String utilityLeases, String leaseInfo, boolean eq) {
		List<UtilityLease> utilityLeasesList = utilityLeases.equals("") ? null : this.utilityLeases;
		Lease newLease = leaseInfo.equals("") ? null
				: new Lease(1, 200, new GregorianCalendar(2024, 11, 12), new GregorianCalendar(2025, 11, 12),
						officeSpace, customer, null);
		Rent newRent = new Rent(rentId, totalRentalRate, utilityLeasesList, newLease);

		assertEquals(eq, rent.equals(newRent));
	}

}
