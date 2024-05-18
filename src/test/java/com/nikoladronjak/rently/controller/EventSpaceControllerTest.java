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
import com.nikoladronjak.rently.domain.EventSpace;
import com.nikoladronjak.rently.domain.Lease;
import com.nikoladronjak.rently.domain.Owner;
import com.nikoladronjak.rently.domain.UtilityLease;
import com.nikoladronjak.rently.dto.EventSpaceDTO;
import com.nikoladronjak.rently.service.EventSpaceService;

@SpringBootTest
class EventSpaceControllerTest {

	EventSpace eventSpace1;

	EventSpace eventSpace2;

	Owner owner;

	@Mock
	private EventSpaceService eventSpaceService;

	@InjectMocks
	private EventSpaceController eventSpaceController;

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
		eventSpace1 = new EventSpace(1, "Event Space 1", "Jove Ilica 154", "", (double) 300, 200, true, 20, photos,
				owner, leases, 50, true, true, utilityLeases);
		eventSpace2 = new EventSpace(2, "Event Space 2", "Studentski trg 1", "", (double) 300, 200, true, 20, null,
				owner, null, 50, true, true, null);

	}

	@AfterEach
	void tearDown() throws Exception {
		owner = null;

		eventSpace1 = null;
		eventSpace2 = null;
	}

	@Test
	void testGetAllEventSpacesError() throws Exception {
		try {
			lenient().when(eventSpaceService.getAll()).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(eventSpaceController).build();
			mockMvc.perform(get("/api/eventspaces").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isInternalServerError());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetAllEventSpaces() throws Exception {
		List<EventSpace> eventSpaces = new ArrayList<EventSpace>();
		eventSpaces.add(eventSpace1);
		eventSpaces.add(eventSpace2);
		when(eventSpaceService.getAll()).thenReturn((ResponseEntity) ResponseEntity
				.ok(eventSpaces.stream().map(this::convertToDTO).collect(Collectors.toList())));

		String eventSpacesJson = new ObjectMapper()
				.writeValueAsString(eventSpaces.stream().map(this::convertToDTO).collect(Collectors.toList()));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(eventSpaceController).build();
		mockMvc.perform(get("/api/eventspaces").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(eventSpacesJson));
	}

	@Test
	void testGetEventSpaceByIdError() throws Exception {
		try {
			lenient().when(eventSpaceService.getById(1)).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(eventSpaceController).build();
			mockMvc.perform(get("/api/eventspaces/1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetEventSpaceById() throws Exception {
		when(eventSpaceService.getById(1)).thenReturn((ResponseEntity) ResponseEntity.ok(convertToDTO(eventSpace1)));

		String eventSpaceJson = new ObjectMapper().writeValueAsString(convertToDTO(eventSpace1));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(eventSpaceController).build();
		mockMvc.perform(get("/api/eventspaces/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(eventSpaceJson));
	}

	@Test
	void testAddEventSpaceError() throws Exception {
		lenient().when(eventSpaceService.add(convertToDTO(eventSpace1))).thenThrow(new RuntimeException());

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(eventSpaceController).build();
		mockMvc.perform(post("/api/eventspaces").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testAddEventSpace() throws Exception {
		when(eventSpaceService.add(convertToDTO(eventSpace1)))
				.thenReturn((ResponseEntity) ResponseEntity.ok(convertToDTO(eventSpace1)));

		String eventSpaceJson = new ObjectMapper().writeValueAsString(convertToDTO(eventSpace1));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(eventSpaceController).build();
		mockMvc.perform(post("/api/eventspaces").contentType(MediaType.APPLICATION_JSON).content(eventSpaceJson))
				.andExpect(status().isOk()).andExpect(content().json(eventSpaceJson));
	}

	@Test
	void testUpdateEventSpaceError() throws Exception {
		lenient().when(eventSpaceService.update(1, convertToDTO(eventSpace1))).thenThrow(new RuntimeException());

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(eventSpaceController).build();
		mockMvc.perform(put("/api/eventspaces/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testUpdateEventSpace() throws Exception {
		when(eventSpaceService.update(1, convertToDTO(eventSpace1)))
				.thenReturn((ResponseEntity) ResponseEntity.ok(convertToDTO(eventSpace1)));

		String eventSpaceJson = new ObjectMapper().writeValueAsString(convertToDTO(eventSpace1));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(eventSpaceController).build();
		mockMvc.perform(put("/api/eventspaces/1").contentType(MediaType.APPLICATION_JSON).content(eventSpaceJson))
				.andExpect(status().isOk()).andExpect(content().json(eventSpaceJson));
	}

	@Test
	void testDeleteEventSpaceError() throws Exception {
		try {
			lenient().when(eventSpaceService.delete(1)).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(eventSpaceController).build();
			mockMvc.perform(delete("/api/eventspaces/1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testDeleteEventSpace() throws Exception {
		when(eventSpaceService.delete(1)).thenReturn((ResponseEntity) ResponseEntity.ok(eventSpace1));

		String eventSpaceJson = new ObjectMapper().writeValueAsString(eventSpace1);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(eventSpaceController).build();
		mockMvc.perform(delete("/api/eventspaces/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(eventSpaceJson));
	}

	private EventSpaceDTO convertToDTO(EventSpace eventSpace) {
		EventSpaceDTO eventSpaceDTO = new EventSpaceDTO();
		eventSpaceDTO.setPropertyId(eventSpace.getPropertyId());
		eventSpaceDTO.setName(eventSpace.getName());
		eventSpaceDTO.setAddress(eventSpace.getAddress());
		eventSpaceDTO.setDescription(eventSpace.getDescription());
		eventSpaceDTO.setRentalRate(eventSpace.getRentalRate());
		eventSpaceDTO.setSize(eventSpace.getSize());
		eventSpaceDTO.setIsAvailable(eventSpace.isAvailable());
		eventSpaceDTO.setNumberOfParkingSpots(eventSpace.getNumberOfParkingSpots());
		eventSpaceDTO.setPhotos(eventSpace.getPhotos());
		eventSpaceDTO.setCapacity(eventSpace.getCapacity());
		eventSpaceDTO.setHasKitchen(eventSpace.isHasKitchen());
		eventSpaceDTO.setHasBar(eventSpace.isHasBar());
		eventSpaceDTO.setOwnerId(eventSpace.getOwner().getOwnerId());

		return eventSpaceDTO;
	}

}
