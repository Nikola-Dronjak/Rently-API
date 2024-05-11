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

import com.nikoladronjak.rently.domain.Customer;
import com.nikoladronjak.rently.domain.EventSpace;
import com.nikoladronjak.rently.domain.HeatingType;
import com.nikoladronjak.rently.domain.OfficeSpace;
import com.nikoladronjak.rently.domain.Owner;
import com.nikoladronjak.rently.domain.Residence;
import com.nikoladronjak.rently.dto.OwnerDTO;
import com.nikoladronjak.rently.repository.CustomerRepository;
import com.nikoladronjak.rently.repository.EventSpaceRepository;
import com.nikoladronjak.rently.repository.OfficeSpaceRepository;
import com.nikoladronjak.rently.repository.OwnerRepository;
import com.nikoladronjak.rently.repository.ResidenceRepository;

@SpringBootTest
class OwnerServiceTest {

	Residence residence1;

	List<Residence> residences;

	EventSpace eventSpace1;

	List<EventSpace> eventSpaces;

	OfficeSpace officeSpace1;

	List<OfficeSpace> officeSpaces;

	Customer customer1;

	Owner owner1;

	Owner owner2;

	@Mock
	private ResidenceRepository residenceRepository;

	@Mock
	private EventSpaceRepository eventSpaceRepository;

	@Mock
	private OfficeSpaceRepository officeSpaceRepository;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private OwnerRepository ownerRepository;

	@InjectMocks
	private OwnerService ownerService;

	@BeforeEach
	void setUp() throws Exception {
		residence1 = new Residence(1, "Apartement 1", "Jove Ilica 154", "", 300, 30, true, 0, null, owner1, null, 1, 1,
				HeatingType.Central, true, true);

		residences = new ArrayList<Residence>();
		residences.add(residence1);

		eventSpace1 = new EventSpace(1, "Event Space 1", "Jove Ilica 154", "", 300, 200, true, 20, null, owner1, null,
				50, true, true, null);

		eventSpaces = new ArrayList<EventSpace>();
		eventSpaces.add(eventSpace1);

		officeSpace1 = new OfficeSpace(1, "Office Space 1", "Jove Ilica 154", "", 400, 100, true, 20, null, owner1,
				null, 20, null);

		officeSpaces = new ArrayList<OfficeSpace>();
		officeSpaces.add(officeSpace1);

		customer1 = new Customer(1, "Jovan", "Jovanovic", "jovan@gmail.com", "jovan123", null);

		owner1 = new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");
		owner2 = new Owner(2, "Mika", "Mikic", "mika@gmail.com", "mika123", "0987654321");
	}

	@AfterEach
	void tearDown() throws Exception {
		residence1 = null;

		residences = null;

		eventSpace1 = null;

		eventSpaces = null;

		officeSpace1 = null;

		officeSpaces = null;

		customer1 = null;

		owner1 = null;
		owner2 = null;
	}

	@Test
	void testGetAllError() {
		when(ownerRepository.findAll()).thenThrow(new RuntimeException("Something went wrong"));

		ResponseEntity<?> response = ownerService.getAll();

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("Something went wrong", response.getBody());
	}

	@Test
	void testGetAll() {
		List<Owner> owners = new ArrayList<Owner>();
		owners.add(owner1);
		owners.add(owner2);
		when(ownerRepository.findAll()).thenReturn(owners);

		ResponseEntity<?> response = ownerService.getAll();
		List<OwnerDTO> ownerDTOs = owners.stream().map(this::convertToDTO).collect(Collectors.toList());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(ownerDTOs, response.getBody());
	}

	@Test
	void testGetByIdBadId() {
		when(ownerRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = ownerService.getById(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no owner with the given id.", response.getBody());
	}

	@Test
	void testGetById() {
		when(ownerRepository.findById(1)).thenReturn(Optional.of(owner1));

		ResponseEntity<?> response = ownerService.getById(1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(owner1), response.getBody());
	}

	@Test
	void testAddDuplicateOwner() {
		when(ownerRepository.findByEmail(owner1.getEmail())).thenReturn(Optional.of(owner1));

		ResponseEntity<?> response = ownerService.add(convertToDTO(owner1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This user already exists.", response.getBody());
	}

	@Test
	void testAddDuplicateCustomer() {
		when(customerRepository.findByEmail(owner1.getEmail())).thenReturn(Optional.of(customer1));

		ResponseEntity<?> response = ownerService.add(convertToDTO(owner1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This user already exists.", response.getBody());
	}

	@Test
	void testAdd() {
		when(ownerRepository.findByEmail(owner1.getEmail())).thenReturn(Optional.empty());
		when(customerRepository.findByEmail(owner1.getEmail())).thenReturn(Optional.empty());
		when(ownerRepository.save(any(Owner.class))).thenReturn(owner1);

		ResponseEntity<?> response = ownerService.add(convertToDTO(owner1));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(owner1), response.getBody());
	}

	@Test
	void testUpdateBadId() {
		when(ownerRepository.findById(owner1.getOwnerId())).thenReturn(Optional.empty());

		ResponseEntity<?> response = ownerService.update(1, convertToDTO(owner1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no owner with the given id.", response.getBody());
	}

	@Test
	void testUpdateDuplicateOwner() {
		when(ownerRepository.findById(owner1.getOwnerId())).thenReturn(Optional.of(owner1));
		when(ownerRepository.findByEmail(owner1.getEmail())).thenReturn(Optional.of(owner1));
		when(ownerRepository.save(any(Owner.class))).thenThrow(new RuntimeException("This user already exists."));

		ResponseEntity<?> response = ownerService.update(owner1.getOwnerId(), convertToDTO(owner1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This user already exists.", response.getBody());
	}

	@Test
	void testUpdateDuplicateCustomer() {
		when(ownerRepository.findById(owner1.getOwnerId())).thenReturn(Optional.of(owner1));
		when(customerRepository.findByEmail(owner1.getEmail())).thenReturn(Optional.of(customer1));
		when(ownerRepository.save(any(Owner.class))).thenThrow(new RuntimeException("This user already exists."));

		ResponseEntity<?> response = ownerService.update(owner1.getOwnerId(), convertToDTO(owner1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This user already exists.", response.getBody());
	}

	@Test
	void testUpdate() {
		when(ownerRepository.findById(owner1.getOwnerId())).thenReturn(Optional.of(owner1));
		when(ownerRepository.findByEmail(owner1.getEmail())).thenReturn(Optional.empty());
		when(ownerRepository.save(any(Owner.class))).thenReturn(owner1);

		ResponseEntity<?> response = ownerService.update(1, convertToDTO(owner1));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(owner1), response.getBody());
	}

	@Test
	void testDeleteBadId() {
		when(ownerRepository.findById(owner1.getOwnerId())).thenReturn(Optional.empty());

		ResponseEntity<?> response = ownerService.delete(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no owner with the given id.", response.getBody());
	}

	@Test
	void testDeleteAssociatedResidence() {
		when(ownerRepository.findById(owner1.getOwnerId())).thenReturn(Optional.of(owner1));
		when(residenceRepository.findAllByOwner_OwnerId(owner1.getOwnerId())).thenReturn(residences);

		ResponseEntity<?> response = ownerService.delete(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("You cannot delete this owner since there are properties associated with him.",
				response.getBody());
	}

	@Test
	void testDeleteAssociatedEventSpace() {
		when(ownerRepository.findById(owner1.getOwnerId())).thenReturn(Optional.of(owner1));
		when(eventSpaceRepository.findAllByOwner_OwnerId(owner1.getOwnerId())).thenReturn(eventSpaces);

		ResponseEntity<?> response = ownerService.delete(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("You cannot delete this owner since there are properties associated with him.",
				response.getBody());
	}

	@Test
	void testDeleteAssociatedOfficeSpace() {
		when(ownerRepository.findById(owner1.getOwnerId())).thenReturn(Optional.of(owner1));
		when(officeSpaceRepository.findAllByOwner_OwnerId(owner1.getOwnerId())).thenReturn(officeSpaces);

		ResponseEntity<?> response = ownerService.delete(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("You cannot delete this owner since there are properties associated with him.",
				response.getBody());
	}

	@Test
	void testDelete() {
		when(ownerRepository.findById(owner1.getOwnerId())).thenReturn(Optional.of(owner1));
		when(residenceRepository.findAllByOwner_OwnerId(owner1.getOwnerId())).thenReturn(new ArrayList<Residence>());
		when(eventSpaceRepository.findAllByOwner_OwnerId(owner1.getOwnerId())).thenReturn(new ArrayList<EventSpace>());
		when(officeSpaceRepository.findAllByOwner_OwnerId(owner1.getOwnerId()))
				.thenReturn(new ArrayList<OfficeSpace>());

		ResponseEntity<?> response = ownerService.delete(1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(owner1), response.getBody());
	}

	private OwnerDTO convertToDTO(Owner owner) {
		OwnerDTO ownerDTO = new OwnerDTO();
		ownerDTO.setFirstName(owner.getFirstName());
		ownerDTO.setLastName(owner.getLastName());
		ownerDTO.setEmail(owner.getEmail());
		ownerDTO.setPassword(owner.getPassword());
		ownerDTO.setPhoneNumber(owner.getPhoneNumber());

		return ownerDTO;
	}

}
