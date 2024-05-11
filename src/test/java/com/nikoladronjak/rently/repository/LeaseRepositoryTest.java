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
import com.nikoladronjak.rently.domain.HeatingType;
import com.nikoladronjak.rently.domain.Lease;
import com.nikoladronjak.rently.domain.Owner;
import com.nikoladronjak.rently.domain.Residence;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LeaseRepositoryTest {

	Residence residence1;

	Residence residence2;

	Owner owner;

	Customer customer;

	Lease lease1;

	Lease lease2;

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ResidenceRepository residenceRepository;

	@Autowired
	private LeaseRepository leaseRepository;

	@BeforeEach
	void setUp() throws Exception {
		List<String> photos = new ArrayList<String>();
		photos.add("photo1");
		photos.add("photo2");

		residence1 = new Residence(1, "Lux Apartment", "Jove Ilica 154", "", 400, 70, true, 2, photos, owner, null, 2,
				2, HeatingType.Central, true, true);
		residence2 = new Residence(2, "Lux Apartment", "Studentski trg 1", "", 300, 60, true, 2, photos, owner, null, 2,
				1, HeatingType.Central, true, true);

		owner = new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");

		customer = new Customer(1, "Mika", "Mikic", "mika@gmail.com", "mika123", null);

		lease1 = new Lease(1, 400, new GregorianCalendar(), new GregorianCalendar(2025, 11, 31), residence1, customer,
				null);
		lease2 = new Lease(2, 400, new GregorianCalendar(), new GregorianCalendar(2025, 11, 31), residence2, customer,
				null);

		ownerRepository.save(owner);
		residenceRepository.save(residence1);
		residenceRepository.save(residence2);
		customerRepository.save(customer);
	}

	@AfterEach
	void tearDown() throws Exception {
		residence1 = null;
		residence2 = null;

		owner = null;

		customer = null;

		lease1 = null;
		lease2 = null;
	}

	@Test
	void testFindAllEmpty() {
		List<Lease> leases = leaseRepository.findAll();

		assertEquals(0, leases.size());
	}

	@Test
	void testFindAll() {
		leaseRepository.save(lease1);
		leaseRepository.save(lease2);

		List<Lease> leases = leaseRepository.findAll();

		assertEquals(2, leases.size());
		assertEquals(lease1, leases.get(0));
		assertEquals(lease2, leases.get(1));
	}

	@Test
	void testFindAllByProperty_PropertyIdEmpty() {
		List<Lease> leases = leaseRepository.findAllByProperty_PropertyId(1);

		assertEquals(0, leases.size());
	}

	@Test
	void testFindAllByProperty_PropertyId() {
		leaseRepository.save(lease1);
		leaseRepository.save(lease2);

		List<Lease> leases = leaseRepository.findAllByProperty_PropertyId(1);

		assertEquals(1, leases.size());
		assertEquals(lease1, leases.get(0));
	}

	@Test
	void testFindAllByCustomer_CustomerIdEmpty() {
		List<Lease> leases = leaseRepository.findAllByCustomer_CustomerId(1);

		assertEquals(0, leases.size());
	}

	@Test
	void testFindAllByCustomer_CustomerId() {
		leaseRepository.save(lease1);
		leaseRepository.save(lease2);

		List<Lease> leases = leaseRepository.findAllByCustomer_CustomerId(1);

		assertEquals(2, leases.size());
		assertEquals(lease1, leases.get(0));
		assertEquals(lease2, leases.get(1));
	}

	@Test
	void testFindByIdBadId() {
		leaseRepository.save(lease1);

		Optional<Lease> lease = leaseRepository.findById(2);

		assertTrue(lease.isEmpty());
	}

	@Test
	void testFindById() {
		leaseRepository.save(lease1);

		Optional<Lease> lease = leaseRepository.findById(1);

		assertTrue(lease.isPresent());
		assertEquals(lease1, lease.get());
	}

	@Test
	void testFindByProperty_PropertyIdAndCustomer_CustomerIdBadPropertyId() {
		leaseRepository.save(lease1);

		Optional<Lease> lease = leaseRepository.findByProperty_PropertyIdAndCustomer_CustomerId(2, 1);

		assertTrue(lease.isEmpty());
	}

	@Test
	void testFindByProperty_PropertyIdAndCustomer_CustomerIdBadCustomerId() {
		leaseRepository.save(lease1);

		Optional<Lease> lease = leaseRepository.findByProperty_PropertyIdAndCustomer_CustomerId(1, 2);

		assertTrue(lease.isEmpty());
	}

	@Test
	void testFindByUtility_UtilityIdAndProperty_PropertyId() {
		leaseRepository.save(lease1);

		Optional<Lease> lease = leaseRepository.findByProperty_PropertyIdAndCustomer_CustomerId(1, 1);

		assertTrue(lease.isPresent());
		assertEquals(lease1, lease.get());
	}

	@Test
	void testSave() {
		Lease savedLease = leaseRepository.save(lease1);

		assertEquals(lease1, savedLease);
	}

	@Test
	void testDeleteById() {
		leaseRepository.save(lease1);

		leaseRepository.deleteById(1);

		Optional<Lease> deletedLease = leaseRepository.findById(1);
		assertFalse(deletedLease.isPresent());
	}

}
