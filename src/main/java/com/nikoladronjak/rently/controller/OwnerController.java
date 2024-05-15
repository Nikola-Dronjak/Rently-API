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

import com.nikoladronjak.rently.dto.OwnerDTO;
import com.nikoladronjak.rently.service.OwnerService;

import jakarta.validation.Valid;

/**
 * Represents a controller class for handling HTTP requests related to Owner
 * entities. This class provides end-points for retrieving, adding, updating,
 * and deleting Owner entities. It also handles validation errors by returning
 * appropriate responses.
 * 
 * @author Nikola Dronjak
 */
@RestController
@RequestMapping("/api/owners")
public class OwnerController {

	/**
	 * Service for handling operations related to Owner entities.
	 */
	@Autowired
	private OwnerService ownerService;

	/**
	 * Retrieves all owners. Route: HTTP GET /api/owners
	 *
	 * @return ResponseEntity with HTTP status and response body containing a list
	 *         of owners or an error message.
	 */
	@GetMapping
	public ResponseEntity<?> getAllOwners() {
		return ownerService.getAll();
	}

	/**
	 * Retrieves an owner by their id. Route: HTTP GET /api/owners/id
	 *
	 * @param id The id of the owner thats is being queried.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         owner if found, or an error message.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getOwnerById(@PathVariable Integer id) {
		return ownerService.getById(id);
	}

	/**
	 * Adds a new owner. Route: HTTP POST /api/owners
	 *
	 * @param ownerDTO The OwnerDTO representing the owner that is being added.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         added owner if successful, or an error message.
	 * @throws MethodArgumentNotValidException if the ownerDTO is not valid.
	 */
	@PostMapping
	public ResponseEntity<?> addOwner(@Valid @RequestBody OwnerDTO ownerDTO) {
		return ownerService.add(ownerDTO);
	}

	/**
	 * Updates an existing owner. Route: HTTP PUT /api/owners/id
	 *
	 * @param id       The id of the owner that is being updated.
	 * @param ownerDTO The OwnerDTO representing the updated owner information.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         updated owner if successful, or an error message.
	 * @throws MethodArgumentNotValidException if the ownerDTO is not valid.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> updateOwner(@PathVariable Integer id, @Valid @RequestBody OwnerDTO ownerDTO) {
		return ownerService.update(id, ownerDTO);
	}

	/**
	 * Deletes an owner by their id. Route: HTTP DELETE /api/owners/id
	 *
	 * @param id The id of the owner that is being deleted.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         deleted owner if successful, or an error message.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOwner(@PathVariable Integer id) {
		return ownerService.delete(id);
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
