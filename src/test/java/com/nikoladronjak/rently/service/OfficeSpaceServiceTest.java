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
import com.nikoladronjak.rently.dto.OfficeSpaceDTO;
import com.nikoladronjak.rently.repository.EventSpaceRepository;
import com.nikoladronjak.rently.repository.LeaseRepository;
import com.nikoladronjak.rently.repository.OfficeSpaceRepository;
import com.nikoladronjak.rently.repository.OwnerRepository;
import com.nikoladronjak.rently.repository.ResidenceRepository;
import com.nikoladronjak.rently.repository.UtilityLeaseRepository;

@SpringBootTest
class OfficeSpaceServiceTest {

	Owner owner;

	List<String> photos;

	Residence residence1;

	EventSpace eventSpace1;

	Lease lease;

	List<Lease> leases;

	UtilityLease utilityLease;

	List<UtilityLease> utilityLeases;

	OfficeSpace officeSpace1;

	OfficeSpace officeSpace2;

	@Mock
	private OwnerRepository ownerRepository;

	@Mock
	private ResidenceRepository residenceRepository;

	@Mock
	private EventSpaceRepository eventSpaceRepository;

	@Mock
	private LeaseRepository leaseRepository;

	@Mock
	private UtilityLeaseRepository utilityLeaseRepository;

	@Mock
	private OfficeSpaceRepository officeSpaceRepository;

	@InjectMocks
	private OfficeSpaceService officeSpaceService;

	@BeforeEach
	void setUp() throws Exception {
		owner = new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");

		photos = new ArrayList<String>();
		photos.add("photo1");
		photos.add("photo2");
		photos.add("photo3");

		residence1 = new Residence(1, "Apartement 1", "Jove Ilica 154", "", (double) 300, 30, true, 0, photos, owner,
				null, 1, 1, HeatingType.Central, true, true);

		eventSpace1 = new EventSpace(1, "Event Space 1", "Jove Ilica 154", "", (double) 300, 200, true, 20, photos,
				owner, null, 50, true, true, null);

		lease = new Lease(1, 300, new GregorianCalendar(2024, 11, 12), new GregorianCalendar(2025, 11, 12),
				officeSpace1, null, null);

		leases = new ArrayList<Lease>();
		leases.add(lease);

		utilityLease = new UtilityLease(1, (double) 0, null, officeSpace1, null);

		utilityLeases = new ArrayList<UtilityLease>();
		utilityLeases.add(utilityLease);

		officeSpace1 = new OfficeSpace(1, "Office Space 1", "Jove Ilica 154", "", (double) 400, 100, true, 20, photos,
				owner, null, 20, null);
		officeSpace2 = new OfficeSpace(2, "Office Space 2", "Studentski trg 1", "", (double) 300, 200, true, 20, photos,
				owner, null, 20, null);
	}

	@AfterEach
	void tearDown() throws Exception {
		owner = null;

		photos = null;

		residence1 = null;

		eventSpace1 = null;

		lease = null;

		leases = null;

		utilityLease = null;

		utilityLeases = null;

		officeSpace1 = null;
		officeSpace2 = null;
	}

	@Test
	void testGetAllError() {
		when(officeSpaceRepository.findAll()).thenThrow(new RuntimeException("Something went wrong"));

		ResponseEntity<?> response = officeSpaceService.getAll();

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("Something went wrong", response.getBody());
	}

	@Test
	void testGetAll() {
		List<OfficeSpace> officeSpaces = new ArrayList<OfficeSpace>();
		officeSpaces.add(officeSpace1);
		officeSpaces.add(officeSpace2);
		when(officeSpaceRepository.findAll()).thenReturn(officeSpaces);

		ResponseEntity<?> response = officeSpaceService.getAll();
		List<OfficeSpaceDTO> officeSpaceDTOs = officeSpaces.stream().map(this::convertToDTO)
				.collect(Collectors.toList());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(officeSpaceDTOs, response.getBody());
	}

	@Test
	void testGetByIdBadId() {
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = officeSpaceService.getById(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no office space with the given id.", response.getBody());
	}

	@Test
	void testGetById() {
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.of(officeSpace1));

		ResponseEntity<?> response = officeSpaceService.getById(1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(officeSpace1), response.getBody());
	}

	@Test
	void testAddMissingOwner() {
		when(ownerRepository.findById(officeSpace1.getOwner().getOwnerId())).thenReturn(Optional.empty());

		ResponseEntity<?> response = officeSpaceService.add(convertToDTO(officeSpace1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no owner with the given id.", response.getBody());
	}

	@Test
	void testAddDuplicateOfficeSpace() {
		when(ownerRepository.findById(officeSpace1.getOwner().getOwnerId())).thenReturn(Optional.of(owner));
		when(officeSpaceRepository.findByAddress(officeSpace1.getAddress())).thenReturn(Optional.of(officeSpace1));

		ResponseEntity<?> response = officeSpaceService.add(convertToDTO(officeSpace1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This property already exists.", response.getBody());
	}

	@Test
	void testAddDuplicateResidence() {
		when(ownerRepository.findById(officeSpace1.getOwner().getOwnerId())).thenReturn(Optional.of(owner));
		when(residenceRepository.findByAddress(officeSpace1.getAddress())).thenReturn(Optional.of(residence1));

		ResponseEntity<?> response = officeSpaceService.add(convertToDTO(officeSpace1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This property already exists.", response.getBody());
	}

	@Test
	void testAddDuplicateEventSpace() {
		when(ownerRepository.findById(officeSpace1.getOwner().getOwnerId())).thenReturn(Optional.of(owner));
		when(eventSpaceRepository.findByAddress(officeSpace1.getAddress())).thenReturn(Optional.of(eventSpace1));

		ResponseEntity<?> response = officeSpaceService.add(convertToDTO(officeSpace1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This property already exists.", response.getBody());
	}

	@Test
	void testAdd() {
		when(ownerRepository.findById(officeSpace1.getOwner().getOwnerId())).thenReturn(Optional.of(owner));
		when(officeSpaceRepository.findByAddress(officeSpace1.getAddress())).thenReturn(Optional.empty());
		when(eventSpaceRepository.findByAddress(officeSpace1.getAddress())).thenReturn(Optional.empty());
		when(residenceRepository.findByAddress(officeSpace1.getAddress())).thenReturn(Optional.empty());
		when(officeSpaceRepository.save(any(OfficeSpace.class))).thenReturn(officeSpace1);

		ResponseEntity<?> response = officeSpaceService.add(convertToDTO(officeSpace1));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(officeSpace1), response.getBody());
	}

	@Test
	void testUpdateBadId() {
		when(officeSpaceRepository.findById(officeSpace1.getPropertyId())).thenReturn(Optional.empty());

		ResponseEntity<?> response = officeSpaceService.update(1, convertToDTO(officeSpace1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no office space with the given id.", response.getBody());
	}

	@Test
	void testUpdateMissingOwner() {
		when(officeSpaceRepository.findById(officeSpace1.getPropertyId())).thenReturn(Optional.of(officeSpace1));
		when(ownerRepository.findById(officeSpace1.getOwner().getOwnerId())).thenReturn(Optional.empty());

		ResponseEntity<?> response = officeSpaceService.update(1, convertToDTO(officeSpace1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no owner with the given id.", response.getBody());
	}

	@Test
	void testUpdateDuplicateOfficeSpace() {
		when(officeSpaceRepository.findById(officeSpace1.getPropertyId())).thenReturn(Optional.of(officeSpace1));
		when(ownerRepository.findById(officeSpace1.getOwner().getOwnerId())).thenReturn(Optional.of(owner));
		when(officeSpaceRepository.findByAddress(officeSpace1.getAddress())).thenReturn(Optional.of(officeSpace2));
		when(officeSpaceRepository.save(any(OfficeSpace.class)))
				.thenThrow(new RuntimeException("This property already exists."));

		ResponseEntity<?> response = officeSpaceService.update(officeSpace1.getPropertyId(),
				convertToDTO(officeSpace1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This property already exists.", response.getBody());
	}

	@Test
	void testUpdateDuplicateResidence() {
		when(officeSpaceRepository.findById(officeSpace1.getPropertyId())).thenReturn(Optional.of(officeSpace1));
		when(ownerRepository.findById(officeSpace1.getOwner().getOwnerId())).thenReturn(Optional.of(owner));
		when(residenceRepository.findByAddress(officeSpace1.getAddress())).thenReturn(Optional.of(residence1));
		when(officeSpaceRepository.save(any(OfficeSpace.class)))
				.thenThrow(new RuntimeException("This property already exists."));

		ResponseEntity<?> response = officeSpaceService.update(officeSpace1.getPropertyId(),
				convertToDTO(officeSpace1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This property already exists.", response.getBody());
	}

	@Test
	void testUpdateDuplicateEventSpace() {
		when(officeSpaceRepository.findById(officeSpace1.getPropertyId())).thenReturn(Optional.of(officeSpace1));
		when(ownerRepository.findById(officeSpace1.getOwner().getOwnerId())).thenReturn(Optional.of(owner));
		when(eventSpaceRepository.findByAddress(officeSpace1.getAddress())).thenReturn(Optional.of(eventSpace1));
		when(officeSpaceRepository.save(any(OfficeSpace.class)))
				.thenThrow(new RuntimeException("This property already exists."));

		ResponseEntity<?> response = officeSpaceService.update(officeSpace1.getPropertyId(),
				convertToDTO(officeSpace1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This property already exists.", response.getBody());
	}

	@Test
	void testUpdate() {
		when(officeSpaceRepository.findById(officeSpace1.getPropertyId())).thenReturn(Optional.of(officeSpace1));
		when(ownerRepository.findById(officeSpace1.getOwner().getOwnerId())).thenReturn(Optional.of(owner));
		when(officeSpaceRepository.findByAddress(officeSpace1.getAddress())).thenReturn(Optional.empty());
		when(officeSpaceRepository.save(any(OfficeSpace.class))).thenReturn(officeSpace1);

		ResponseEntity<?> response = officeSpaceService.update(1, convertToDTO(officeSpace1));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(officeSpace1), response.getBody());
	}

	@Test
	void testDeleteBadId() {
		when(officeSpaceRepository.findById(officeSpace1.getPropertyId())).thenReturn(Optional.empty());

		ResponseEntity<?> response = officeSpaceService.delete(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no office space with the given id.", response.getBody());
	}

	@Test
	void testDeleteAssociatedLease() {
		when(officeSpaceRepository.findById(officeSpace1.getPropertyId())).thenReturn(Optional.of(officeSpace1));
		when(leaseRepository.findAllByProperty_PropertyId(officeSpace1.getPropertyId())).thenReturn(leases);

		ResponseEntity<?> response = officeSpaceService.delete(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("You cannot delete this office space since there are leases associated with it.",
				response.getBody());
	}

	@Test
	void testDeleteAssociatedUtilityLease() {
		when(officeSpaceRepository.findById(officeSpace1.getPropertyId())).thenReturn(Optional.of(officeSpace1));
		when(leaseRepository.findAllByProperty_PropertyId(officeSpace1.getPropertyId()))
				.thenReturn(new ArrayList<Lease>());
		when(utilityLeaseRepository.findAllByProperty_PropertyId(officeSpace1.getPropertyId()))
				.thenReturn(utilityLeases);

		ResponseEntity<?> response = officeSpaceService.delete(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("You cannot delete this office space since there are utility leases associated with it.",
				response.getBody());
	}

	@Test
	void testDelete() {
		when(officeSpaceRepository.findById(officeSpace1.getPropertyId())).thenReturn(Optional.of(officeSpace1));
		when(leaseRepository.findAllByProperty_PropertyId(officeSpace1.getPropertyId()))
				.thenReturn(new ArrayList<Lease>());

		ResponseEntity<?> response = officeSpaceService.delete(1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(officeSpace1), response.getBody());
	}

	private OfficeSpaceDTO convertToDTO(OfficeSpace officeSpace) {
		OfficeSpaceDTO officeSpaceDTO = new OfficeSpaceDTO();
		officeSpaceDTO.setPropertyId(officeSpace.getPropertyId());
		officeSpaceDTO.setName(officeSpace.getName());
		officeSpaceDTO.setAddress(officeSpace.getAddress());
		officeSpaceDTO.setDescription(officeSpace.getDescription());
		officeSpaceDTO.setRentalRate(officeSpace.getRentalRate());
		officeSpaceDTO.setSize(officeSpace.getSize());
		officeSpaceDTO.setIsAvailable(officeSpace.isAvailable());
		officeSpaceDTO.setNumberOfParkingSpots(officeSpace.getNumberOfParkingSpots());
		officeSpaceDTO.setPhotos(officeSpace.getPhotos());
		officeSpaceDTO.setCapacity(officeSpace.getCapacity());
		officeSpaceDTO.setOwnerId(officeSpace.getOwner().getOwnerId());

		return officeSpaceDTO;
	}

}
