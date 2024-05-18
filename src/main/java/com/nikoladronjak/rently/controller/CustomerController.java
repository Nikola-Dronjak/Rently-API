package com.nikoladronjak.rently.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nikoladronjak.rently.dto.CustomerDTO;
import com.nikoladronjak.rently.service.CustomerService;

/**
 * Represents a controller class for handling HTTP requests related to Customer
 * entities. This class provides end-points for retrieving, adding, updating,
 * and deleting Customer entities. It also handles validation errors by
 * returning appropriate responses.
 * 
 * @author Nikola Dronjak
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	/**
	 * Service for handling operations related to Customer entities.
	 */
	@Autowired
	private CustomerService customerService;

	/**
	 * Retrieves all customers. Route: HTTP GET /api/customers
	 *
	 * @return ResponseEntity with HTTP status and response body containing a list
	 *         of customers or an error message.
	 */
	@GetMapping
	public ResponseEntity<?> getAllCustomers() {
		return customerService.getAll();
	}

	/**
	 * Retrieves a customer by their id. Route: HTTP GET /api/customers/id
	 *
	 * @param id The id of the customer thats is being queried.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         customer if found, or an error message.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getCustomerById(@PathVariable Integer id) {
		return customerService.getById(id);
	}

	/**
	 * Adds a new customer. Route: HTTP POST /api/customers
	 *
	 * @param customerDTO The CustomerDTO representing the customer that is being
	 *                    added.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         added customer if successful, or an error message.
	 */
	@PostMapping
	public ResponseEntity<?> addCustomer(@RequestBody CustomerDTO customerDTO) {
		return customerService.add(customerDTO);
	}

	/**
	 * Updates an existing customer. Route: HTTP PUT /api/customers/id
	 *
	 * @param id          The id of the customer that is being updated.
	 * @param customerDTO The CustomerDTO representing the updated customer
	 *                    information.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         updated customer if successful, or an error message.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> updateCustomer(@PathVariable Integer id, @RequestBody CustomerDTO customerDTO) {
		return customerService.update(id, customerDTO);
	}

	/**
	 * Deletes a customer by their id. Route: HTTP DELETE /api/customers/id
	 *
	 * @param id The id of the customer that is being deleted.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         deleted customer if successful, or an error message.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCustomer(@PathVariable Integer id) {
		return customerService.delete(id);
	}
}
