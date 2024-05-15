package com.nikoladronjak.rently.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nikoladronjak.rently.dto.CustomerDTO;
import com.nikoladronjak.rently.service.CustomerService;

import jakarta.validation.Valid;

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
	 * @throws MethodArgumentNotValidException if the customerDTO is not valid.
	 */
	@PostMapping
	public ResponseEntity<?> addCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
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
	 * @throws MethodArgumentNotValidException if the customerDTO is not valid.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> updateCustomer(@PathVariable Integer id, @Valid @RequestBody CustomerDTO customerDTO) {
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

	/**
	 * Creates custom error messages for validation exceptions.
	 *
	 * @param e MethodArgumentNotValidException thrown during validation.
	 * @return ResponseEntity with HTTP status and response body containing
	 *         validation error details.
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	private ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
		Map<String, String> errors = new HashMap<String, String>();
		e.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return ResponseEntity.badRequest().body(errors);
	}
}
