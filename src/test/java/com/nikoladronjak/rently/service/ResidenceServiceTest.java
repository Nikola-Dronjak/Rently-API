package com.nikoladronjak.rently.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.GregorianCalendar;
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

import com.nikoladronjak.rently.domain.EventSpace;
import com.nikoladronjak.rently.domain.HeatingType;
import com.nikoladronjak.rently.domain.Lease;
import com.nikoladronjak.rently.domain.OfficeSpace;
import com.nikoladronjak.rently.domain.Owner;
import com.nikoladronjak.rently.domain.Residence;
import com.nikoladronjak.rently.dto.ResidenceDTO;
import com.nikoladronjak.rently.repository.EventSpaceRepository;
import com.nikoladronjak.rently.repository.LeaseRepository;
import com.nikoladronjak.rently.repository.OfficeSpaceRepository;
import com.nikoladronjak.rently.repository.OwnerRepository;
import com.nikoladronjak.rently.repository.ResidenceRepository;

@SpringBootTest
class ResidenceServiceTest {

	Owner owner;

	EventSpace eventSpace1;

	OfficeSpace officeSpace1;

	Lease lease;

	List<Lease> leases;

	Residence residence1;

	Residence residence2;

	@Mock
	private OwnerRepository ownerRepository;

	@Mock
	private EventSpaceRepository eventSpaceRepository;

	@Mock
	private OfficeSpaceRepository officeSpaceRepository;

	@Mock
	private LeaseRepository leaseRepository;

	@Mock
	private ResidenceRepository residenceRepository;

	@InjectMocks
	private ResidenceService residenceService;

	@BeforeEach
	void setUp() throws Exception {
		owner = new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");

		eventSpace1 = new EventSpace(1, "Lux Event Space 1", "Bulevar Osolobodjenja 13", "", 250, 100, true, 50, null,
				owner, null, 200, true, true, null);

		officeSpace1 = new OfficeSpace(1, "Office 1", "Bulevar Kralja Aleksandra 1", "", 400, 100, true, 30, null,
				owner, null, 50, null);

		lease = new Lease(1, 300, new GregorianCalendar(2024, 11, 12), new GregorianCalendar(2025, 11, 12), residence1,
				null, null);

		leases = new ArrayList<Lease>();
		leases.add(lease);

		residence1 = new Residence(1, "Apartement 1", "Jove Ilica 154", "", 300, 30, true, 0, null, owner, null, 1, 1,
				HeatingType.Central, true, true);
		residence2 = new Residence(2, "Apartement 2", "Studentski trg 1", "", 400, 40, true, 0, null, owner, null, 1, 1,
				HeatingType.Central, true, true);

	}

	@AfterEach
	void tearDown() throws Exception {
		owner = null;

		eventSpace1 = null;

		officeSpace1 = null;

		lease = null;

		leases = null;

		residence1 = null;
		residence2 = null;
	}

	@Test
	void testGetAllError() {
		when(residenceRepository.findAll()).thenThrow(new RuntimeException("Something went wrong"));

		ResponseEntity<?> response = residenceService.getAll();

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("Something went wrong", response.getBody());
	}

	@Test
	void testGetAll() {
		List<Residence> residences = new ArrayList<Residence>();
		residences.add(residence1);
		residences.add(residence2);
		when(residenceRepository.findAll()).thenReturn(residences);

		ResponseEntity<?> response = residenceService.getAll();
		List<ResidenceDTO> residenceDTOs = residences.stream().map(this::convertToDTO).collect(Collectors.toList());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(residenceDTOs, response.getBody());
	}

	@Test
	void testGetByIdBadId() {
		when(residenceRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = residenceService.getById(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no residence with the given id.", response.getBody());
	}

	@Test
	void testGetById() {
		when(residenceRepository.findById(1)).thenReturn(Optional.of(residence1));

		ResponseEntity<?> response = residenceService.getById(1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(residence1), response.getBody());
	}

	@Test
	void testAddMissingOwner() {
		when(ownerRepository.findById(residence1.getOwner().getOwnerId())).thenReturn(Optional.empty());

		ResponseEntity<?> response = residenceService.add(convertToDTO(residence1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no owner with the given id.", response.getBody());
	}

	@Test
	void testAddDuplicateResidence() {
		when(ownerRepository.findById(residence1.getOwner().getOwnerId())).thenReturn(Optional.of(owner));
		when(residenceRepository.findByAddress(residence1.getAddress())).thenReturn(Optional.of(residence1));

		ResponseEntity<?> response = residenceService.add(convertToDTO(residence1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This property already exists.", response.getBody());
	}

	@Test
	void testAddDuplicateEventSpace() {
		when(ownerRepository.findById(residence1.getOwner().getOwnerId())).thenReturn(Optional.of(owner));
		when(eventSpaceRepository.findByAddress(residence1.getAddress())).thenReturn(Optional.of(eventSpace1));

		ResponseEntity<?> response = residenceService.add(convertToDTO(residence1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This property already exists.", response.getBody());
	}

	@Test
	void testAddDuplicateOfficeSpace() {
		when(ownerRepository.findById(residence1.getOwner().getOwnerId())).thenReturn(Optional.of(owner));
		when(officeSpaceRepository.findByAddress(residence1.getAddress())).thenReturn(Optional.of(officeSpace1));

		ResponseEntity<?> response = residenceService.add(convertToDTO(residence1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This property already exists.", response.getBody());
	}

	@Test
	void testAdd() {
		when(ownerRepository.findById(residence1.getOwner().getOwnerId())).thenReturn(Optional.of(owner));
		when(residenceRepository.findByAddress(residence1.getAddress())).thenReturn(Optional.empty());
		when(eventSpaceRepository.findByAddress(residence1.getAddress())).thenReturn(Optional.empty());
		when(officeSpaceRepository.findByAddress(residence1.getAddress())).thenReturn(Optional.empty());
		when(residenceRepository.save(any(Residence.class))).thenReturn(residence1);

		ResponseEntity<?> response = residenceService.add(convertToDTO(residence1));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(residence1), response.getBody());
	}

	@Test
	void testUpdateBadId() {
		when(residenceRepository.findById(residence1.getPropertyId())).thenReturn(Optional.empty());

		ResponseEntity<?> response = residenceService.update(1, convertToDTO(residence1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no residence with the given id.", response.getBody());
	}

	@Test
	void testUpdateMissingOwner() {
		when(residenceRepository.findById(residence1.getPropertyId())).thenReturn(Optional.of(residence1));
		when(ownerRepository.findById(residence1.getOwner().getOwnerId())).thenReturn(Optional.empty());

		ResponseEntity<?> response = residenceService.update(1, convertToDTO(residence1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no owner with the given id.", response.getBody());
	}

	@Test
	void testUpdateDuplicateResidence() {
		when(residenceRepository.findById(residence1.getPropertyId())).thenReturn(Optional.of(residence1));
		when(ownerRepository.findById(residence1.getOwner().getOwnerId())).thenReturn(Optional.of(owner));
		when(residenceRepository.findByAddress(residence1.getAddress())).thenReturn(Optional.of(residence1));
		when(residenceRepository.save(any(Residence.class)))
				.thenThrow(new RuntimeException("This property already exists."));

		ResponseEntity<?> response = residenceService.update(residence1.getPropertyId(), convertToDTO(residence1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This property already exists.", response.getBody());
	}

	@Test
	void testUpdateDuplicateEventSpace() {
		when(residenceRepository.findById(residence1.getPropertyId())).thenReturn(Optional.of(residence1));
		when(ownerRepository.findById(residence1.getOwner().getOwnerId())).thenReturn(Optional.of(owner));
		when(eventSpaceRepository.findByAddress(residence1.getAddress())).thenReturn(Optional.of(eventSpace1));
		when(residenceRepository.save(any(Residence.class)))
				.thenThrow(new RuntimeException("This property already exists."));

		ResponseEntity<?> response = residenceService.update(residence1.getPropertyId(), convertToDTO(residence1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This property already exists.", response.getBody());
	}

	@Test
	void testUpdateDuplicateOfficeSpace() {
		when(residenceRepository.findById(residence1.getPropertyId())).thenReturn(Optional.of(residence1));
		when(ownerRepository.findById(residence1.getOwner().getOwnerId())).thenReturn(Optional.of(owner));
		when(officeSpaceRepository.findByAddress(residence1.getAddress())).thenReturn(Optional.of(officeSpace1));
		when(residenceRepository.save(any(Residence.class)))
				.thenThrow(new RuntimeException("This property already exists."));

		ResponseEntity<?> response = residenceService.update(residence1.getPropertyId(), convertToDTO(residence1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This property already exists.", response.getBody());
	}

	@Test
	void testUpdate() {
		when(residenceRepository.findById(residence1.getPropertyId())).thenReturn(Optional.of(residence1));
		when(ownerRepository.findById(residence1.getOwner().getOwnerId())).thenReturn(Optional.of(owner));
		when(residenceRepository.findByAddress(residence1.getAddress())).thenReturn(Optional.empty());
		when(residenceRepository.save(any(Residence.class))).thenReturn(residence1);

		ResponseEntity<?> response = residenceService.update(1, convertToDTO(residence1));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(residence1), response.getBody());
	}

	@Test
	void testDeleteBadId() {
		when(residenceRepository.findById(residence1.getPropertyId())).thenReturn(Optional.empty());

		ResponseEntity<?> response = residenceService.delete(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no residence with the given id.", response.getBody());
	}

	@Test
	void testDeleteAssociatedLease() {
		when(residenceRepository.findById(residence1.getPropertyId())).thenReturn(Optional.of(residence1));
		when(leaseRepository.findAllByProperty_PropertyId(residence1.getPropertyId())).thenReturn(leases);

		ResponseEntity<?> response = residenceService.delete(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("You cannot delete this residence since there are leases associated with it.", response.getBody());
	}

	@Test
	void testDelete() {
		when(residenceRepository.findById(residence1.getPropertyId())).thenReturn(Optional.of(residence1));
		when(leaseRepository.findAllByProperty_PropertyId(residence1.getPropertyId()))
				.thenReturn(new ArrayList<Lease>());

		ResponseEntity<?> response = residenceService.delete(1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(residence1), response.getBody());
	}

	private ResidenceDTO convertToDTO(Residence residence) {
		ResidenceDTO residenceDTO = new ResidenceDTO();
		residenceDTO.setPropertyId(residence.getPropertyId());
		residenceDTO.setName(residence.getName());
		residenceDTO.setAddress(residence.getAddress());
		residenceDTO.setDescription(residence.getDescription());
		residenceDTO.setRentalRate(residence.getRentalRate());
		residenceDTO.setSize(residence.getSize());
		residenceDTO.setIsAvailable(residence.isAvailable());
		residenceDTO.setNumberOfParkingSpots(residence.getNumberOfParkingSpots());
		residenceDTO.setPhotos(residence.getPhotos());
		residenceDTO.setNumberOfBedrooms(residence.getNumberOfBedrooms());
		residenceDTO.setNumberOfBathrooms(residence.getNumberOfBathrooms());
		residenceDTO.setHeatingType(residence.getHeatingType());
		residenceDTO.setIsPetFriendly(residence.isPetFriendly());
		residenceDTO.setIsFurnished(residence.isFurnished());
		residenceDTO.setOwnerId(residence.getOwner().getOwnerId());

		return residenceDTO;
	}
}
