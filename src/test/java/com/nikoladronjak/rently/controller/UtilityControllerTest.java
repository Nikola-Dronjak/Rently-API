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
import com.nikoladronjak.rently.domain.Utility;
import com.nikoladronjak.rently.dto.UtilityDTO;
import com.nikoladronjak.rently.service.UtilityService;

@SpringBootTest
class UtilityControllerTest {

	Utility utility1;

	Utility utility2;

	@Mock
	private UtilityService utilityService;

	@InjectMocks
	private UtilityController utilityController;

	@BeforeEach
	void setUp() throws Exception {
		utility1 = new Utility(1, "Microphone", "", null);
		utility2 = new Utility(2, "Projector", "", null);
	}

	@AfterEach
	void tearDown() throws Exception {
		utility1 = null;
		utility2 = null;
	}

	@Test
	void testGetAllUtilitiesError() throws Exception {
		try {
			lenient().when(utilityService.getAll()).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(utilityController).build();
			mockMvc.perform(get("/api/utilities").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isInternalServerError());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetAllUtilities() throws Exception {
		List<Utility> utilities = new ArrayList<Utility>();
		utilities.add(utility1);
		utilities.add(utility2);
		when(utilityService.getAll()).thenReturn((ResponseEntity) ResponseEntity.ok(utilities));

		String utilitiesJson = new ObjectMapper().writeValueAsString(utilities);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(utilityController).build();
		mockMvc.perform(get("/api/utilities").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(utilitiesJson));
	}

	@Test
	void testGetUtilityByIdError() throws Exception {
		try {
			lenient().when(utilityService.getById(1)).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(utilityController).build();
			mockMvc.perform(get("/api/utilities/1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetUtilityById() throws Exception {
		when(utilityService.getById(1)).thenReturn((ResponseEntity) ResponseEntity.ok(utility1));

		String utilityJson = new ObjectMapper().writeValueAsString(utility1);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(utilityController).build();
		mockMvc.perform(get("/api/utilities/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(utilityJson));
	}

	@Test
	void testAddUtilityError() throws Exception {
		lenient().when(utilityService.add(convertToDTO(utility1))).thenThrow(new RuntimeException());

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(utilityController).build();
		mockMvc.perform(post("/api/utilities").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testAddUtility() throws Exception {
		when(utilityService.add(convertToDTO(utility1))).thenReturn((ResponseEntity) ResponseEntity.ok(utility1));

		String utilityJson = new ObjectMapper().writeValueAsString(convertToDTO(utility1));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(utilityController).build();
		mockMvc.perform(post("/api/utilities").contentType(MediaType.APPLICATION_JSON).content(utilityJson))
				.andExpect(status().isOk()).andExpect(content().json(utilityJson));
	}

	@Test
	void testUpdateUtilityError() throws Exception {
		lenient().when(utilityService.update(1, convertToDTO(utility1))).thenThrow(new RuntimeException());

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(utilityController).build();
		mockMvc.perform(put("/api/utilities/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testUpdateUtility() throws Exception {
		when(utilityService.update(1, convertToDTO(utility1))).thenReturn((ResponseEntity) ResponseEntity.ok(utility1));

		String utilityJson = new ObjectMapper().writeValueAsString(convertToDTO(utility1));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(utilityController).build();
		mockMvc.perform(put("/api/utilities/1").contentType(MediaType.APPLICATION_JSON).content(utilityJson))
				.andExpect(status().isOk()).andExpect(content().json(utilityJson));
	}

	@Test
	void testDeleteUtilityError() throws Exception {
		try {
			lenient().when(utilityService.delete(1)).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(utilityController).build();
			mockMvc.perform(delete("/api/utilities/1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testDeleteUtility() throws Exception {
		when(utilityService.delete(1)).thenReturn((ResponseEntity) ResponseEntity.ok(utility1));

		String utilityJson = new ObjectMapper().writeValueAsString(utility1);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(utilityController).build();
		mockMvc.perform(delete("/api/utilities/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(utilityJson));
	}

	private UtilityDTO convertToDTO(Utility utility) {
		UtilityDTO utilityDTO = new UtilityDTO();
		utilityDTO.setName(utility.getName());
		utilityDTO.setDescription(utility.getDescription());

		return utilityDTO;
	}

}
