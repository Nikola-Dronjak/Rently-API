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

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OfficeSpaceRepositoryTest {

	Owner owner;

	List<String> photos;

	OfficeSpace officeSpace1;

	OfficeSpace officeSpace2;

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private OfficeSpaceRepository officeSpaceRepository;

	@BeforeEach
	void setUp() throws Exception {
		owner = new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");

		photos = new ArrayList<String>();
		photos.add("photo1");
		photos.add("photo2");
		photos.add("photo3");

		officeSpace1 = new OfficeSpace(1, "Office Space 1", "Jove Ilica 154", "", (double) 400, 100, true, 20, photos,
				owner, null, 20, null);
		officeSpace2 = new OfficeSpace(2, "Office Space 2", "Studentski trg 1", "", (double) 300, 200, true, 20, photos,
				owner, null, 20, null);

		ownerRepository.save(owner);
	}

	@AfterEach
	void tearDown() throws Exception {
		owner = null;

		photos = null;

		officeSpace1 = null;
		officeSpace2 = null;
	}

	@Test
	void testFindAllEmpty() {
		List<OfficeSpace> officeSpaces = officeSpaceRepository.findAll();

		assertEquals(0, officeSpaces.size());
	}

	@Test
	void testFindAll() {
		officeSpaceRepository.save(officeSpace1);
		officeSpaceRepository.save(officeSpace2);

		List<OfficeSpace> officeSpaces = officeSpaceRepository.findAll();

		assertEquals(2, officeSpaces.size());
		assertEquals(officeSpace1, officeSpaces.get(0));
		assertEquals(officeSpace2, officeSpaces.get(1));
	}

	@Test
	void testFindAllByOwner_OnwerIdEmpty() {
		List<OfficeSpace> officeSpaces = officeSpaceRepository.findAllByOwner_OwnerId(2);

		assertEquals(0, officeSpaces.size());
	}

	@Test
	void testFindAllByOwner_OwnerId() {
		officeSpaceRepository.save(officeSpace1);
		officeSpaceRepository.save(officeSpace2);

		List<OfficeSpace> officeSpaces = officeSpaceRepository.findAllByOwner_OwnerId(1);

		assertEquals(2, officeSpaces.size());
		assertEquals(officeSpace1, officeSpaces.get(0));
		assertEquals(officeSpace2, officeSpaces.get(1));
	}

	@Test
	void testFindByIdBadId() {
		officeSpaceRepository.save(officeSpace1);

		Optional<OfficeSpace> officeSpace = officeSpaceRepository.findById(2);

		assertTrue(officeSpace.isEmpty());
	}

	@Test
	void testFindById() {
		officeSpaceRepository.save(officeSpace1);

		Optional<OfficeSpace> officeSpace = officeSpaceRepository.findById(1);

		assertTrue(officeSpace.isPresent());
		assertEquals(officeSpace1, officeSpace.get());
	}

	@Test
	void testFindByAddressBadAddress() {
		officeSpaceRepository.save(officeSpace1);

		Optional<OfficeSpace> officeSpace = officeSpaceRepository.findByAddress("Studentski trg 1");

		assertTrue(officeSpace.isEmpty());
	}

	@Test
	void testFindByAddress() {
		officeSpaceRepository.save(officeSpace1);

		Optional<OfficeSpace> officeSpace = officeSpaceRepository.findByAddress("Jove Ilica 154");

		assertTrue(officeSpace.isPresent());
		assertEquals(officeSpace1, officeSpace.get());
	}

	@Test
	void testSave() {
		OfficeSpace savedOfficeSpace = officeSpaceRepository.save(officeSpace1);

		assertEquals(officeSpace1, savedOfficeSpace);
	}

	@Test
	void testDeleteById() {
		officeSpaceRepository.save(officeSpace1);

		officeSpaceRepository.deleteById(1);

		Optional<OfficeSpace> deletedOfficeSpace = officeSpaceRepository.findById(1);
		assertFalse(deletedOfficeSpace.isPresent());
	}

}
