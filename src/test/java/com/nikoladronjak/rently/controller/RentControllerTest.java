package com.nikoladronjak.rently.controller;

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
import java.util.stream.Collectors;

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
import com.nikoladronjak.rently.domain.Lease;
import com.nikoladronjak.rently.domain.OfficeSpace;
import com.nikoladronjak.rently.domain.Owner;
import com.nikoladronjak.rently.domain.Rent;
import com.nikoladronjak.rently.domain.Utility;
import com.nikoladronjak.rently.domain.UtilityLease;
import com.nikoladronjak.rently.dto.RentDTO;
import com.nikoladronjak.rently.service.RentService;

@SpringBootTest
class RentControllerTest {

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
	private RentService rentService;

	@InjectMocks
	private RentController rentController;

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

		officeSpace1 = new OfficeSpace(1, "Office Space 1", "Jove Ilica 154", "", 300, 150, true, 30, photos, owner,
				null, 100, null);
		officeSpace2 = new OfficeSpace(2, "Office Space 2", "Studentski trg 1", "", 250, 120, true, 20, photos, owner,
				null, 90, null);

		lease1 = new Lease(1, 200, new GregorianCalendar(2024, 11, 12), new GregorianCalendar(2025, 11, 12),
				officeSpace1, customer, null);
		lease2 = new Lease(2, 250, new GregorianCalendar(2024, 11, 12), new GregorianCalendar(2025, 11, 12),
				officeSpace2, customer, null);

		utilityLease1 = new UtilityLease(1, 40, utility1, officeSpace1, rents);
		utilityLease2 = new UtilityLease(2, 60, utility2, officeSpace1, rents);

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
	void testGetAllRentsError() throws Exception {
		try {
			lenient().when(rentService.getAll()).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(rentController).build();
			mockMvc.perform(get("/api/rents").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isInternalServerError());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetAllRents() throws Exception {
		List<Rent> rents = new ArrayList<Rent>();
		rents.add(rent1);
		rents.add(rent2);
		when(rentService.getAll()).thenReturn((ResponseEntity) ResponseEntity.ok(rents));

		String rentsJson = new ObjectMapper().writeValueAsString(rents);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(rentController).build();
		mockMvc.perform(get("/api/rents").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(rentsJson));
	}

	@Test
	void testGetRentByIdError() throws Exception {
		try {
			lenient().when(rentService.getById(1)).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(rentController).build();
			mockMvc.perform(get("/api/rents/1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isInternalServerError());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetRentById() throws Exception {
		when(rentService.getById(1)).thenReturn((ResponseEntity) ResponseEntity.ok(convertToDTO(rent1)));

		String rentJson = new ObjectMapper().writeValueAsString(convertToDTO(rent1));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(rentController).build();
		mockMvc.perform(get("/api/rents/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(rentJson));
	}

	@Test
	void testAddRentError() throws Exception {
		lenient().when(rentService.add(convertToDTO(rent1))).thenThrow(new RuntimeException());

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(rentController).build();
		mockMvc.perform(post("/api/rents").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testAddRent() throws Exception {
		when(rentService.add(convertToDTO(rent1))).thenReturn((ResponseEntity) ResponseEntity.ok(convertToDTO(rent1)));

		String rentJson = new ObjectMapper().writeValueAsString(convertToDTO(rent1));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(rentController).build();
		mockMvc.perform(post("/api/rents").contentType(MediaType.APPLICATION_JSON).content(rentJson))
				.andExpect(status().isOk()).andExpect(content().json(rentJson));
	}

	@Test
	void testUpdateRentError() throws Exception {
		lenient().when(rentService.update(1, convertToDTO(rent1))).thenThrow(new RuntimeException());

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(rentController).build();
		mockMvc.perform(put("/api/rents/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testUpdateRent() throws Exception {
		when(rentService.update(1, convertToDTO(rent1)))
				.thenReturn((ResponseEntity) ResponseEntity.ok(convertToDTO(rent1)));

		String rentJson = new ObjectMapper().writeValueAsString(convertToDTO(rent1));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(rentController).build();
		mockMvc.perform(put("/api/rents/1").contentType(MediaType.APPLICATION_JSON).content(rentJson))
				.andExpect(status().isOk()).andExpect(content().json(rentJson));
	}

	@Test
	void testDeleteRentError() throws Exception {
		try {
			lenient().when(rentService.delete(1)).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(rentController).build();
			mockMvc.perform(delete("/api/rents/1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testDeleteRent() throws Exception {
		when(rentService.delete(1)).thenReturn((ResponseEntity) ResponseEntity.ok(rent1));

		String rentJson = new ObjectMapper().writeValueAsString(rent1);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(rentController).build();
		mockMvc.perform(delete("/api/rents/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(rentJson));
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
