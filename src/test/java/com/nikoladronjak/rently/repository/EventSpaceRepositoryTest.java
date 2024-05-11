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

import com.nikoladronjak.rently.domain.EventSpace;
import com.nikoladronjak.rently.domain.Owner;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EventSpaceRepositoryTest {

	Owner owner;

	EventSpace eventSpace1;

	EventSpace eventSpace2;

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private EventSpaceRepository eventSpaceRepository;

	@BeforeEach
	void setUp() throws Exception {
		owner = new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");

		eventSpace1 = new EventSpace(1, "Event Space 1", "Jove Ilica 154", "", 300, 200, true, 20, null, owner, null,
				50, true, true, null);
		eventSpace2 = new EventSpace(2, "Event Space 2", "Studentski trg 1", "", 300, 200, true, 20, null, owner, null,
				50, true, true, null);

		ownerRepository.save(owner);
	}

	@AfterEach
	void tearDown() throws Exception {
		owner = null;

		eventSpace1 = null;
		eventSpace2 = null;
	}

	@Test
	void testFindAllEmpty() {
		List<EventSpace> eventSpaces = eventSpaceRepository.findAll();

		assertEquals(0, eventSpaces.size());
	}

	@Test
	void testFindAll() {
		eventSpaceRepository.save(eventSpace1);
		eventSpaceRepository.save(eventSpace2);

		List<EventSpace> eventSpaces = eventSpaceRepository.findAll();

		assertEquals(2, eventSpaces.size());
		assertEquals(eventSpace1, eventSpaces.get(0));
		assertEquals(eventSpace2, eventSpaces.get(1));
	}

	@Test
	void testFindAllByOwner_OnwerIdEmpty() {
		List<EventSpace> eventSpaces = eventSpaceRepository.findAllByOwner_OwnerId(2);

		assertEquals(0, eventSpaces.size());
	}

	@Test
	void testFindAllByOwner_OwnerId() {
		eventSpaceRepository.save(eventSpace1);
		eventSpaceRepository.save(eventSpace2);

		List<EventSpace> eventSpaces = eventSpaceRepository.findAllByOwner_OwnerId(1);

		assertEquals(2, eventSpaces.size());
		assertEquals(eventSpace1, eventSpaces.get(0));
		assertEquals(eventSpace2, eventSpaces.get(1));
	}

	@Test
	void testFindByIdBadId() {
		eventSpaceRepository.save(eventSpace1);

		Optional<EventSpace> eventSpace = eventSpaceRepository.findById(2);

		assertTrue(eventSpace.isEmpty());
	}

	@Test
	void testFindById() {
		eventSpaceRepository.save(eventSpace1);

		Optional<EventSpace> eventSpace = eventSpaceRepository.findById(1);

		assertTrue(eventSpace.isPresent());
		assertEquals(eventSpace1, eventSpace.get());
	}

	@Test
	void testFindByAddressBadAddress() {
		eventSpaceRepository.save(eventSpace1);

		Optional<EventSpace> eventSpace = eventSpaceRepository.findByAddress("Studentski trg 1");

		assertTrue(eventSpace.isEmpty());
	}

	@Test
	void testFindByAddress() {
		eventSpaceRepository.save(eventSpace1);

		Optional<EventSpace> eventSpace = eventSpaceRepository.findByAddress("Jove Ilica 154");

		assertTrue(eventSpace.isPresent());
		assertEquals(eventSpace1, eventSpace.get());
	}

	@Test
	void testSave() {
		EventSpace savedEventSpace = eventSpaceRepository.save(eventSpace1);

		assertEquals(eventSpace1, savedEventSpace);
	}

	@Test
	void testDeleteById() {
		eventSpaceRepository.save(eventSpace1);

		eventSpaceRepository.deleteById(1);

		Optional<EventSpace> deletedEventSpace = eventSpaceRepository.findById(1);
		assertFalse(deletedEventSpace.isPresent());
	}

}
