package com.nikoladronjak.rently.controller;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikoladronjak.rently.domain.Owner;
import com.nikoladronjak.rently.dto.OwnerDTO;
import com.nikoladronjak.rently.service.OwnerService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

	Owner owner1;

	Owner owner2;

	@Mock
	private OwnerService ownerService;

	@InjectMocks
	private OwnerController ownerController;

	@BeforeEach
	void setUp() throws Exception {
		owner1 = new Owner(1, "Pera", "Peric", "pera@gmail.com", "pera123", "1234567890");
		owner2 = new Owner(2, "Mika", "Mikic", "mika@gmail.com", "mika123", "0987654321");
	}

	@AfterEach
	void tearDown() throws Exception {
		owner1 = null;
		owner2 = null;
	}

	@Test
	void testGetAllOwnersError() throws Exception {
		try {
			lenient().when(ownerService.getAll()).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
			mockMvc.perform(get("/api/owners").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isInternalServerError());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetAllOwners() throws Exception {
		List<Owner> owners = new ArrayList<>();
		owners.add(owner1);
		owners.add(owner2);
		when(ownerService.getAll()).thenReturn((ResponseEntity) ResponseEntity.ok(owners));

		String ownersJson = new ObjectMapper().writeValueAsString(owners);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
		mockMvc.perform(get("/api/owners").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(ownersJson));
	}

	@Test
	void testGetOwnerByIdError() throws Exception {
		try {
			lenient().when(ownerService.getById(1)).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
			mockMvc.perform(get("/api/owners/1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetOwnerById() throws Exception {
		when(ownerService.getById(1)).thenReturn((ResponseEntity) ResponseEntity.ok(owner1));

		String ownerJson = new ObjectMapper().writeValueAsString(owner1);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
		mockMvc.perform(get("/api/owners/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(ownerJson));
	}

	@Test
	void testAddOwnerError() throws Exception {
		lenient().when(ownerService.add(convertToDTO(owner1))).thenThrow(new RuntimeException());

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
		mockMvc.perform(post("/api/owners").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testAddOwner() throws Exception {
		when(ownerService.add(convertToDTO(owner1))).thenReturn((ResponseEntity) ResponseEntity.ok(owner1));

		String ownerJson = new ObjectMapper().writeValueAsString(convertToDTO(owner1));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
		mockMvc.perform(post("/api/owners").contentType(MediaType.APPLICATION_JSON).content(ownerJson))
				.andExpect(status().isOk()).andExpect(content().json(ownerJson));
	}

	@Test
	void testUpdateOwnerError() throws Exception {
		lenient().when(ownerService.update(1, convertToDTO(owner1))).thenThrow(new RuntimeException());

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
		mockMvc.perform(put("/api/owners/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testUpdateOwner() throws Exception {
		when(ownerService.update(1, convertToDTO(owner1))).thenReturn((ResponseEntity) ResponseEntity.ok(owner1));

		String ownerJson = new ObjectMapper().writeValueAsString(convertToDTO(owner1));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
		mockMvc.perform(put("/api/owners/1").contentType(MediaType.APPLICATION_JSON).content(ownerJson))
				.andExpect(status().isOk()).andExpect(content().json(ownerJson));
	}

	@Test
	void testDeleteOwnerError() throws Exception {
		try {
			lenient().when(ownerService.delete(1)).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
			mockMvc.perform(delete("/api/owners/1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testDeleteOwner() throws Exception {
		when(ownerService.delete(1)).thenReturn((ResponseEntity) ResponseEntity.ok(owner1));

		String ownerJson = new ObjectMapper().writeValueAsString(owner1);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
		mockMvc.perform(delete("/api/owners/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(ownerJson));
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
