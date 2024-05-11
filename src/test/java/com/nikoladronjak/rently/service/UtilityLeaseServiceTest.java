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

import com.nikoladronjak.rently.domain.OfficeSpace;
import com.nikoladronjak.rently.domain.Owner;
import com.nikoladronjak.rently.domain.Rent;
import com.nikoladronjak.rently.domain.Utility;
import com.nikoladronjak.rently.domain.UtilityLease;
import com.nikoladronjak.rently.dto.UtilityLeaseDTO;
import com.nikoladronjak.rently.repository.EventSpaceRepository;
import com.nikoladronjak.rently.repository.OfficeSpaceRepository;
import com.nikoladronjak.rently.repository.RentRepository;
import com.nikoladronjak.rently.repository.UtilityLeaseRepository;
import com.nikoladronjak.rently.repository.UtilityRepository;

@SpringBootTest
class UtilityLeaseServiceTest {

	Utility utility1;

	Utility utility2;

	Owner owner;

	OfficeSpace officeSpace1;

	OfficeSpace officeSpace2;

	Rent rent;

	List<Rent> rents;

	List<UtilityLease> utilityLeases;

	UtilityLease utilityLease1;

	UtilityLease utilityLease2;

	UtilityLease utilityLease3;

	UtilityLease utilityLease4;

	@Mock
	private UtilityRepository utilityRepository;

	@Mock
	private EventSpaceRepository eventSpaceRepository;

	@Mock
	private OfficeSpaceRepository officeSpaceRepository;

	@Mock
	private RentRepository rentRepository;

	@Mock
	private UtilityLeaseRepository utilityLeaseRepository;

	@InjectMocks
	private UtilityLeaseService utilityLeaseService;

	@BeforeEach
	void setUp() throws Exception {
		List<String> photos = new ArrayList<String>();
		photos.add("photo1");
		photos.add("photo2");

		utility1 = new Utility(1, "Microphone", "", null);
		utility2 = new Utility(2, "Projector", "", null);

		owner = new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");

		officeSpace1 = new OfficeSpace(1, "Office Space 1", "Jove Ilica 154", "", 300, 150, true, 30, photos, owner,
				null, 100, null);
		officeSpace2 = new OfficeSpace(2, "Office Space 2", "Studentski trg 1", "", 250, 120, true, 20, photos, owner,
				null, 90, null);

		rent = new Rent(1, 0, utilityLeases, null);

		rents = new ArrayList<Rent>();
		rents.add(rent);

		utilityLease1 = new UtilityLease(1, 50, utility1, officeSpace1, null);
		utilityLease2 = new UtilityLease(2, 60, utility2, officeSpace1, null);
		utilityLease3 = new UtilityLease(3, 50, utility1, officeSpace2, null);
		utilityLease4 = new UtilityLease(4, 60, utility2, officeSpace2, null);

		utilityLeases = new ArrayList<UtilityLease>();
		utilityLeases.add(utilityLease1);
		utilityLeases.add(utilityLease2);
		utilityLeases.add(utilityLease3);
		utilityLeases.add(utilityLease4);
	}

	@AfterEach
	void tearDown() throws Exception {
		utility1 = null;
		utility2 = null;

		officeSpace1 = null;
		officeSpace2 = null;

		owner = null;

		rent = null;

		rents = null;

		utilityLease1 = null;
		utilityLease2 = null;
		utilityLease3 = null;
		utilityLease4 = null;

		utilityLeases = null;
	}

	@Test
	void testGetAllError() {
		when(utilityLeaseRepository.findAll()).thenThrow(new RuntimeException("Something went wrong"));

		ResponseEntity<?> response = utilityLeaseService.getAll();

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("Something went wrong", response.getBody());
	}

	@Test
	void testGetAll() {
		List<UtilityLease> utilityLeases = new ArrayList<UtilityLease>();
		utilityLeases.add(utilityLease1);
		utilityLeases.add(utilityLease2);
		utilityLeases.add(utilityLease3);
		utilityLeases.add(utilityLease4);
		when(utilityLeaseRepository.findAll()).thenReturn(utilityLeases);

		ResponseEntity<?> response = utilityLeaseService.getAll();
		List<UtilityLeaseDTO> utilityLeaseDTOs = utilityLeases.stream().map(this::convertToDTO)
				.collect(Collectors.toList());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(utilityLeaseDTOs, response.getBody());
	}

	@Test
	void testGetAllByUtilityIdError() {
		when(utilityLeaseRepository.findAllByUtility_UtilityId(1))
				.thenThrow(new RuntimeException("Something went wrong"));

		ResponseEntity<?> response = utilityLeaseService.getAllByUtilityId(1);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("Something went wrong", response.getBody());
	}

	@Test
	void testGetAllByUtilityId() {
		List<UtilityLease> utilityLeases = new ArrayList<UtilityLease>();
		utilityLeases.add(utilityLease1);
		utilityLeases.add(utilityLease3);
		when(utilityLeaseRepository.findAllByUtility_UtilityId(1)).thenReturn(utilityLeases);

		ResponseEntity<?> response = utilityLeaseService.getAllByUtilityId(1);
		List<UtilityLeaseDTO> utilityLeaseDTOs = utilityLeases.stream().map(this::convertToDTO)
				.collect(Collectors.toList());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(utilityLeaseDTOs, response.getBody());
	}

	@Test
	void testGetAllByPropertyIdError() {
		when(utilityLeaseRepository.findAllByProperty_PropertyId(1))
				.thenThrow(new RuntimeException("Something went wrong"));

		ResponseEntity<?> response = utilityLeaseService.getAllByPropertyId(1);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("Something went wrong", response.getBody());
	}

	@Test
	void testGetAllByPropertyId() {
		List<UtilityLease> utilityLeases = new ArrayList<UtilityLease>();
		utilityLeases.add(utilityLease1);
		utilityLeases.add(utilityLease2);
		when(utilityLeaseRepository.findAllByProperty_PropertyId(1)).thenReturn(utilityLeases);

		ResponseEntity<?> response = utilityLeaseService.getAllByPropertyId(1);
		List<UtilityLeaseDTO> utilityLeaseDTOs = utilityLeases.stream().map(this::convertToDTO)
				.collect(Collectors.toList());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(utilityLeaseDTOs, response.getBody());
	}

	@Test
	void testGetByIdBadId() {
		when(utilityLeaseRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = utilityLeaseService.getById(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no utility lease with the given id.", response.getBody());
	}

	@Test
	void testGetById() {
		when(utilityLeaseRepository.findById(1)).thenReturn(Optional.of(utilityLease1));

		ResponseEntity<?> response = utilityLeaseService.getById(1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(utilityLease1), response.getBody());
	}

	@Test
	void testAddBadUtilityId() {
		when(utilityRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = utilityLeaseService.add(convertToDTO(utilityLease1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no utility for the given utilityId.", response.getBody());
	}

	@Test
	void testAddBadPropertyId() {
		when(utilityRepository.findById(1)).thenReturn(Optional.of(utility1));
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = utilityLeaseService.add(convertToDTO(utilityLease1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("The property has to be either an event space or an office space.", response.getBody());
	}

	@Test
	void testAddDuplicate() {
		when(utilityRepository.findById(1)).thenReturn(Optional.of(utility1));
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.of(officeSpace1));
		when(utilityLeaseRepository.findByUtility_UtilityIdAndProperty_PropertyId(1, 1))
				.thenReturn(Optional.of(utilityLease1));

		ResponseEntity<?> response = utilityLeaseService.add(convertToDTO(utilityLease1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This utility lease already exists.", response.getBody());
	}

	@Test
	void testAdd() {
		when(utilityRepository.findById(1)).thenReturn(Optional.of(utility1));
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.of(officeSpace1));
		when(utilityLeaseRepository.findByUtility_UtilityIdAndProperty_PropertyId(1, 1)).thenReturn(Optional.empty());
		when(utilityLeaseRepository.save(any(UtilityLease.class))).thenReturn(utilityLease1);

		ResponseEntity<?> response = utilityLeaseService.add(convertToDTO(utilityLease1));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(utilityLease1), response.getBody());
	}

	@Test
	void testUpdateBadId() {
		when(utilityLeaseRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = utilityLeaseService.update(1, convertToDTO(utilityLease1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no utility lease with the given id.", response.getBody());
	}

	@Test
	void testUpdateBadUtilityId() {
		when(utilityLeaseRepository.findById(1)).thenReturn(Optional.of(utilityLease1));
		when(utilityRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = utilityLeaseService.update(1, convertToDTO(utilityLease1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no utility for the given utilityId.", response.getBody());
	}

	@Test
	void testUpdateBadPropertyId() {
		when(utilityLeaseRepository.findById(1)).thenReturn(Optional.of(utilityLease1));
		when(utilityRepository.findById(1)).thenReturn(Optional.of(utility1));
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = utilityLeaseService.update(1, convertToDTO(utilityLease1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("The property has to be either an event space or an office space.", response.getBody());
	}

	@Test
	void testUpdateDuplicate() {
		when(utilityLeaseRepository.findById(1)).thenReturn(Optional.of(utilityLease1));
		when(utilityRepository.findById(1)).thenReturn(Optional.of(utility1));
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.of(officeSpace1));
		when(utilityLeaseRepository.findByUtility_UtilityIdAndProperty_PropertyId(1, 1))
				.thenReturn(Optional.of(utilityLease1));
		when(utilityLeaseRepository.save(any(UtilityLease.class)))
				.thenThrow(new RuntimeException("This utility lease already exists."));

		ResponseEntity<?> response = utilityLeaseService.update(1, convertToDTO(utilityLease1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This utility lease already exists.", response.getBody());
	}

	@Test
	void testUpdate() {
		when(utilityLeaseRepository.findById(1)).thenReturn(Optional.of(utilityLease1));
		when(utilityRepository.findById(1)).thenReturn(Optional.of(utility1));
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.of(officeSpace1));
		when(utilityLeaseRepository.findByUtility_UtilityIdAndProperty_PropertyId(1, 1)).thenReturn(Optional.empty());
		when(utilityLeaseRepository.save(any(UtilityLease.class))).thenReturn(utilityLease1);

		ResponseEntity<?> response = utilityLeaseService.update(1, convertToDTO(utilityLease1));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(utilityLease1), response.getBody());
	}

	@Test
	void testDeleteBadId() {
		when(utilityLeaseRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = utilityLeaseService.delete(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no utility lease with the given id.", response.getBody());
	}

	@Test
	void testDeleteAssociatedRent() {
		when(utilityLeaseRepository.findById(1)).thenReturn(Optional.of(utilityLease1));
		when(rentRepository.findAllByUtilityLeases_UtilityLeaseId(1)).thenReturn(rents);

		ResponseEntity<?> response = utilityLeaseService.delete(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("You cannot delete this utility lease since there are rents associated with it.",
				response.getBody());
	}

	@Test
	void testDelete() {
		when(utilityLeaseRepository.findById(1)).thenReturn(Optional.of(utilityLease1));
		when(rentRepository.findAllByUtilityLeases_UtilityLeaseId(1)).thenReturn(new ArrayList<Rent>());

		ResponseEntity<?> response = utilityLeaseService.delete(1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(utilityLease1), response.getBody());
	}

	private UtilityLeaseDTO convertToDTO(UtilityLease utilityLease) {
		UtilityLeaseDTO utilityLeaseDTO = new UtilityLeaseDTO();
		utilityLeaseDTO.setUtilityId(utilityLease.getUtility().getUtilityId());
		utilityLeaseDTO.setPropertyId(utilityLease.getProperty().getPropertyId());
		utilityLeaseDTO.setRentalRate(utilityLease.getRentalRate());

		return utilityLeaseDTO;
	}

}
