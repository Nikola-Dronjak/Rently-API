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
import com.nikoladronjak.rently.domain.UtilityLease;
import com.nikoladronjak.rently.dto.EventSpaceDTO;
import com.nikoladronjak.rently.repository.EventSpaceRepository;
import com.nikoladronjak.rently.repository.LeaseRepository;
import com.nikoladronjak.rently.repository.OfficeSpaceRepository;
import com.nikoladronjak.rently.repository.OwnerRepository;
import com.nikoladronjak.rently.repository.ResidenceRepository;
import com.nikoladronjak.rently.repository.UtilityLeaseRepository;

@SpringBootTest
class EventSpaceServiceTest {

	Owner owner;

	List<String> photos;

	Residence residence1;

	OfficeSpace officeSpace1;

	Lease lease;

	List<Lease> leases;

	UtilityLease utilityLease;

	List<UtilityLease> utilityLeases;

	EventSpace eventSpace1;

	EventSpace eventSpace2;

	@Mock
	private OwnerRepository ownerRepository;

	@Mock
	private OfficeSpaceRepository officeSpaceRepository;

	@Mock
	private ResidenceRepository residenceRepository;

	@Mock
	private LeaseRepository leaseRepository;

	@Mock
	private UtilityLeaseRepository utilityLeaseRepository;

	@Mock
	private EventSpaceRepository eventSpaceRepository;

	@InjectMocks
	private EventSpaceService eventSpaceService;

	@BeforeEach
	void setUp() throws Exception {
		owner = new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");

		photos = new ArrayList<String>();
		photos.add("photo1");
		photos.add("photo2");
		photos.add("photo3");

		residence1 = new Residence(1, "Apartement 1", "Jove Ilica 154", "", (double) 300, 30, true, 0, photos, owner,
				null, 1, 1, HeatingType.Central, true, true);

		officeSpace1 = new OfficeSpace(1, "Office 1", "Bulevar Kralja Aleksandra 1", "", (double) 400, 100, true, 30,
				photos, owner, null, 50, null);

		lease = new Lease(1, 300, new GregorianCalendar(2024, 11, 12), new GregorianCalendar(2025, 11, 12), eventSpace1,
				null, null);

		leases = new ArrayList<Lease>();
		leases.add(lease);

		utilityLease = new UtilityLease(1, (double) 0, null, eventSpace1, null);

		utilityLeases = new ArrayList<UtilityLease>();
		utilityLeases.add(utilityLease);

		eventSpace1 = new EventSpace(1, "Event Space 1", "Jove Ilica 154", "", (double) 300, 200, true, 20, photos,
				owner, null, 50, true, true, null);
		eventSpace2 = new EventSpace(2, "Event Space 2", "Studentski trg 1", "", (double) 300, 200, true, 20, photos,
				owner, null, 50, true, true, null);

	}

	@AfterEach
	void tearDown() throws Exception {
		owner = null;

		photos = null;

		residence1 = null;

		officeSpace1 = null;

		lease = null;

		leases = null;

		utilityLease = null;

		utilityLeases = null;

		eventSpace1 = null;
		eventSpace2 = null;
	}

	@Test
	void testGetAllError() {
		when(eventSpaceRepository.findAll()).thenThrow(new RuntimeException("Something went wrong"));

		ResponseEntity<?> response = eventSpaceService.getAll();

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("Something went wrong", response.getBody());
	}

	@Test
	void testGetAll() {
		List<EventSpace> eventSpaces = new ArrayList<EventSpace>();
		eventSpaces.add(eventSpace1);
		eventSpaces.add(eventSpace2);
		when(eventSpaceRepository.findAll()).thenReturn(eventSpaces);

		ResponseEntity<?> response = eventSpaceService.getAll();
		List<EventSpaceDTO> eventSpaceDTOs = eventSpaces.stream().map(this::convertToDTO).collect(Collectors.toList());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(eventSpaceDTOs, response.getBody());
	}

	@Test
	void testGetByIdBadId() {
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = eventSpaceService.getById(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no event space with the given id.", response.getBody());
	}

	@Test
	void testGetById() {
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.of(eventSpace1));

		ResponseEntity<?> response = eventSpaceService.getById(1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(eventSpace1), response.getBody());
	}

	@Test
	void testAddMissingOwner() {
		when(ownerRepository.findById(eventSpace1.getOwner().getOwnerId())).thenReturn(Optional.empty());

		ResponseEntity<?> response = eventSpaceService.add(convertToDTO(eventSpace1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no owner with the given id.", response.getBody());
	}

	@Test
	void testAddDuplicateEventSpace() {
		when(ownerRepository.findById(eventSpace1.getOwner().getOwnerId())).thenReturn(Optional.of(owner));
		when(eventSpaceRepository.findByAddress(eventSpace1.getAddress())).thenReturn(Optional.of(eventSpace1));

		ResponseEntity<?> response = eventSpaceService.add(convertToDTO(eventSpace1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This property already exists.", response.getBody());
	}

	@Test
	void testAddDuplicateResidence() {
		when(ownerRepository.findById(eventSpace1.getOwner().getOwnerId())).thenReturn(Optional.of(owner));
		when(residenceRepository.findByAddress(eventSpace1.getAddress())).thenReturn(Optional.of(residence1));

		ResponseEntity<?> response = eventSpaceService.add(convertToDTO(eventSpace1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This property already exists.", response.getBody());
	}

	@Test
	void testAddDuplicateOfficeSpace() {
		when(ownerRepository.findById(eventSpace1.getOwner().getOwnerId())).thenReturn(Optional.of(owner));
		when(officeSpaceRepository.findByAddress(eventSpace1.getAddress())).thenReturn(Optional.of(officeSpace1));

		ResponseEntity<?> response = eventSpaceService.add(convertToDTO(eventSpace1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This property already exists.", response.getBody());
	}

	@Test
	void testAdd() {
		when(ownerRepository.findById(eventSpace1.getOwner().getOwnerId())).thenReturn(Optional.of(owner));
		when(eventSpaceRepository.findByAddress(eventSpace1.getAddress())).thenReturn(Optional.empty());
		when(residenceRepository.findByAddress(eventSpace1.getAddress())).thenReturn(Optional.empty());
		when(officeSpaceRepository.findByAddress(eventSpace1.getAddress())).thenReturn(Optional.empty());
		when(eventSpaceRepository.save(any(EventSpace.class))).thenReturn(eventSpace1);

		ResponseEntity<?> response = eventSpaceService.add(convertToDTO(eventSpace1));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(eventSpace1), response.getBody());
	}

	@Test
	void testUpdateBadId() {
		when(eventSpaceRepository.findById(eventSpace1.getPropertyId())).thenReturn(Optional.empty());

		ResponseEntity<?> response = eventSpaceService.update(1, convertToDTO(eventSpace1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no event space with the given id.", response.getBody());
	}

	@Test
	void testUpdateMissingOwner() {
		when(eventSpaceRepository.findById(eventSpace1.getPropertyId())).thenReturn(Optional.of(eventSpace1));
		when(ownerRepository.findById(eventSpace1.getOwner().getOwnerId())).thenReturn(Optional.empty());

		ResponseEntity<?> response = eventSpaceService.update(1, convertToDTO(eventSpace1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no owner with the given id.", response.getBody());
	}

	@Test
	void testUpdateDuplicateEventSpace() {
		when(eventSpaceRepository.findById(eventSpace1.getPropertyId())).thenReturn(Optional.of(eventSpace1));
		when(ownerRepository.findById(eventSpace1.getOwner().getOwnerId())).thenReturn(Optional.of(owner));
		when(eventSpaceRepository.findByAddress(eventSpace1.getAddress())).thenReturn(Optional.of(eventSpace2));
		when(eventSpaceRepository.save(any(EventSpace.class)))
				.thenThrow(new RuntimeException("This property already exists."));

		ResponseEntity<?> response = eventSpaceService.update(eventSpace1.getPropertyId(), convertToDTO(eventSpace1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This property already exists.", response.getBody());
	}

	@Test
	void testUpdateDuplicateResidence() {
		when(eventSpaceRepository.findById(eventSpace1.getPropertyId())).thenReturn(Optional.of(eventSpace1));
		when(ownerRepository.findById(eventSpace1.getOwner().getOwnerId())).thenReturn(Optional.of(owner));
		when(residenceRepository.findByAddress(eventSpace1.getAddress())).thenReturn(Optional.of(residence1));
		when(eventSpaceRepository.save(any(EventSpace.class)))
				.thenThrow(new RuntimeException("This property already exists."));

		ResponseEntity<?> response = eventSpaceService.update(eventSpace1.getPropertyId(), convertToDTO(eventSpace1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This property already exists.", response.getBody());
	}

	@Test
	void testUpdateDuplicateOfficeSpace() {
		when(eventSpaceRepository.findById(eventSpace1.getPropertyId())).thenReturn(Optional.of(eventSpace1));
		when(ownerRepository.findById(eventSpace1.getOwner().getOwnerId())).thenReturn(Optional.of(owner));
		when(officeSpaceRepository.findByAddress(eventSpace1.getAddress())).thenReturn(Optional.of(officeSpace1));
		when(eventSpaceRepository.save(any(EventSpace.class)))
				.thenThrow(new RuntimeException("This property already exists."));

		ResponseEntity<?> response = eventSpaceService.update(eventSpace1.getPropertyId(), convertToDTO(eventSpace1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This property already exists.", response.getBody());
	}

	@Test
	void testUpdate() {
		when(eventSpaceRepository.findById(eventSpace1.getPropertyId())).thenReturn(Optional.of(eventSpace1));
		when(ownerRepository.findById(eventSpace1.getOwner().getOwnerId())).thenReturn(Optional.of(owner));
		when(eventSpaceRepository.findByAddress(eventSpace1.getAddress())).thenReturn(Optional.empty());
		when(eventSpaceRepository.save(any(EventSpace.class))).thenReturn(eventSpace1);

		ResponseEntity<?> response = eventSpaceService.update(1, convertToDTO(eventSpace1));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(eventSpace1), response.getBody());
	}

	@Test
	void testDeleteBadId() {
		when(eventSpaceRepository.findById(eventSpace1.getPropertyId())).thenReturn(Optional.empty());

		ResponseEntity<?> response = eventSpaceService.delete(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no event space with the given id.", response.getBody());
	}

	@Test
	void testDeleteAssociatedLease() {
		when(eventSpaceRepository.findById(eventSpace1.getPropertyId())).thenReturn(Optional.of(eventSpace1));
		when(leaseRepository.findAllByProperty_PropertyId(residence1.getPropertyId())).thenReturn(leases);

		ResponseEntity<?> response = eventSpaceService.delete(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("You cannot delete this event space since there are leases associated with it.",
				response.getBody());
	}

	@Test
	void testDeleteAssociatedUtilityLease() {
		when(eventSpaceRepository.findById(eventSpace1.getPropertyId())).thenReturn(Optional.of(eventSpace1));
		when(leaseRepository.findAllByProperty_PropertyId(eventSpace1.getPropertyId()))
				.thenReturn(new ArrayList<Lease>());
		when(utilityLeaseRepository.findAllByProperty_PropertyId(eventSpace1.getPropertyId()))
				.thenReturn(utilityLeases);

		ResponseEntity<?> response = eventSpaceService.delete(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("You cannot delete this event space since there are utility leases associated with it.",
				response.getBody());
	}

	@Test
	void testDelete() {
		when(eventSpaceRepository.findById(eventSpace1.getPropertyId())).thenReturn(Optional.of(eventSpace1));
		when(leaseRepository.findAllByProperty_PropertyId(eventSpace1.getPropertyId()))
				.thenReturn(new ArrayList<Lease>());
		when(utilityLeaseRepository.findAllByProperty_PropertyId(eventSpace1.getPropertyId()))
				.thenReturn(new ArrayList<UtilityLease>());

		ResponseEntity<?> response = eventSpaceService.delete(1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(eventSpace1), response.getBody());
	}

	private EventSpaceDTO convertToDTO(EventSpace eventSpace) {
		EventSpaceDTO eventSpaceDTO = new EventSpaceDTO();
		eventSpaceDTO.setPropertyId(eventSpace.getPropertyId());
		eventSpaceDTO.setName(eventSpace.getName());
		eventSpaceDTO.setAddress(eventSpace.getAddress());
		eventSpaceDTO.setDescription(eventSpace.getDescription());
		eventSpaceDTO.setRentalRate(eventSpace.getRentalRate());
		eventSpaceDTO.setSize(eventSpace.getSize());
		eventSpaceDTO.setIsAvailable(eventSpace.isAvailable());
		eventSpaceDTO.setNumberOfParkingSpots(eventSpace.getNumberOfParkingSpots());
		eventSpaceDTO.setPhotos(eventSpace.getPhotos());
		eventSpaceDTO.setCapacity(eventSpace.getCapacity());
		eventSpaceDTO.setHasKitchen(eventSpace.isHasKitchen());
		eventSpaceDTO.setHasBar(eventSpace.isHasBar());
		eventSpaceDTO.setOwnerId(eventSpace.getOwner().getOwnerId());

		return eventSpaceDTO;
	}

}
