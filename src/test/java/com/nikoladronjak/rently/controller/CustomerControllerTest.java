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
import com.nikoladronjak.rently.domain.Customer;
import com.nikoladronjak.rently.dto.CustomerDTO;
import com.nikoladronjak.rently.service.CustomerService;

@SpringBootTest
class CustomerControllerTest {

	Customer customer1;

	Customer customer2;

	@Mock
	private CustomerService customerService;

	@InjectMocks
	private CustomerController customerController;

	@BeforeEach
	void setUp() throws Exception {
		customer1 = new Customer(1, "Pera", "Peric", "pera@gmail.com", "pera123", null);
		customer2 = new Customer(2, "Mika", "Mikic", "mika@gmail.com", "mika123", null);
	}

	@AfterEach
	void tearDown() throws Exception {
		customer1 = null;
		customer2 = null;
	}

	@Test
	void testGetAllCustomersError() throws Exception {
		try {
			lenient().when(customerService.getAll()).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
			mockMvc.perform(get("/api/customers").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isInternalServerError());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetAllCustomers() throws Exception {
		List<Customer> customers = new ArrayList<Customer>();
		customers.add(customer1);
		customers.add(customer2);
		when(customerService.getAll()).thenReturn((ResponseEntity) ResponseEntity.ok(customers));

		String customersJson = new ObjectMapper().writeValueAsString(customers);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
		mockMvc.perform(get("/api/customers").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(customersJson));
	}

	@Test
	void testGetCustomerByIdError() throws Exception {
		try {
			lenient().when(customerService.getById(1)).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
			mockMvc.perform(get("/api/customers/1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testGetCustomerById() throws Exception {
		when(customerService.getById(1)).thenReturn((ResponseEntity) ResponseEntity.ok(customer1));

		String customerJson = new ObjectMapper().writeValueAsString(customer1);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
		mockMvc.perform(get("/api/customers/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(customerJson));
	}

	@Test
	void testAddCustomerError() throws Exception {
		lenient().when(customerService.add(convertToDTO(customer1))).thenThrow(new RuntimeException());

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
		mockMvc.perform(post("/api/customers").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testAddCustomer() throws Exception {
		when(customerService.add(convertToDTO(customer1))).thenReturn((ResponseEntity) ResponseEntity.ok(customer1));

		String customerJson = new ObjectMapper().writeValueAsString(convertToDTO(customer1));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
		mockMvc.perform(post("/api/customers").contentType(MediaType.APPLICATION_JSON).content(customerJson))
				.andExpect(status().isOk()).andExpect(content().json(customerJson));
	}

	@Test
	void testUpdateCustomerError() throws Exception {
		lenient().when(customerService.update(1, convertToDTO(customer1))).thenThrow(new RuntimeException());

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
		mockMvc.perform(put("/api/customers/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testUpdateCustomer() throws Exception {
		when(customerService.update(1, convertToDTO(customer1)))
				.thenReturn((ResponseEntity) ResponseEntity.ok(customer1));

		String customerJson = new ObjectMapper().writeValueAsString(convertToDTO(customer1));
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
		mockMvc.perform(put("/api/customers/1").contentType(MediaType.APPLICATION_JSON).content(customerJson))
				.andExpect(status().isOk()).andExpect(content().json(customerJson));
	}

	@Test
	void testDeleteCustomerError() throws Exception {
		try {
			lenient().when(customerService.delete(1)).thenThrow(new RuntimeException());
		} catch (Exception e) {
			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
			mockMvc.perform(delete("/api/customers/1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	void testDeleteCustomer() throws Exception {
		when(customerService.delete(1)).thenReturn((ResponseEntity) ResponseEntity.ok(customer1));

		String customerJson = new ObjectMapper().writeValueAsString(customer1);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
		mockMvc.perform(delete("/api/customers/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(customerJson));
	}

	private CustomerDTO convertToDTO(Customer customer) {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setFirstName(customer.getFirstName());
		customerDTO.setLastName(customer.getLastName());
		customerDTO.setEmail(customer.getEmail());
		customerDTO.setPassword(customer.getPassword());

		return customerDTO;
	}

}
