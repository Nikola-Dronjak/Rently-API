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
import com.nikoladronjak.rently.domain.Residence;
import com.nikoladronjak.rently.domain.HeatingType;
import com.nikoladronjak.rently.domain.Owner;
import com.nikoladronjak.rently.domain.Lease;
import com.nikoladronjak.rently.dto.ResidenceDTO;
import com.nikoladronjak.rently.service.ResidenceService;

@SpringBootTest
class ResidenceControllerTest {

	Residence residence1;

	Residence residence2;

	Owner owner;

	@Mock
	private ResidenceService residenceService;

	@InjectMocks
	private ResidenceController residenceController;

	@BeforeEach
	void setUp() throws Exception {
		List<String> photos = new ArrayList<String>();
		photos.add("photo1");
		photos.add("photo2");

		List<Lease> leases = new ArrayList<Lease>();
		leases.add(new Lease());

		owner = new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");

		residence1 = new Residence(1, "Apartement 1", "Jove Ilica 154", "", (double) 300, 30, true, 150, photos, owner,
				leases, 2, 2, HeatingType.Central, true, true);
		residence2 = new Residence(2, "Apartement 2", "Studentski trg 1", "", (double) 400, 40, true, 150, photos,
				owner, leases, 2, 2, HeatingType.Central, true, true);

	}

	@AfterEach
	void tearDown() throws Exception {
		owner = null;

		residence1 = null;
		residence2 = null;
	}

	@Test
	void testGetAllResidencesError() throws Exception {
		try {
			lenient().when(residenceService.getAll()).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(residenceController).build();
			mockMvc.perform(get("/api/residences").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isInternalServerError());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetAllResidences() throws Exception {
		List<Residence> residences = new ArrayList<Residence>();
		residences.add(residence1);
		residences.add(residence2);
		when(residenceService.getAll()).thenReturn((ResponseEntity) ResponseEntity
				.ok(residences.stream().map(this::convertToDTO).collect(Collectors.toList())));

		String residencesJson = new ObjectMapper()
				.writeValueAsString(residences.stream().map(this::convertToDTO).collect(Collectors.toList()));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(residenceController).build();
		mockMvc.perform(get("/api/residences").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(residencesJson));
	}

	@Test
	void testGetResidenceByIdError() throws Exception {
		try {
			lenient().when(residenceService.getById(1)).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(residenceController).build();
			mockMvc.perform(get("/api/residences/1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetResidenceById() throws Exception {
		when(residenceService.getById(1)).thenReturn((ResponseEntity) ResponseEntity.ok(convertToDTO(residence1)));

		String residenceJson = new ObjectMapper().writeValueAsString(convertToDTO(residence1));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(residenceController).build();
		mockMvc.perform(get("/api/residences/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(residenceJson));
	}

	@Test
	void testAddResidenceError() throws Exception {
		lenient().when(residenceService.add(convertToDTO(residence1))).thenThrow(new RuntimeException());

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(residenceController).build();
		mockMvc.perform(post("/api/residences").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testAddResidence() throws Exception {
		when(residenceService.add(convertToDTO(residence1)))
				.thenReturn((ResponseEntity) ResponseEntity.ok(convertToDTO(residence1)));

		String residenceJson = new ObjectMapper().writeValueAsString(convertToDTO(residence1));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(residenceController).build();
		mockMvc.perform(post("/api/residences").contentType(MediaType.APPLICATION_JSON).content(residenceJson))
				.andExpect(status().isOk()).andExpect(content().json(residenceJson));
	}

	@Test
	void testUpdateResidenceError() throws Exception {
		lenient().when(residenceService.update(1, convertToDTO(residence1))).thenThrow(new RuntimeException());

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(residenceController).build();
		mockMvc.perform(put("/api/residences/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testUpdateResidence() throws Exception {
		when(residenceService.update(1, convertToDTO(residence1)))
				.thenReturn((ResponseEntity) ResponseEntity.ok(convertToDTO(residence1)));

		String residenceJson = new ObjectMapper().writeValueAsString(convertToDTO(residence1));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(residenceController).build();
		mockMvc.perform(put("/api/residences/1").contentType(MediaType.APPLICATION_JSON).content(residenceJson))
				.andExpect(status().isOk()).andExpect(content().json(residenceJson));
	}

	@Test
	void testDeleteResidenceError() throws Exception {
		try {
			lenient().when(residenceService.delete(1)).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(residenceController).build();
			mockMvc.perform(delete("/api/residences/1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testDeleteResidence() throws Exception {
		when(residenceService.delete(1)).thenReturn((ResponseEntity) ResponseEntity.ok(residence1));

		String residenceJson = new ObjectMapper().writeValueAsString(residence1);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(residenceController).build();
		mockMvc.perform(delete("/api/residences/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(residenceJson));
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
