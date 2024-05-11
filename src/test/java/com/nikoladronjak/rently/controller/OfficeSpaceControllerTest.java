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
import com.nikoladronjak.rently.domain.OfficeSpace;
import com.nikoladronjak.rently.domain.Lease;
import com.nikoladronjak.rently.domain.Owner;
import com.nikoladronjak.rently.domain.UtilityLease;
import com.nikoladronjak.rently.dto.OfficeSpaceDTO;
import com.nikoladronjak.rently.service.OfficeSpaceService;

@SpringBootTest
class OfficeSpaceControllerTest {

	OfficeSpace officeSpace1;

	OfficeSpace officeSpace2;

	Owner owner;

	@Mock
	private OfficeSpaceService officeSpaceService;

	@InjectMocks
	private OfficeSpaceController officeSpaceController;

	@BeforeEach
	void setUp() throws Exception {
		List<String> photos = new ArrayList<String>();
		photos.add("photo1");
		photos.add("photo2");

		List<Lease> leases = new ArrayList<Lease>();
		leases.add(new Lease());

		List<UtilityLease> utilityLeases = new ArrayList<UtilityLease>();
		utilityLeases.add(new UtilityLease());

		owner = new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");
		officeSpace1 = new OfficeSpace(1, "Office Space 1", "Jove Ilica 154", "", 400, 100, true, 20, photos, owner,
				leases, 20, utilityLeases);
		officeSpace2 = new OfficeSpace(2, "Office Space 2", "Studentski trg 1", "", 300, 200, true, 20, null, owner,
				null, 20, null);
	}

	@AfterEach
	void tearDown() throws Exception {
		owner = null;

		officeSpace1 = null;
		officeSpace2 = null;
	}

	@Test
	void testGetAllOfficeSpacesError() throws Exception {
		try {
			lenient().when(officeSpaceService.getAll()).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(officeSpaceController).build();
			mockMvc.perform(get("/api/officespaces").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isInternalServerError());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetAllOfficeSpaces() throws Exception {
		List<OfficeSpace> officeSpaces = new ArrayList<OfficeSpace>();
		officeSpaces.add(officeSpace1);
		officeSpaces.add(officeSpace2);
		when(officeSpaceService.getAll()).thenReturn((ResponseEntity) ResponseEntity
				.ok(officeSpaces.stream().map(this::convertToDTO).collect(Collectors.toList())));

		String officeSpacesJson = new ObjectMapper()
				.writeValueAsString(officeSpaces.stream().map(this::convertToDTO).collect(Collectors.toList()));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(officeSpaceController).build();
		mockMvc.perform(get("/api/officespaces").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(officeSpacesJson));
	}

	@Test
	void testGetOfficeSpaceByIdError() throws Exception {
		try {
			lenient().when(officeSpaceService.getById(1)).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(officeSpaceController).build();
			mockMvc.perform(get("/api/officespaces/1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetOfficeSpaceById() throws Exception {
		when(officeSpaceService.getById(1)).thenReturn((ResponseEntity) ResponseEntity.ok(convertToDTO(officeSpace1)));

		String officeSpaceJson = new ObjectMapper().writeValueAsString(convertToDTO(officeSpace1));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(officeSpaceController).build();
		mockMvc.perform(get("/api/officespaces/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(officeSpaceJson));
	}

	@Test
	void testAddOfficeSpaceError() throws Exception {
		lenient().when(officeSpaceService.add(convertToDTO(officeSpace1))).thenThrow(new RuntimeException());

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(officeSpaceController).build();
		mockMvc.perform(post("/api/officespaces").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testAddOfficeSpace() throws Exception {
		when(officeSpaceService.add(convertToDTO(officeSpace1)))
				.thenReturn((ResponseEntity) ResponseEntity.ok(convertToDTO(officeSpace1)));

		String officeSpaceJson = new ObjectMapper().writeValueAsString(convertToDTO(officeSpace1));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(officeSpaceController).build();
		mockMvc.perform(post("/api/officespaces").contentType(MediaType.APPLICATION_JSON).content(officeSpaceJson))
				.andExpect(status().isOk()).andExpect(content().json(officeSpaceJson));
	}

	@Test
	void testUpdateOfficeSpaceError() throws Exception {
		lenient().when(officeSpaceService.update(1, convertToDTO(officeSpace1))).thenThrow(new RuntimeException());

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(officeSpaceController).build();
		mockMvc.perform(put("/api/officespaces/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testUpdateOfficeSpace() throws Exception {
		when(officeSpaceService.update(1, convertToDTO(officeSpace1)))
				.thenReturn((ResponseEntity) ResponseEntity.ok(convertToDTO(officeSpace1)));

		String officeSpaceJson = new ObjectMapper().writeValueAsString(convertToDTO(officeSpace1));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(officeSpaceController).build();
		mockMvc.perform(put("/api/officespaces/1").contentType(MediaType.APPLICATION_JSON).content(officeSpaceJson))
				.andExpect(status().isOk()).andExpect(content().json(officeSpaceJson));
	}

	@Test
	void testDeleteOfficeSpaceError() throws Exception {
		try {
			lenient().when(officeSpaceService.delete(1)).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(officeSpaceController).build();
			mockMvc.perform(delete("/api/officespaces/1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testDeleteOfficeSpace() throws Exception {
		when(officeSpaceService.delete(1)).thenReturn((ResponseEntity) ResponseEntity.ok(officeSpace1));

		String officeSpaceJson = new ObjectMapper().writeValueAsString(officeSpace1);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(officeSpaceController).build();
		mockMvc.perform(delete("/api/officespaces/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(officeSpaceJson));
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
