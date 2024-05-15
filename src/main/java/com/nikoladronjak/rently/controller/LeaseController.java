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

import com.nikoladronjak.rently.dto.LeaseDTO;
import com.nikoladronjak.rently.service.LeaseService;

import jakarta.validation.Valid;

/**
 * Represents a controller class for handling HTTP requests related to Lease
 * entities. This class provides end-points for retrieving, adding, updating,
 * and deleting Lease entities. It also handles validation errors by returning
 * appropriate responses.
 * 
 * @author Nikola Dronjak
 */
@RestController
@RequestMapping("/api/leases")
public class LeaseController {

	/**
	 * Service for handling operations related to Lease entities.
	 */
	@Autowired
	private LeaseService leaseService;

	/**
	 * Retrieves all leases. Route: HTTP GET /api/leases
	 *
	 * @return ResponseEntity with HTTP status and response body containing a list
	 *         of leases or an error message.
	 */
	@GetMapping
	public ResponseEntity<?> getAllLeases() {
		return leaseService.getAll();
	}

	/**
	 * Retrieves all leases for a specified propertyId. Route: HTTP GET
	 * /api/leases/property/propertyId
	 *
	 * @param propertyId The id of the property for which the leases are being
	 *                   queried.
	 * @return ResponseEntity with HTTP status and response body containing a list
	 *         of leases or an error message.
	 */
	@GetMapping("/property/{propertyId}")
	public ResponseEntity<?> getAllLeasesByPropertyId(@PathVariable Integer propertyId) {
		return leaseService.getAllByPropertyId(propertyId);
	}

	/**
	 * Retrieves all leases for a specified customerId. Route: HTTP GET
	 * /api/leases/customer/customerId
	 *
	 * @param customerId The id of the customer for which the leases are being
	 *                   queried.
	 * @return ResponseEntity with HTTP status and response body containing a list
	 *         of leases or an error message.
	 */
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<?> getAllLeasesByCustomerId(@PathVariable Integer customerId) {
		return leaseService.getAllByCustomerId(customerId);
	}

	/**
	 * Retrieves a lease by its id. Route: HTTP GET /api/leases/id
	 *
	 * @param id The id of the lease thats is being queried.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         lease if found, or an error message.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getLeaseById(@PathVariable Integer id) {
		return leaseService.getById(id);
	}

	/**
	 * Adds a new lease. Route: HTTP POST /api/leases
	 *
	 * @param leaseDTO The LeaseDTO representing the lease that is being added.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         added lease if successful, or an error message.
	 * @throws MethodArgumentNotValidException if the leaseDTO is not valid.
	 */
	@PostMapping
	public ResponseEntity<?> addLease(@Valid @RequestBody LeaseDTO leaseDTO) {
		return leaseService.add(leaseDTO);
	}

	/**
	 * Updates an existing lease. Route: HTTP PUT /api/leases/id
	 *
	 * @param id       The id of the lease that is being updated.
	 * @param leaseDTO The LeaseDTO representing the updated lease information.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         updated lease if successful, or an error message.
	 * @throws MethodArgumentNotValidException if the leaseDTO is not valid.
	 */
	@PutMapping("/{id}")
	ResponseEntity<?> updateLease(@PathVariable Integer id, @Valid @RequestBody LeaseDTO leaseDTO) {
		return leaseService.update(id, leaseDTO);
	}

	/**
	 * Deletes a lease by its id. Route: HTTP DELETE /api/leases/id
	 *
	 * @param id The id of the lease that is being deleted.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         deleted lease if successful, or an error message.
	 */
	@DeleteMapping("/{id}")
	ResponseEntity<?> deleteLease(@PathVariable Integer id) {
		return leaseService.delete(id);
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
