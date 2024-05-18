package com.nikoladronjak.rently.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import com.nikoladronjak.rently.domain.OfficeSpace;
import com.nikoladronjak.rently.domain.Owner;
import com.nikoladronjak.rently.domain.Utility;
import com.nikoladronjak.rently.domain.UtilityLease;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UtilityLeaseRepositoryTest {

	Utility utility1;

	Utility utility2;

	Owner owner;

	List<String> photos;

	OfficeSpace officeSpace1;

	OfficeSpace officeSpace2;

	UtilityLease utilityLease1;

	UtilityLease utilityLease2;

	UtilityLease utilityLease3;

	UtilityLease utilityLease4;

	@Autowired
	private UtilityRepository utilityRepository;

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private OfficeSpaceRepository officeSpaceRepository;

	@Autowired
	private UtilityLeaseRepository utilityLeaseRepository;

	@BeforeEach
	void setUp() throws Exception {

		utility1 = new Utility(1, "Microphone", "", null);
		utility2 = new Utility(2, "Projector", "", null);

		owner = new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");

		photos = new ArrayList<String>();
		photos.add("photo1");
		photos.add("photo2");
		photos.add("photo3");

		officeSpace1 = new OfficeSpace(1, "Office Space 1", "Jove Ilica 154", "", (double) 300, 150, true, 30, photos,
				owner, null, 100, null);
		officeSpace2 = new OfficeSpace(2, "Office Space 2", "Studentski trg 1", "", (double) 250, 120, true, 20, photos,
				owner, null, 90, null);

		utilityLease1 = new UtilityLease(1, (double) 50, utility1, officeSpace1, null);
		utilityLease2 = new UtilityLease(2, (double) 60, utility2, officeSpace1, null);
		utilityLease3 = new UtilityLease(3, (double) 50, utility1, officeSpace2, null);
		utilityLease4 = new UtilityLease(4, (double) 60, utility2, officeSpace2, null);

		utilityRepository.save(utility1);
		utilityRepository.save(utility2);
		ownerRepository.save(owner);
		officeSpaceRepository.save(officeSpace1);
		officeSpaceRepository.save(officeSpace2);
	}

	@AfterEach
	void tearDown() throws Exception {
		utility1 = null;
		utility2 = null;

		officeSpace1 = null;
		officeSpace2 = null;

		owner = null;

		photos = null;

		utilityLease1 = null;
		utilityLease2 = null;
		utilityLease3 = null;
		utilityLease4 = null;
	}

	@Test
	void testFindAllEmpty() {
		List<UtilityLease> utilityLeases = utilityLeaseRepository.findAll();

		assertEquals(0, utilityLeases.size());
	}

	@Test
	void testFindAll() {
		utilityLeaseRepository.save(utilityLease1);
		utilityLeaseRepository.save(utilityLease2);
		utilityLeaseRepository.save(utilityLease3);
		utilityLeaseRepository.save(utilityLease4);

		List<UtilityLease> utilityLeases = utilityLeaseRepository.findAll();

		assertEquals(4, utilityLeases.size());
		assertEquals(utilityLease1, utilityLeases.get(0));
		assertEquals(utilityLease2, utilityLeases.get(1));
		assertEquals(utilityLease3, utilityLeases.get(2));
		assertEquals(utilityLease4, utilityLeases.get(3));
	}

	@Test
	void testFindAllByUtility_UtilityIdEmpty() {
		List<UtilityLease> utilityLeases = utilityLeaseRepository.findAllByUtility_UtilityId(1);

		assertEquals(0, utilityLeases.size());
	}

	@Test
	void testFindAllByUtility_UtilityId() {
		utilityLeaseRepository.save(utilityLease1);
		utilityLeaseRepository.save(utilityLease2);
		utilityLeaseRepository.save(utilityLease3);
		utilityLeaseRepository.save(utilityLease4);

		List<UtilityLease> utilityLeases = utilityLeaseRepository.findAllByUtility_UtilityId(1);

		assertEquals(2, utilityLeases.size());
		assertEquals(utilityLease1, utilityLeases.get(0));
		assertEquals(utilityLease3, utilityLeases.get(1));
	}

	@Test
	void testFindAllByProperty_PropertyIdEmpty() {
		List<UtilityLease> utilityLeases = utilityLeaseRepository.findAllByProperty_PropertyId(1);

		assertEquals(0, utilityLeases.size());
	}

	@Test
	void testFindAllByProperty_PropertyId() {
		utilityLeaseRepository.save(utilityLease1);
		utilityLeaseRepository.save(utilityLease2);
		utilityLeaseRepository.save(utilityLease3);
		utilityLeaseRepository.save(utilityLease4);

		List<UtilityLease> utilityLeases = utilityLeaseRepository.findAllByProperty_PropertyId(1);

		assertEquals(2, utilityLeases.size());
		assertEquals(utilityLease1, utilityLeases.get(0));
		assertEquals(utilityLease2, utilityLeases.get(1));
	}

	@Test
	void testFindByIdBadId() {
		utilityLeaseRepository.save(utilityLease1);

		Optional<UtilityLease> utilityLease = utilityLeaseRepository.findById(2);

		assertTrue(utilityLease.isEmpty());
	}

	@Test
	void testFindById() {
		utilityLeaseRepository.save(utilityLease1);

		Optional<UtilityLease> utilityLease = utilityLeaseRepository.findById(1);

		assertTrue(utilityLease.isPresent());
		assertEquals(utilityLease1, utilityLease.get());
	}

	@Test
	void testFindByUtility_UtilityIdAndProperty_PropertyIdBadUtilityId() {
		utilityLeaseRepository.save(utilityLease1);

		Optional<UtilityLease> utilityLease = utilityLeaseRepository.findByUtility_UtilityIdAndProperty_PropertyId(2,
				1);

		assertTrue(utilityLease.isEmpty());
	}

	@Test
	void testFindByUtility_UtilityIdAndProperty_PropertyIdBadPropertyId() {
		utilityLeaseRepository.save(utilityLease1);

		Optional<UtilityLease> utilityLease = utilityLeaseRepository.findByUtility_UtilityIdAndProperty_PropertyId(1,
				2);

		assertTrue(utilityLease.isEmpty());
	}

	@Test
	void testFindByUtility_UtilityIdAndProperty_PropertyId() {
		utilityLeaseRepository.save(utilityLease1);

		Optional<UtilityLease> utilityLease = utilityLeaseRepository.findByUtility_UtilityIdAndProperty_PropertyId(1,
				1);

		assertTrue(utilityLease.isPresent());
		assertEquals(utilityLease1, utilityLease.get());
	}

	@Test
	void testSave() {
		UtilityLease savedUtilityLease = utilityLeaseRepository.save(utilityLease1);

		assertEquals(utilityLease1, savedUtilityLease);
	}

	@Test
	void testDeleteById() {
		utilityLeaseRepository.save(utilityLease1);

		utilityLeaseRepository.deleteById(1);

		Optional<UtilityLease> deletedUtilityLease = utilityLeaseRepository.findById(1);
		assertFalse(deletedUtilityLease.isPresent());
	}

}
