package com.nikoladronjak.rently.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikoladronjak.rently.domain.Customer;
import com.nikoladronjak.rently.domain.HeatingType;
import com.nikoladronjak.rently.domain.Lease;
import com.nikoladronjak.rently.domain.Owner;
import com.nikoladronjak.rently.domain.Residence;
import com.nikoladronjak.rently.dto.LeaseDTO;
import com.nikoladronjak.rently.service.LeaseService;

@SpringBootTest
class LeaseControllerTest {

	Residence residence1;

	Residence residence2;

	Owner owner;

	Customer customer;

	Lease lease1;

	Lease lease2;

	@Mock
	private LeaseService leaseService;

	@InjectMocks
	private LeaseController leaseController;

	@BeforeEach
	void setUp() throws Exception {
		List<String> photos = new ArrayList<String>();
		photos.add("photo1");
		photos.add("photo2");

		residence1 = new Residence(1, "Lux Apartment", "Jove Ilica 154", "", 400, 70, true, 2, photos, owner, null, 2,
				2, HeatingType.Central, true, true);
		residence2 = new Residence(2, "Lux Apartment", "Studentski trg 1", "", 300, 60, true, 2, photos, owner, null, 2,
				1, HeatingType.Central, true, true);

		owner = new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");

		customer = new Customer(1, "Mika", "Mikic", "mika@gmail.com", "mika123", null);

		lease1 = new Lease(1, 400, new GregorianCalendar(2024, 11, 12), new GregorianCalendar(2025, 11, 31), residence1,
				customer, null);
		lease2 = new Lease(2, 400, new GregorianCalendar(2024, 11, 12), new GregorianCalendar(2025, 11, 31), residence2,
				customer, null);
	}

	@AfterEach
	void tearDown() throws Exception {
		residence1 = null;
		residence2 = null;

		owner = null;

		customer = null;

		lease1 = null;
		lease2 = null;
	}

	@Test
	void testGetAllLeasesError() throws Exception {
		try {
			lenient().when(leaseService.getAll()).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(leaseController).build();
			mockMvc.perform(get("/api/leases").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isInternalServerError());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetAllLeases() throws Exception {
		List<Lease> leases = new ArrayList<Lease>();
		leases.add(lease1);
		leases.add(lease2);
		when(leaseService.getAll()).thenReturn((ResponseEntity) ResponseEntity.ok(leases));

		String leasesJson = new ObjectMapper().writeValueAsString(leases);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(leaseController).build();
		mockMvc.perform(get("/api/leases").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(leasesJson));
	}

	@Test
	void testGetAllLeasesByPropertyIdError() throws Exception {
		try {
			lenient().when(leaseService.getAllByPropertyId(1)).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(leaseController).build();
			mockMvc.perform(get("/api/leases").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isInternalServerError());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetAllLeasesByPropertyId() throws Exception {
		List<Lease> leases = new ArrayList<Lease>();
		leases.add(lease1);
		when(leaseService.getAllByPropertyId(1)).thenReturn((ResponseEntity) ResponseEntity.ok(leases));

		String leasesJson = new ObjectMapper().writeValueAsString(leases);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(leaseController).build();
		mockMvc.perform(get("/api/leases/property/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(leasesJson));
	}

	@Test
	void testGetAllLeasesByCustomerIdError() throws Exception {
		try {
			lenient().when(leaseService.getAllByCustomerId(1)).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(leaseController).build();
			mockMvc.perform(get("/api/leases/customer/1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isInternalServerError());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetAllLeasesByCustomerId() throws Exception {
		List<Lease> leases = new ArrayList<Lease>();
		leases.add(lease1);
		when(leaseService.getAllByCustomerId(1)).thenReturn((ResponseEntity) ResponseEntity.ok(leases));

		String leasesJson = new ObjectMapper().writeValueAsString(leases);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(leaseController).build();
		mockMvc.perform(get("/api/leases/customer/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(leasesJson));
	}

	@Test
	void testGetLeaseByIdError() throws Exception {
		try {
			lenient().when(leaseService.getById(1)).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(leaseController).build();
			mockMvc.perform(get("/api/leases/1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isInternalServerError());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetLeaseById() throws Exception {
		when(leaseService.getById(1)).thenReturn((ResponseEntity) ResponseEntity.ok(convertToDTO(lease1)));

		String leaseJson = new ObjectMapper().writeValueAsString(convertToDTO(lease1));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(leaseController).build();
		mockMvc.perform(get("/api/leases/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(leaseJson));
	}

	@Test
	void testAddLeaseError() throws Exception {
		lenient().when(leaseService.add(convertToDTO(lease1))).thenThrow(new RuntimeException());

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(leaseController).build();
		mockMvc.perform(post("/api/leases").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testAddLease() throws Exception {
		when(leaseService.add(any(LeaseDTO.class)))
				.thenReturn((ResponseEntity) ResponseEntity.ok(convertToDTO(lease1)));

		String leaseJson = new ObjectMapper().writeValueAsString(convertToDTO(lease1));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(leaseController).build();
		mockMvc.perform(post("/api/leases").contentType(MediaType.APPLICATION_JSON).content(leaseJson))
				.andExpect(status().isOk()).andExpect(content().json(leaseJson));
	}

	@Test
	void testUpdateLeaseError() throws Exception {
		lenient().when(leaseService.update(1, convertToDTO(lease1))).thenThrow(new RuntimeException());

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(leaseController).build();
		mockMvc.perform(put("/api/leases/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testUpdateLease() throws Exception {
		when(leaseService.update(eq(1), any(LeaseDTO.class)))
				.thenReturn((ResponseEntity) ResponseEntity.ok(convertToDTO(lease1)));

		String leaseJson = new ObjectMapper().writeValueAsString(convertToDTO(lease1));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(leaseController).build();
		mockMvc.perform(put("/api/leases/1").contentType(MediaType.APPLICATION_JSON).content(leaseJson))
				.andExpect(status().isOk()).andExpect(content().json(leaseJson));
	}

	@Test
	void testDeleteLeaseError() throws Exception {
		try {
			lenient().when(leaseService.delete(1)).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(leaseController).build();
			mockMvc.perform(delete("/api/leases/1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testDeleteLease() throws Exception {
		when(leaseService.delete(1)).thenReturn((ResponseEntity) ResponseEntity.ok(lease1));

		String leaseJson = new ObjectMapper().writeValueAsString(lease1);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(leaseController).build();
		mockMvc.perform(delete("/api/leases/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(leaseJson));
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
