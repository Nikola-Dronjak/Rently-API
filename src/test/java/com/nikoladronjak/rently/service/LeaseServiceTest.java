package com.nikoladronjak.rently.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
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

import com.nikoladronjak.rently.domain.Customer;
import com.nikoladronjak.rently.domain.EventSpace;
import com.nikoladronjak.rently.domain.HeatingType;
import com.nikoladronjak.rently.domain.Lease;
import com.nikoladronjak.rently.domain.OfficeSpace;
import com.nikoladronjak.rently.domain.Owner;
import com.nikoladronjak.rently.domain.Rent;
import com.nikoladronjak.rently.domain.Residence;
import com.nikoladronjak.rently.dto.LeaseDTO;
import com.nikoladronjak.rently.repository.CustomerRepository;
import com.nikoladronjak.rently.repository.EventSpaceRepository;
import com.nikoladronjak.rently.repository.LeaseRepository;
import com.nikoladronjak.rently.repository.OfficeSpaceRepository;
import com.nikoladronjak.rently.repository.OwnerRepository;
import com.nikoladronjak.rently.repository.RentRepository;
import com.nikoladronjak.rently.repository.ResidenceRepository;

@SpringBootTest
class LeaseServiceTest {

	Residence residence1;

	Residence residence2;

	EventSpace eventSpace1;

	EventSpace eventSpace2;

	OfficeSpace officeSpace1;

	OfficeSpace officeSpace2;

	Owner owner;

	Customer customer;

	Rent rent;

	Lease lease1;

	Lease lease2;

	@Mock
	private OwnerRepository ownerRepository;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private ResidenceRepository residenceRepository;

	@Mock
	private EventSpaceRepository eventSpaceRepository;

	@Mock
	private OfficeSpaceRepository officeSpaceRepository;

	@Mock
	private RentRepository rentRepository;

	@Mock
	private LeaseRepository leaseRepository;

	@InjectMocks
	private LeaseService leaseService;

	@BeforeEach
	void setUp() throws Exception {
		List<String> photos = new ArrayList<String>();
		photos.add("photo1");
		photos.add("photo2");

		residence1 = new Residence(1, "Lux Apartment 1", "Jove Ilica 154", "", 400, 70, true, 2, photos, owner, null, 2,
				2, HeatingType.Central, true, true);
		residence2 = new Residence(2, "Lux Apartment 2", "Studentski trg 1", "", 300, 60, false, 2, photos, owner, null,
				2, 1, HeatingType.Central, true, true);

		eventSpace1 = new EventSpace(1, "Lux Event Space 1", "Bulevar Osolobodjenja 13", "", 250, 100, true, 50, photos,
				owner, null, 200, true, true, null);
		eventSpace2 = new EventSpace(2, "Lux Event Space 2", "Milentija Popovica 5", "", 350, 200, false, 50, photos,
				owner, null, 200, true, true, null);

		officeSpace1 = new OfficeSpace(1, "Office 1", "Bulevar Kralja Aleksandra 1", "", 400, 100, true, 30, photos,
				owner, null, 50, null);
		officeSpace2 = new OfficeSpace(2, "Office 2", "Bulevar Kralja Aleksandra 2", "", 500, 100, false, 30, photos,
				owner, null, 50, null);

		owner = new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");

		customer = new Customer(1, "Mika", "Mikic", "mika@gmail.com", "mika123", null);

		rent = new Rent(1, 400, null, lease1);

		lease1 = new Lease(1, 400, new GregorianCalendar(), new GregorianCalendar(2025, 12, 31), residence1, customer,
				null);
		lease2 = new Lease(2, 400, new GregorianCalendar(), new GregorianCalendar(2025, 12, 31), residence2, customer,
				null);
	}

	@AfterEach
	void tearDown() throws Exception {
		residence1 = null;
		residence2 = null;

		owner = null;

		customer = null;

		rent = null;

		lease1 = null;
		lease2 = null;
	}

	@Test
	void testGetAllError() {
		when(leaseRepository.findAll()).thenThrow(new RuntimeException("Something went wrong"));

		ResponseEntity<?> response = leaseService.getAll();

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("Something went wrong", response.getBody());
	}

	@Test
	void testGetAll() {
		List<Lease> leases = new ArrayList<Lease>();
		leases.add(lease1);
		leases.add(lease2);
		when(leaseRepository.findAll()).thenReturn(leases);

		ResponseEntity<?> response = leaseService.getAll();
		List<LeaseDTO> leaseDTOs = leases.stream().map(this::convertToDTO).collect(Collectors.toList());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(leaseDTOs, response.getBody());
	}

	@Test
	void testGetAllByPropertyIdError() {
		when(leaseRepository.findAllByProperty_PropertyId(1)).thenThrow(new RuntimeException("Something went wrong"));

		ResponseEntity<?> response = leaseService.getAllByPropertyId(1);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("Something went wrong", response.getBody());
	}

	@Test
	void testGetAllByPropertyId() {
		List<Lease> leases = new ArrayList<Lease>();
		leases.add(lease1);
		when(leaseRepository.findAllByProperty_PropertyId(1)).thenReturn(leases);

		ResponseEntity<?> response = leaseService.getAllByPropertyId(1);
		List<LeaseDTO> leaseDTOs = leases.stream().map(this::convertToDTO).collect(Collectors.toList());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(leaseDTOs, response.getBody());
	}

	@Test
	void testGetAllByCustomerIdError() {
		when(leaseRepository.findAllByCustomer_CustomerId(1)).thenThrow(new RuntimeException("Something went wrong"));

		ResponseEntity<?> response = leaseService.getAllByCustomerId(1);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals("Something went wrong", response.getBody());
	}

	@Test
	void testGetAllByCustomerId() {
		List<Lease> leases = new ArrayList<Lease>();
		leases.add(lease1);
		leases.add(lease2);
		when(leaseRepository.findAllByCustomer_CustomerId(1)).thenReturn(leases);

		ResponseEntity<?> response = leaseService.getAllByCustomerId(1);
		List<LeaseDTO> leaseDTOs = leases.stream().map(this::convertToDTO).collect(Collectors.toList());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(leaseDTOs, response.getBody());
	}

	@Test
	void testGetByIdBadId() {
		when(leaseRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = leaseService.getById(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no lease with the given id.", response.getBody());
	}

	@Test
	void testGetById() {
		when(leaseRepository.findById(1)).thenReturn(Optional.of(lease1));

		ResponseEntity<?> response = leaseService.getById(1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(lease1), response.getBody());
	}

	@Test
	void testAddBadPropertyId() {
		when(residenceRepository.findById(1)).thenReturn(Optional.empty());
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = leaseService.add(convertToDTO(lease1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("The property has to be either a residence, an event space or an office space.",
				response.getBody());
	}

	@Test
	void testAddResidenceUnavailable() {
		when(residenceRepository.findById(1)).thenReturn(Optional.of(residence2));
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = leaseService.add(convertToDTO(lease1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This property is currently unavailable.", response.getBody());
	}

	@Test
	void testAddEventSpaceUnavailable() {
		when(residenceRepository.findById(1)).thenReturn(Optional.empty());
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.of(eventSpace2));
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = leaseService.add(convertToDTO(lease1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This property is currently unavailable.", response.getBody());
	}

	@Test
	void testAddOfficeSpaceUnavailable() {
		when(residenceRepository.findById(1)).thenReturn(Optional.empty());
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.of(officeSpace2));

		ResponseEntity<?> response = leaseService.add(convertToDTO(lease1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This property is currently unavailable.", response.getBody());
	}

	@Test
	void testAddBadCustomerId() {
		when(residenceRepository.findById(1)).thenReturn(Optional.of(residence1));
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(customerRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = leaseService.add(convertToDTO(lease1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no customer for the given customerId.", response.getBody());
	}

	@Test
	void testAddBadStartDate() {
		when(residenceRepository.findById(1)).thenReturn(Optional.of(residence1));
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
		when(leaseRepository.save(any(Lease.class))).thenThrow(
				new RuntimeException("The start date of the lease has to be before the end date of the lease."));

		LeaseDTO leaseDTO = convertToDTO(lease1);
		GregorianCalendar startDate = lease1.getEndDate();
		startDate.add(Calendar.DAY_OF_MONTH, 1);
		leaseDTO.setStartDate(startDate);

		ResponseEntity<?> response = leaseService.add(leaseDTO);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("The start date of the lease has to be before the end date of the lease.", response.getBody());
	}

	@Test
	void testAddBadEndDate() {
		when(residenceRepository.findById(1)).thenReturn(Optional.of(residence1));
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
		when(leaseRepository.save(any(Lease.class))).thenThrow(
				new RuntimeException("The end date of the lease has to be after the start date of the lease."));

		LeaseDTO leaseDTO = convertToDTO(lease1);
		GregorianCalendar endDate = lease1.getStartDate();
		endDate.add(Calendar.DAY_OF_MONTH, -1);
		leaseDTO.setEndDate(endDate);

		ResponseEntity<?> response = leaseService.add(leaseDTO);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("The end date of the lease has to be after the start date of the lease.", response.getBody());
	}

	@Test
	void testAddDuplicate() {
		when(residenceRepository.findById(1)).thenReturn(Optional.of(residence1));
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
		when(leaseRepository.findByProperty_PropertyIdAndCustomer_CustomerId(1, 1)).thenReturn(Optional.of(lease1));
		when(leaseRepository.save(any(Lease.class))).thenThrow(new RuntimeException("This lease already exists."));

		ResponseEntity<?> response = leaseService.add(convertToDTO(lease1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This lease already exists.", response.getBody());
	}

	@Test
	void testAddResidence() {
		when(residenceRepository.findById(1)).thenReturn(Optional.of(residence1));
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
		when(leaseRepository.findByProperty_PropertyIdAndCustomer_CustomerId(1, 1)).thenReturn(Optional.empty());
		when(leaseRepository.save(any(Lease.class))).thenReturn(lease1);

		ResponseEntity<?> response = leaseService.add(convertToDTO(lease1));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(lease1), response.getBody());
		assertFalse(residence1.isAvailable());
	}

	@Test
	void testAddEventSpace() {
		when(residenceRepository.findById(1)).thenReturn(Optional.empty());
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.of(eventSpace1));
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
		when(leaseRepository.findByProperty_PropertyIdAndCustomer_CustomerId(1, 1)).thenReturn(Optional.empty());
		when(leaseRepository.save(any(Lease.class))).thenReturn(lease1);

		ResponseEntity<?> response = leaseService.add(convertToDTO(lease1));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(lease1), response.getBody());
		assertFalse(eventSpace1.isAvailable());
	}

	@Test
	void testAddOfficeSpace() {
		when(residenceRepository.findById(1)).thenReturn(Optional.empty());
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.of(officeSpace1));
		when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
		when(leaseRepository.findByProperty_PropertyIdAndCustomer_CustomerId(1, 1)).thenReturn(Optional.empty());
		when(leaseRepository.save(any(Lease.class))).thenReturn(lease1);

		ResponseEntity<?> response = leaseService.add(convertToDTO(lease1));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(lease1), response.getBody());
		assertFalse(officeSpace1.isAvailable());
	}

	@Test
	void testUpdateBadId() {
		when(leaseRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = leaseService.update(1, convertToDTO(lease1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no lease with the given id.", response.getBody());
	}

	@Test
	void testUpdateBadPropertyId() {
		when(leaseRepository.findById(1)).thenReturn(Optional.of(lease1));
		when(residenceRepository.findById(1)).thenReturn(Optional.empty());
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = leaseService.update(1, convertToDTO(lease1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("The property has to be either a residence, an event space or an office space.",
				response.getBody());
	}

	@Test
	void testUpdateBadCustomerId() {
		when(leaseRepository.findById(1)).thenReturn(Optional.of(lease1));
		when(residenceRepository.findById(1)).thenReturn(Optional.of(residence1));
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(customerRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = leaseService.update(1, convertToDTO(lease1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no customer for the given customerId.", response.getBody());
	}

	@Test
	void testUpdateBadStartDate() {
		when(leaseRepository.findById(1)).thenReturn(Optional.of(lease1));
		when(residenceRepository.findById(1)).thenReturn(Optional.of(residence1));
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
		when(leaseRepository.save(any(Lease.class))).thenThrow(
				new RuntimeException("The start date of the lease has to be before the end date of the lease."));

		LeaseDTO leaseDTO = convertToDTO(lease1);
		GregorianCalendar startDate = lease1.getEndDate();
		startDate.add(Calendar.DAY_OF_MONTH, 1);
		leaseDTO.setStartDate(startDate);

		ResponseEntity<?> response = leaseService.update(1, leaseDTO);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("The start date of the lease has to be before the end date of the lease.", response.getBody());
	}

	@Test
	void testUpdateBadEndDate() {
		when(leaseRepository.findById(1)).thenReturn(Optional.of(lease1));
		when(residenceRepository.findById(1)).thenReturn(Optional.of(residence1));
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
		when(leaseRepository.save(any(Lease.class))).thenThrow(
				new RuntimeException("The end date of the lease has to be after the start date of the lease."));

		LeaseDTO leaseDTO = convertToDTO(lease1);
		GregorianCalendar endDate = lease1.getStartDate();
		endDate.add(Calendar.DAY_OF_MONTH, -1);
		leaseDTO.setEndDate(endDate);

		ResponseEntity<?> response = leaseService.update(1, convertToDTO(lease1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("The end date of the lease has to be after the start date of the lease.", response.getBody());
	}

	@Test
	void testUpdateDuplicate() {
		when(leaseRepository.findById(1)).thenReturn(Optional.of(lease1));
		when(residenceRepository.findById(1)).thenReturn(Optional.of(residence1));
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
		when(leaseRepository.findByProperty_PropertyIdAndCustomer_CustomerId(1, 1)).thenReturn(Optional.of(lease1));
		when(leaseRepository.save(any(Lease.class))).thenThrow(new RuntimeException("This lease already exists."));

		ResponseEntity<?> response = leaseService.update(1, convertToDTO(lease1));

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("This lease already exists.", response.getBody());
	}

	@Test
	void testUpdate() {
		when(leaseRepository.findById(1)).thenReturn(Optional.of(lease1));
		when(residenceRepository.findById(1)).thenReturn(Optional.of(residence1));
		when(eventSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(officeSpaceRepository.findById(1)).thenReturn(Optional.empty());
		when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
		when(leaseRepository.findByProperty_PropertyIdAndCustomer_CustomerId(1, 1)).thenReturn(Optional.empty());
		when(leaseRepository.save(any(Lease.class))).thenReturn(lease1);

		ResponseEntity<?> response = leaseService.update(1, convertToDTO(lease1));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(lease1), response.getBody());
	}

	@Test
	void testDeleteBadId() {
		when(leaseRepository.findById(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = leaseService.delete(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("There is no lease with the given id.", response.getBody());
	}

	@Test
	void testDeleteAssociatedRent() {
		when(leaseRepository.findById(1)).thenReturn(Optional.of(lease1));
		when(rentRepository.findByLeaseLeaseId(1)).thenReturn(Optional.of(rent));

		ResponseEntity<?> response = leaseService.delete(1);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("You cannot delete this lease since there are rents associated with it.", response.getBody());
	}

	@Test
	void testDelete() {
		when(leaseRepository.findById(1)).thenReturn(Optional.of(lease1));
		when(rentRepository.findByLeaseLeaseId(1)).thenReturn(Optional.empty());

		ResponseEntity<?> response = leaseService.delete(1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(convertToDTO(lease1), response.getBody());
	}

	private LeaseDTO convertToDTO(Lease lease) {
		LeaseDTO leaseDTO = new LeaseDTO();
		leaseDTO.setPropertyId(lease.getProperty().getPropertyId());
		leaseDTO.setCustomerId(lease.getCustomer().getCustomerId());
		leaseDTO.setRentalRate(lease.getRentalRate());
		leaseDTO.setStartDate(lease.getStartDate());
		leaseDTO.setEndDate(lease.getEndDate());

		return leaseDTO;
	}

}
