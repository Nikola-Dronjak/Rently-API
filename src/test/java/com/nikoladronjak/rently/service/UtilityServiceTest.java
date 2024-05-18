package com.nikoladronjak.rently.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nikoladronjak.rently.domain.Utility;
import com.nikoladronjak.rently.domain.UtilityLease;
import com.nikoladronjak.rently.dto.UtilityDTO;
import com.nikoladronjak.rently.repository.UtilityLeaseRepository;
import com.nikoladronjak.rently.repository.UtilityRepository;

@SpringBootTest
class UtilityServiceTest {

	UtilityLease utilityLease;

	List<UtilityLease> utilityLeases;

	Utility utility1;

	Utility utility2;

	@Mock
	private UtilityLeaseRepository utilityLeaseRepository;

	@Mock
	private UtilityRepository utilityRepository;

	@InjectMocks
	private UtilityService utilityService;

	@BeforeEach
	void setUp() throws Exception {
		utilityLease = new UtilityLease(1, (double) 50, utility1, null, null);

		utilityLeases = new ArrayList<UtilityLease>();
		utilityLeases.add(utilityLease);

		utility1 = new Utility(1, "Microphone", "", null);
		utility2 = new Utility(2, "Projector", "", null);
	}

	@AfterEach
	void tearDown() throws Exception {
		utilityLease = null;

		utilityLeases = null;

		utility1 = null;
		utility2 = null;
	}

	@Test
	void testGetAllError() {
		when(utilityRepository.findAll()).thenThrow(new RuntimeException("Something went wrong"));

		ResponseEntity<?> response = utilityService.getAll();

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("Something went wrong", response.getBody());
	}

	@Test
	void testGetAll() {
		List<Utility> utilities = new ArrayList<Utility>();
		utilities.add(utility1);
		utilities.add(utility2);
		when(utilityRepository.findAll()).thenReturn(utilities);

		ResponseEntity<?> response = utilityService.getAll();
		List<UtilityDTO> utilityDTOs = utilities.stream().map(this::convertToDTO).collect(Collectors.toList());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(utilityDTOs, response.getBody());
	}

	@Test
	void testGetByIdBadId() {
		when(utilityRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = utilityService.getById(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no utility with the given id.", response.getBody());
	}

	@Test
	void testGetById() {
		when(utilityRepository.findById(1)).thenReturn(Optional.of(utility1));

		ResponseEntity<?> response = utilityService.getById(1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(utility1), response.getBody());
	}

	@Test
	void testAddDuplicate() {
		when(utilityRepository.findByName(utility1.getName())).thenReturn(Optional.of(utility1));

		ResponseEntity<?> response = utilityService.add(convertToDTO(utility1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This utility already exists.", response.getBody());
	}

	@Test
	void testAdd() {
		when(utilityRepository.findByName(utility1.getName())).thenReturn(Optional.empty());
		when(utilityRepository.save(any(Utility.class))).thenReturn(utility1);

		ResponseEntity<?> response = utilityService.add(convertToDTO(utility1));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(utility1), response.getBody());
	}

	@Test
	void testUpdateBadId() {
		when(utilityRepository.findById(utility1.getUtilityId())).thenReturn(Optional.empty());

		ResponseEntity<?> response = utilityService.update(1, convertToDTO(utility1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no utility with the given id.", response.getBody());
	}

	@Test
	void testUpdateDuplicate() {
		when(utilityRepository.findById(utility1.getUtilityId())).thenReturn(Optional.of(utility1));
		when(utilityRepository.findByName(utility1.getName())).thenReturn(Optional.of(utility1));
		when(utilityRepository.save(any(Utility.class)))
				.thenThrow(new RuntimeException("This utility already exists."));

		ResponseEntity<?> response = utilityService.update(utility1.getUtilityId(), convertToDTO(utility1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This utility already exists.", response.getBody());
	}

	@Test
	void testUpdate() {
		when(utilityRepository.findById(utility1.getUtilityId())).thenReturn(Optional.of(utility1));
		when(utilityRepository.findByName(utility1.getName())).thenReturn(Optional.empty());
		when(utilityRepository.save(any(Utility.class))).thenReturn(utility1);

		ResponseEntity<?> response = utilityService.update(1, convertToDTO(utility1));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(utility1), response.getBody());
	}

	@Test
	void testDeleteBadId() {
		when(utilityRepository.findById(utility1.getUtilityId())).thenReturn(Optional.empty());

		ResponseEntity<?> response = utilityService.delete(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no utility with the given id.", response.getBody());
	}

	@Test
	void testDeleteAssociatedUtilityLease() {
		when(utilityRepository.findById(utility1.getUtilityId())).thenReturn(Optional.of(utility1));
		when(utilityLeaseRepository.findAllByUtility_UtilityId(utility1.getUtilityId())).thenReturn(utilityLeases);

		ResponseEntity<?> response = utilityService.delete(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("You cannot delete this utility since there are utility leases associated with it.",
				response.getBody());
	}

	@Test
	void testDelete() {
		when(utilityRepository.findById(utility1.getUtilityId())).thenReturn(Optional.of(utility1));
		when(utilityLeaseRepository.findAllByUtility_UtilityId(utility1.getUtilityId()))
				.thenReturn(new ArrayList<UtilityLease>());

		ResponseEntity<?> response = utilityService.delete(1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(utility1), response.getBody());
	}

	private UtilityDTO convertToDTO(Utility utility) {
		UtilityDTO utilityDTO = new UtilityDTO();
		utilityDTO.setName(utility.getName());
		utilityDTO.setDescription(utility.getDescription());

		return utilityDTO;
	}

}
