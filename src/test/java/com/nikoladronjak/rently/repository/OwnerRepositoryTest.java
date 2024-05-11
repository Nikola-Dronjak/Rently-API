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

import com.nikoladronjak.rently.domain.Owner;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OwnerRepositoryTest {

	Owner owner1;

	Owner owner2;

	@Autowired
	private OwnerRepository ownerRepository;

	@BeforeEach
	void setUp() throws Exception {
		owner1 = new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");
		owner2 = new Owner(2, "Mika", "Mikic", "mika@gmail.com", "mika123", "0987654321");
	}

	@AfterEach
	void tearDown() throws Exception {
		owner1 = null;
		owner2 = null;
	}

	@Test
	void testFindAllEmpty() {
		List<Owner> owners = ownerRepository.findAll();

		assertEquals(0, owners.size());
	}

	@Test
	void testFindAll() {
		ownerRepository.save(owner1);
		ownerRepository.save(owner2);

		List<Owner> owners = ownerRepository.findAll();

		assertEquals(2, owners.size());
		assertEquals(owner1, owners.get(0));
		assertEquals(owner2, owners.get(1));
	}

	@Test
	void testFindByIdBadId() {
		ownerRepository.save(owner1);

		Optional<Owner> owner = ownerRepository.findById(2);

		assertTrue(owner.isEmpty());
	}

	@Test
	void testFindById() {
		ownerRepository.save(owner1);

		Optional<Owner> owner = ownerRepository.findById(1);

		assertTrue(owner.isPresent());
		assertEquals(owner1, owner.get());
	}

	@Test
	void testFindByEmailBadEmail() {
		ownerRepository.save(owner1);

		Optional<Owner> owner = ownerRepository.findByEmail("mika@gmail.com");

		assertTrue(owner.isEmpty());
	}

	@Test
	void testFindByEmail() {
		ownerRepository.save(owner1);

		Optional<Owner> owner = ownerRepository.findByEmail("pera@gmail.com");

		assertTrue(owner.isPresent());
		assertEquals(owner1, owner.get());
	}

	@Test
	void testSave() {
		Owner savedOwner = ownerRepository.save(owner1);

		assertEquals(owner1, savedOwner);
	}

	@Test
	void testDeleteById() {
		ownerRepository.save(owner1);

		ownerRepository.deleteById(1);

		Optional<Owner> deletedOwner = ownerRepository.findById(1);
		assertFalse(deletedOwner.isPresent());
	}

}
