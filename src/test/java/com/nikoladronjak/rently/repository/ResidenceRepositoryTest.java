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

import com.nikoladronjak.rently.domain.HeatingType;
import com.nikoladronjak.rently.domain.Owner;
import com.nikoladronjak.rently.domain.Residence;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ResidenceRepositoryTest {

	Owner owner;

	List<String> photos;

	Residence residence1;

	Residence residence2;

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private ResidenceRepository residenceRepository;

	@BeforeEach
	void setUp() throws Exception {
		owner = new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");

		photos = new ArrayList<String>();
		photos.add("photo1");
		photos.add("photo2");
		photos.add("photo3");

		residence1 = new Residence(1, "Apartement 1", "Jove Ilica 154", "", (double) 300, 30, true, 0, photos, owner,
				null, 1, 1, HeatingType.Central, true, true);
		residence2 = new Residence(2, "Apartement 2", "Studentski trg 1", "", (double) 400, 40, true, 0, photos, owner,
				null, 1, 1, HeatingType.Central, true, true);

		ownerRepository.save(owner);
	}

	@AfterEach
	void tearDown() throws Exception {
		owner = null;

		photos = null;

		residence1 = null;
		residence2 = null;
	}

	@Test
	void testFindAllEmpty() {
		List<Residence> residences = residenceRepository.findAll();

		assertEquals(0, residences.size());
	}

	@Test
	void testFindAll() {
		residenceRepository.save(residence1);
		residenceRepository.save(residence2);

		List<Residence> residences = residenceRepository.findAll();

		assertEquals(2, residences.size());
		assertEquals(residence1, residences.get(0));
		assertEquals(residence2, residences.get(1));
	}

	@Test
	void testFindAllByOwner_OwnerIdEmpty() {
		List<Residence> residences = residenceRepository.findAllByOwner_OwnerId(2);

		assertEquals(0, residences.size());
	}

	@Test
	void testFindAllOwner_OwnerId() {
		residenceRepository.save(residence1);
		residenceRepository.save(residence2);

		List<Residence> residences = residenceRepository.findAllByOwner_OwnerId(1);

		assertEquals(2, residences.size());
		assertEquals(residence1, residences.get(0));
		assertEquals(residence2, residences.get(1));
	}

	@Test
	void testFindByIdBadId() {
		residenceRepository.save(residence1);

		Optional<Residence> residence = residenceRepository.findById(2);

		assertTrue(residence.isEmpty());
	}

	@Test
	void testFindById() {
		residenceRepository.save(residence1);

		Optional<Residence> residence = residenceRepository.findById(1);

		assertTrue(residence.isPresent());
		assertEquals(residence1, residence.get());
	}

	@Test
	void testFindByAddressBadAddress() {
		residenceRepository.save(residence1);

		Optional<Residence> residence = residenceRepository.findByAddress("Studentski trg 1");

		assertTrue(residence.isEmpty());
	}

	@Test
	void testFindByAddress() {
		residenceRepository.save(residence1);

		Optional<Residence> residence = residenceRepository.findByAddress("Jove Ilica 154");

		assertTrue(residence.isPresent());
		assertEquals(residence1, residence.get());
	}

	@Test
	void testSave() {
		Residence savedResidence = residenceRepository.save(residence1);

		assertEquals(residence1, savedResidence);
	}

	@Test
	void testDeleteById() {
		residenceRepository.save(residence1);

		residenceRepository.deleteById(1);

		Optional<Residence> deletedResidence = residenceRepository.findById(1);
		assertFalse(deletedResidence.isPresent());
	}

}
