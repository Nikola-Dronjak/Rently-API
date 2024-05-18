package com.nikoladronjak.rently.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import com.nikoladronjak.rently.domain.Customer;
import com.nikoladronjak.rently.domain.Lease;
import com.nikoladronjak.rently.domain.OfficeSpace;
import com.nikoladronjak.rently.domain.Owner;
import com.nikoladronjak.rently.domain.Rent;
import com.nikoladronjak.rently.domain.Utility;
import com.nikoladronjak.rently.domain.UtilityLease;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RentRepositoryTest {

	Utility utility1;

	Utility utility2;

	Owner owner;

	List<String> photos;

	Customer customer;

	OfficeSpace officeSpace1;

	OfficeSpace officeSpace2;

	Lease lease1;

	Lease lease2;

	UtilityLease utilityLease1;

	UtilityLease utilityLease2;

	Rent rent1;

	Rent rent2;

	@Autowired
	private UtilityRepository utilityRepository;

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private OfficeSpaceRepository officeSpaceRepository;

	@Autowired
	private LeaseRepository leaseRepository;

	@Autowired
	private UtilityLeaseRepository utilityLeaseRepository;

	@Autowired
	private RentRepository rentRepository;

	@BeforeEach
	void setUp() throws Exception {

		utility1 = new Utility(1, "Microphone", "", null);
		utility2 = new Utility(2, "Projector", "", null);

		owner = new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");

		photos = new ArrayList<String>();
		photos.add("photo1");
		photos.add("photo2");
		photos.add("photo3");

		customer = new Customer(1, "Mika", "Mikic", "mika@gmail.com", "mika123", null);

		officeSpace1 = new OfficeSpace(1, "Office Space 1", "Jove Ilica 154", "", (double) 300, 150, true, 30, photos,
				owner, null, 100, null);
		officeSpace2 = new OfficeSpace(2, "Office Space 2", "Studentski trg 1", "", (double) 250, 120, true, 20, photos,
				owner, null, 90, null);

		lease1 = new Lease(1, 200, new GregorianCalendar(2024, 11, 12), new GregorianCalendar(2025, 11, 12),
				officeSpace1, customer, null);
		lease2 = new Lease(2, 250, new GregorianCalendar(2024, 11, 12), new GregorianCalendar(2025, 11, 12),
				officeSpace2, customer, null);

		utilityLease1 = new UtilityLease(1, (double) 40, utility1, officeSpace1, null);
		utilityLease2 = new UtilityLease(2, (double) 60, utility2, officeSpace1, null);

		List<UtilityLease> utilityLeases = new ArrayList<UtilityLease>();
		utilityLeases.add(utilityLease1);
		utilityLeases.add(utilityLease2);

		rent1 = new Rent(1, 300, utilityLeases, lease1);
		rent2 = new Rent(2, 350, utilityLeases, lease2);

		utilityRepository.save(utility1);
		utilityRepository.save(utility2);
		ownerRepository.save(owner);
		customerRepository.save(customer);
		officeSpaceRepository.save(officeSpace1);
		officeSpaceRepository.save(officeSpace2);
		leaseRepository.save(lease1);
		leaseRepository.save(lease2);
		utilityLeaseRepository.save(utilityLease1);
		utilityLeaseRepository.save(utilityLease2);
	}

	@AfterEach
	void tearDown() throws Exception {
		utility1 = null;
		utility2 = null;

		owner = null;

		photos = null;

		customer = null;

		officeSpace1 = null;
		officeSpace2 = null;

		lease1 = null;
		lease2 = null;

		utilityLease1 = null;
		utilityLease2 = null;

		rent1 = null;
		rent2 = null;
	}

	@Test
	void testFindAllEmpty() {
		List<Rent> rents = rentRepository.findAll();

		assertEquals(0, rents.size());
	}

	@Test
	void testFindAll() {
		rentRepository.save(rent1);
		rentRepository.save(rent2);

		List<Rent> rents = rentRepository.findAll();

		assertEquals(2, rents.size());
		assertEquals(rent1, rents.get(0));
		assertEquals(rent2, rents.get(1));
	}

	@Test
	void testFindAllByLease_LeaseIdEmpty() {
		rentRepository.save(rent2);

		List<Rent> rents = rentRepository.findAllByLease_LeaseId(1);

		assertEquals(0, rents.size());
	}

	@Test
	void testFindAllByLease_LeaseId() {
		rentRepository.save(rent1);
		rentRepository.save(rent2);

		List<Rent> rents = rentRepository.findAllByLease_LeaseId(1);

		assertEquals(1, rents.size());
		assertEquals(rent1, rents.get(0));
	}

	@Test
	void testFindByIdBadId() {
		rentRepository.save(rent1);

		Optional<Rent> rent = rentRepository.findById(2);

		assertTrue(rent.isEmpty());
	}

	@Test
	void testFindById() {
		rentRepository.save(rent1);

		Optional<Rent> rent = rentRepository.findById(1);

		assertTrue(rent.isPresent());
		assertEquals(rent1, rent.get());
	}

	@Test
	void testFindByLease_LeaseIdBadId() {
		rentRepository.save(rent1);

		Optional<Rent> rent = rentRepository.findByLease_LeaseId(2);

		assertTrue(rent.isEmpty());
	}

	@Test
	void testFindByLease_LeaseId() {
		rentRepository.save(rent1);

		Optional<Rent> rent = rentRepository.findByLease_LeaseId(1);

		assertTrue(rent.isPresent());
		assertEquals(rent1, rent.get());
	}

	@Test
	void testSave() {
		Rent savedRent = rentRepository.save(rent1);

		assertEquals(rent1, savedRent);
	}

	@Test
	void testDeleteById() {
		rentRepository.save(rent1);

		rentRepository.deleteById(1);

		Optional<Rent> deletedRent = rentRepository.findById(1);
		assertFalse(deletedRent.isPresent());
	}

}
