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
import com.nikoladronjak.rently.domain.OfficeSpace;
import com.nikoladronjak.rently.domain.Owner;
import com.nikoladronjak.rently.domain.Utility;
import com.nikoladronjak.rently.domain.UtilityLease;
import com.nikoladronjak.rently.dto.UtilityLeaseDTO;
import com.nikoladronjak.rently.service.UtilityLeaseService;

@SpringBootTest
class UtilityLeaseControllerTest {

	Utility utility1;

	Utility utility2;

	Owner owner;

	OfficeSpace officeSpace1;

	OfficeSpace officeSpace2;

	UtilityLease utilityLease1;

	UtilityLease utilityLease2;

	UtilityLease utilityLease3;

	UtilityLease utilityLease4;

	@Mock
	private UtilityLeaseService utilityLeaseService;

	@InjectMocks
	private UtilityLeaseController utilityLeaseController;

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

		utilityLease1 = new UtilityLease(1, 50, utility1, officeSpace1, null);
		utilityLease2 = new UtilityLease(2, 60, utility2, officeSpace1, null);
		utilityLease3 = new UtilityLease(3, 50, utility1, officeSpace2, null);
		utilityLease4 = new UtilityLease(4, 60, utility2, officeSpace2, null);
	}

	@AfterEach
	void tearDown() throws Exception {
		utility1 = null;
		utility2 = null;

		officeSpace1 = null;
		officeSpace2 = null;

		owner = null;

		utilityLease1 = null;
		utilityLease2 = null;
		utilityLease3 = null;
		utilityLease4 = null;
	}

	@Test
	void testGetAllUtilityLeasesError() throws Exception {
		try {
			lenient().when(utilityLeaseService.getAll()).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(utilityLeaseController).build();
			mockMvc.perform(get("/api/utilityleases").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isInternalServerError());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetAllUtilityLeases() throws Exception {
		List<UtilityLease> utilityLeases = new ArrayList<UtilityLease>();
		utilityLeases.add(utilityLease1);
		utilityLeases.add(utilityLease2);
		utilityLeases.add(utilityLease3);
		utilityLeases.add(utilityLease4);
		when(utilityLeaseService.getAll()).thenReturn((ResponseEntity) ResponseEntity.ok(utilityLeases));

		String utilityLeasesJson = new ObjectMapper().writeValueAsString(utilityLeases);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(utilityLeaseController).build();
		mockMvc.perform(get("/api/utilityleases").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(utilityLeasesJson));
	}

	@Test
	void testGetAllUtilityLeasesByUtilityIdError() throws Exception {
		try {
			lenient().when(utilityLeaseService.getAllByUtilityId(1)).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(utilityLeaseController).build();
			mockMvc.perform(get("/api/utilityleases/utility/1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isInternalServerError());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetAllUtilityLeasesByUtilityId() throws Exception {
		List<UtilityLease> utilityLeases = new ArrayList<UtilityLease>();
		utilityLeases.add(utilityLease1);
		utilityLeases.add(utilityLease3);
		when(utilityLeaseService.getAllByUtilityId(1)).thenReturn((ResponseEntity) ResponseEntity.ok(utilityLeases));

		String utilityLeasesJson = new ObjectMapper().writeValueAsString(utilityLeases);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(utilityLeaseController).build();
		mockMvc.perform(get("/api/utilityleases/utility/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(utilityLeasesJson));
	}

	@Test
	void testGetAllUtilityLeasesByPropertyIdError() throws Exception {
		try {
			lenient().when(utilityLeaseService.getAllByPropertyId(1)).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(utilityLeaseController).build();
			mockMvc.perform(get("/api/utilityleases/property/1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isInternalServerError());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetAllUtilityLeasesByPropertyId() throws Exception {
		List<UtilityLease> utilityLeases = new ArrayList<UtilityLease>();
		utilityLeases.add(utilityLease1);
		utilityLeases.add(utilityLease2);
		when(utilityLeaseService.getAllByPropertyId(1)).thenReturn((ResponseEntity) ResponseEntity.ok(utilityLeases));

		String utilityLeasesJson = new ObjectMapper().writeValueAsString(utilityLeases);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(utilityLeaseController).build();
		mockMvc.perform(get("/api/utilityleases/property/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(utilityLeasesJson));
	}

	@Test
	void testGetUtilityLeaseByIdError() throws Exception {
		try {
			lenient().when(utilityLeaseService.getById(1)).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(utilityLeaseController).build();
			mockMvc.perform(get("/api/utilityleases/1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isInternalServerError());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetUtilityLeaseById() throws Exception {
		when(utilityLeaseService.getById(1))
				.thenReturn((ResponseEntity) ResponseEntity.ok(convertToDTO(utilityLease1)));

		String utilityLeaseJson = new ObjectMapper().writeValueAsString(convertToDTO(utilityLease1));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(utilityLeaseController).build();
		mockMvc.perform(get("/api/utilityleases/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(utilityLeaseJson));
	}

	@Test
	void testAddUtilityLeaseError() throws Exception {
		lenient().when(utilityLeaseService.add(convertToDTO(utilityLease1))).thenThrow(new RuntimeException());

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(utilityLeaseController).build();
		mockMvc.perform(post("/api/utilityleases").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testAddUtilityLease() throws Exception {
		when(utilityLeaseService.add(convertToDTO(utilityLease1)))
				.thenReturn((ResponseEntity) ResponseEntity.ok(convertToDTO(utilityLease1)));

		String utilityLeaseJson = new ObjectMapper().writeValueAsString(convertToDTO(utilityLease1));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(utilityLeaseController).build();
		mockMvc.perform(post("/api/utilityleases").contentType(MediaType.APPLICATION_JSON).content(utilityLeaseJson))
				.andExpect(status().isOk()).andExpect(content().json(utilityLeaseJson));
	}

	@Test
	void testUpdateUtilityLeaseError() throws Exception {
		lenient().when(utilityLeaseService.update(1, convertToDTO(utilityLease1))).thenThrow(new RuntimeException());

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(utilityLeaseController).build();
		mockMvc.perform(put("/api/utilityleases/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testUpdateUtilityLease() throws Exception {
		when(utilityLeaseService.update(1, convertToDTO(utilityLease1)))
				.thenReturn((ResponseEntity) ResponseEntity.ok(convertToDTO(utilityLease1)));

		String utilityLeaseJson = new ObjectMapper().writeValueAsString(convertToDTO(utilityLease1));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(utilityLeaseController).build();
		mockMvc.perform(put("/api/utilityleases/1").contentType(MediaType.APPLICATION_JSON).content(utilityLeaseJson))
				.andExpect(status().isOk()).andExpect(content().json(utilityLeaseJson));
	}

	@Test
	void testDeleteUtilityError() throws Exception {
		try {
			lenient().when(utilityLeaseService.delete(1)).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(utilityLeaseController).build();
			mockMvc.perform(delete("/api/utilityleases/1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testDeleteUtility() throws Exception {
		when(utilityLeaseService.delete(1)).thenReturn((ResponseEntity) ResponseEntity.ok(utilityLease1));

		String utilityLeaseJson = new ObjectMapper().writeValueAsString(utilityLease1);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(utilityLeaseController).build();
		mockMvc.perform(delete("/api/utilityleases/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(utilityLeaseJson));
	}

	private UtilityLeaseDTO convertToDTO(UtilityLease utilityLease) {
		UtilityLeaseDTO utilityLeaseDTO = new UtilityLeaseDTO();
		utilityLeaseDTO.setUtilityId(utilityLease.getUtility().getUtilityId());
		utilityLeaseDTO.setPropertyId(utilityLease.getProperty().getPropertyId());
		utilityLeaseDTO.setRentalRate(utilityLease.getRentalRate());

		return utilityLeaseDTO;
	}

}
