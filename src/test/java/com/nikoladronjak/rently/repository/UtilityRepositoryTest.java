package com.nikoladronjak.rently.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import com.nikoladronjak.rently.domain.Utility;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UtilityRepositoryTest {

	Utility utility1;

	Utility utility2;

	@Autowired
	private UtilityRepository utilityRepository;

	@BeforeEach
	void setUp() throws Exception {
		utility1 = new Utility(1, "Microphone", "", null);
		utility2 = new Utility(2, "Projector", "", null);
	}

	@AfterEach
	void tearDown() throws Exception {
		utility1 = null;
		utility2 = null;
	}

	@Test
	void testFindAllEmpty() {
		List<Utility> utilities = utilityRepository.findAll();

		assertEquals(0, utilities.size());
	}

	@Test
	void testFindAll() {
		utilityRepository.save(utility1);
		utilityRepository.save(utility2);

		List<Utility> utilities = utilityRepository.findAll();

		assertEquals(2, utilities.size());
		assertEquals(utility1, utilities.get(0));
		assertEquals(utility2, utilities.get(1));
	}

	@Test
	void testFindByIdBadId() {
		utilityRepository.save(utility1);

		Optional<Utility> utility = utilityRepository.findById(2);

		assertTrue(utility.isEmpty());
	}

	@Test
	void testFindById() {
		utilityRepository.save(utility1);

		Optional<Utility> utility = utilityRepository.findById(1);

		assertTrue(utility.isPresent());
		assertEquals(utility1, utility.get());
	}

	@Test
	void testFindByEmailBadEmail() {
		utilityRepository.save(utility1);

		Optional<Utility> utility = utilityRepository.findByName("Projector");

		assertTrue(utility.isEmpty());
	}

	@Test
	void testFindByEmail() {
		utilityRepository.save(utility1);

		Optional<Utility> utility = utilityRepository.findByName("Microphone");

		assertTrue(utility.isPresent());
		assertEquals(utility1, utility.get());
	}

	@Test
	void testSave() {
		Utility savedUtility = utilityRepository.save(utility1);

		assertEquals(utility1, savedUtility);
	}

	@Test
	void testDeleteById() {
		utilityRepository.save(utility1);

		utilityRepository.deleteById(1);

		Optional<Utility> deletedUtility = utilityRepository.findById(1);
		assertFalse(deletedUtility.isPresent());
	}

}
