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
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nikoladronjak.rently.domain.Customer;
import com.nikoladronjak.rently.domain.Lease;
import com.nikoladronjak.rently.domain.OfficeSpace;
import com.nikoladronjak.rently.domain.Owner;
import com.nikoladronjak.rently.domain.Rent;
import com.nikoladronjak.rently.domain.Utility;
import com.nikoladronjak.rently.domain.UtilityLease;
import com.nikoladronjak.rently.dto.RentDTO;
import com.nikoladronjak.rently.repository.LeaseRepository;
import com.nikoladronjak.rently.repository.RentRepository;
import com.nikoladronjak.rently.repository.ResidenceRepository;
import com.nikoladronjak.rently.repository.UtilityLeaseRepository;

@SpringBootTest
class RentServiceTest {

	Utility utility1;

	Utility utility2;

	Owner owner;

	Customer customer;

	OfficeSpace officeSpace1;

	OfficeSpace officeSpace2;

	Lease lease1;

	Lease lease2;

	UtilityLease utilityLease1;

	UtilityLease utilityLease2;

	List<UtilityLease> utilityLeases;

	Rent rent1;

	Rent rent2;

	List<Rent> rents;

	@Mock
	private UtilityLeaseRepository utilityLeaseRepository;

	@Mock
	private ResidenceRepository residenceRepository;

	@Mock
	private LeaseRepository leaseRepository;

	@Mock
	private RentRepository rentRepository;

	@InjectMocks
	private RentService rentService;

	@BeforeEach
	void setUp() throws Exception {
		List<String> photos = new ArrayList<String>();
		photos.add("photo1");
		photos.add("photo2");

		rents = new ArrayList<Rent>();
		rents.add(rent1);
		rents.add(rent2);

		utility1 = new Utility(1, "Microphone", "", null);
		utility2 = new Utility(2, "Projector", "", null);

		owner = new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");

		customer = new Customer(1, "Mika", "Mikic", "mika@gmail.com", "mika123", null);

		officeSpace1 = new OfficeSpace(1, "Office Space 1", "Jove Ilica 154", "", (double) 300, 150, true, 30, photos,
				owner, null, 100, null);
		officeSpace2 = new OfficeSpace(2, "Office Space 2", "Studentski trg 1", "", (double) 250, 120, true, 20, photos,
				owner, null, 90, null);

		lease1 = new Lease(1, 200, new GregorianCalendar(2024, 11, 12), new GregorianCalendar(2025, 11, 12),
				officeSpace1, customer, null);
		lease2 = new Lease(2, 250, new GregorianCalendar(2024, 11, 12), new GregorianCalendar(2025, 11, 12),
				officeSpace2, customer, null);

		utilityLease1 = new UtilityLease(1, (double) 40, utility1, officeSpace1, rents);
		utilityLease2 = new UtilityLease(2, (double) 60, utility2, officeSpace1, rents);

		utilityLeases = new ArrayList<UtilityLease>();
		utilityLeases.add(utilityLease1);
		utilityLeases.add(utilityLease2);

		rent1 = new Rent(1, 300, utilityLeases, lease1);
		rent2 = new Rent(2, 350, utilityLeases, lease2);
	}

	@AfterEach
	void tearDown() throws Exception {
		utility1 = null;
		utility2 = null;

		owner = null;

		customer = null;

		officeSpace1 = null;
		officeSpace2 = null;

		lease1 = null;
		lease2 = null;

		utilityLeases = null;

		utilityLease1 = null;
		utilityLease2 = null;

		rent1 = null;
		rent2 = null;

		rents = null;
	}

	@Test
	void testGetAllError() {
		when(rentRepository.findAll()).thenThrow(new RuntimeException("Something went wrong"));

		ResponseEntity<?> response = rentService.getAll();

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("Something went wrong", response.getBody());
	}

	@Test
	void testGetAll() {
		List<Rent> rents = new ArrayList<Rent>();
		rents.add(rent1);
		rents.add(rent2);
		when(rentRepository.findAll()).thenReturn(rents);

		ResponseEntity<?> response = rentService.getAll();
		List<RentDTO> rentDTOs = rents.stream().map(this::convertToDTO).collect(Collectors.toList());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(rentDTOs, response.getBody());
	}

	@Test
	void testGetByIdBadId() {
		when(rentRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = rentService.getById(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no rent with the given id.", response.getBody());
	}

	@Test
	void testGetById() {
		when(rentRepository.findById(1)).thenReturn(Optional.of(rent1));

		ResponseEntity<?> response = rentService.getById(1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(rent1), response.getBody());
	}

	@Test
	void testAddBadLeaseId() {
		when(leaseRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = rentService.add(convertToDTO(rent1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no lease for the given leaseId.", response.getBody());
	}

	@Test
	void testAddBadUtilityLeaseId() {
		when(leaseRepository.findById(1)).thenReturn(Optional.of(lease1));
		when(utilityLeaseRepository.findById(1)).thenReturn(Optional.empty());

		RentDTO rentDTO = new RentDTO();
		rentDTO.setLeaseId(lease1.getLeaseId());
		rentDTO.setUtilityLeaseIds(List.of(1));
		rentDTO.setTotalRent((double) 300);

		ResponseEntity<?> response = rentService.add(rentDTO);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no utility lease for the utilityLeaseId: " + 1, response.getBody());
	}

	@Test
	void testAddDuplicate() {
		when(leaseRepository.findById(1)).thenReturn(Optional.of(lease1));
		when(utilityLeaseRepository.findById(1)).thenReturn(Optional.of(utilityLease1));
		when(utilityLeaseRepository.findById(2)).thenReturn(Optional.of(utilityLease2));
		when(rentRepository.findByLease_LeaseId(1)).thenReturn(Optional.of(rent1));

		ResponseEntity<?> response = rentService.add(convertToDTO(rent1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This rent already exists.", response.getBody());
	}

	@Test
	void testAdd() {
		when(leaseRepository.findById(1)).thenReturn(Optional.of(lease1));
		when(utilityLeaseRepository.findById(1)).thenReturn(Optional.of(utilityLease1));
		when(utilityLeaseRepository.findById(2)).thenReturn(Optional.of(utilityLease2));
		when(rentRepository.findByLease_LeaseId(1)).thenReturn(Optional.empty());
		when(rentRepository.save(any(Rent.class))).thenReturn(rent1);

		ResponseEntity<?> response = rentService.add(convertToDTO(rent1));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(rent1), response.getBody());
	}

	@Test
	void testUpdateBadId() {
		when(rentRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = rentService.update(1, convertToDTO(rent1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no rent for the given id.", response.getBody());
	}

	@Test
	void testUpdateBadLeaseId() {
		when(rentRepository.findById(1)).thenReturn(Optional.of(rent1));
		when(leaseRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = rentService.update(1, convertToDTO(rent1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no lease for the given leaseId.", response.getBody());
	}

	@Test
	void testUpdateBadUtilityLeaseId() {
		when(rentRepository.findById(1)).thenReturn(Optional.of(rent1));
		when(leaseRepository.findById(1)).thenReturn(Optional.of(lease1));
		when(utilityLeaseRepository.findById(1)).thenReturn(Optional.empty());

		RentDTO rentDTO = new RentDTO();
		rentDTO.setLeaseId(lease1.getLeaseId());
		rentDTO.setUtilityLeaseIds(List.of(1));
		rentDTO.setTotalRent((double) 300);

		ResponseEntity<?> response = rentService.update(1, rentDTO);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no utility lease for the utilityLeaseId: " + 1, response.getBody());
	}

	@Test
	void testUpdateDuplicate() {
		when(rentRepository.findById(1)).thenReturn(Optional.of(rent1));
		when(leaseRepository.findById(1)).thenReturn(Optional.of(lease1));
		when(utilityLeaseRepository.findById(1)).thenReturn(Optional.of(utilityLease1));
		when(utilityLeaseRepository.findById(2)).thenReturn(Optional.of(utilityLease2));
		when(rentRepository.findByLease_LeaseId(1)).thenReturn(Optional.of(rent1));
		when(rentRepository.save(any(Rent.class))).thenThrow(new RuntimeException("This rent already exists."));

		ResponseEntity<?> response = rentService.update(1, convertToDTO(rent1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This rent already exists.", response.getBody());
	}

	@Test
	void testUpdate() {
		when(rentRepository.findById(1)).thenReturn(Optional.of(rent1));
		when(leaseRepository.findById(1)).thenReturn(Optional.of(lease1));
		when(utilityLeaseRepository.findById(1)).thenReturn(Optional.of(utilityLease1));
		when(utilityLeaseRepository.findById(2)).thenReturn(Optional.of(utilityLease2));
		when(rentRepository.findByLease_LeaseId(1)).thenReturn(Optional.empty());
		when(rentRepository.save(any(Rent.class))).thenReturn(rent1);

		ResponseEntity<?> response = rentService.update(1, convertToDTO(rent1));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(rent1), response.getBody());
	}

	@Test
	void testDeleteBadId() {
		when(rentRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = rentService.delete(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no rent with the given id.", response.getBody());
	}

	@Test
	void testDelete() {
		when(rentRepository.findById(1)).thenReturn(Optional.of(rent1));
		Rent rentMock = Mockito.mock(Rent.class);
		when(rentMock.getUtilityLeases()).thenReturn(utilityLeases);

		ResponseEntity<?> response = rentService.delete(1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(rent1), response.getBody());
	}

	private RentDTO convertToDTO(Rent rent) {
		List<Integer> utilityLeaseIds = rent.getUtilityLeases().stream()
				.map(utilityLease -> utilityLease.getUtilityLeaseId()).collect(Collectors.toList());

		RentDTO rentDTO = new RentDTO();
		rentDTO.setLeaseId(rent.getLease().getLeaseId());
		rentDTO.setUtilityLeaseIds(utilityLeaseIds);
		rentDTO.setTotalRent(rent.getTotalRent());

		return rentDTO;
	}

}
