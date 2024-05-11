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

@RestController
@RequestMapping("/api/leases")
public class LeaseController {

	@Autowired
	private LeaseService leaseService;

	@GetMapping
	public ResponseEntity<?> getAllLeases() {
		return leaseService.getAll();
	}

	@GetMapping("/property/{propertyId}")
	public ResponseEntity<?> getAllLeasesByPropertyId(@PathVariable Integer propertyId) {
		return leaseService.getAllByPropertyId(propertyId);
	}

	@GetMapping("/customer/{customerId}")
	public ResponseEntity<?> getAllLeasesByCustomerId(@PathVariable Integer customerId) {
		return leaseService.getAllByCustomerId(customerId);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getLeaseById(@PathVariable Integer id) {
		return leaseService.getById(id);
	}

	@PostMapping
	public ResponseEntity<?> addLease(@Valid @RequestBody LeaseDTO leaseDTO) {
		return leaseService.add(leaseDTO);
	}

	@PutMapping("/{id}")
	ResponseEntity<?> updateLease(@PathVariable Integer id, @Valid @RequestBody LeaseDTO leaseDTO) {
		return leaseService.update(id, leaseDTO);
	}

	@DeleteMapping("/{id}")
	ResponseEntity<?> deleteLease(@PathVariable Integer id) {
		return leaseService.delete(id);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
		Map<String, String> errors = new HashMap<String, String>();
		e.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return ResponseEntity.badRequest().body(errors);
	}
}
