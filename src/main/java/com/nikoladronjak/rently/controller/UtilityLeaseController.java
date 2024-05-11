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

import com.nikoladronjak.rently.dto.UtilityLeaseDTO;
import com.nikoladronjak.rently.service.UtilityLeaseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/utilityleases")
public class UtilityLeaseController {

	@Autowired
	private UtilityLeaseService utilityLeaseService;

	@GetMapping
	public ResponseEntity<?> getAllUtilityLeases() {
		return utilityLeaseService.getAll();
	}

	@GetMapping("/utility/{utilityId}")
	public ResponseEntity<?> getAllUtilityLeasesByUtilityId(@PathVariable Integer utilityId) {
		return utilityLeaseService.getAllByUtilityId(utilityId);
	}

	@GetMapping("/property/{propertyId}")
	public ResponseEntity<?> getAllUtilityLeasesByPropertyId(@PathVariable Integer propertyId) {
		return utilityLeaseService.getAllByPropertyId(propertyId);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getUtilityLeaseById(@PathVariable Integer id) {
		return utilityLeaseService.getById(id);
	}

	@PostMapping
	public ResponseEntity<?> addUtilityLease(@Valid @RequestBody UtilityLeaseDTO utilityLeaseDTO) {
		return utilityLeaseService.add(utilityLeaseDTO);
	}

	@PutMapping("/{id}")
	ResponseEntity<?> updateUtilityLease(@PathVariable Integer id,
			@Valid @RequestBody UtilityLeaseDTO utilityLeaseDTO) {
		return utilityLeaseService.update(id, utilityLeaseDTO);
	}

	@DeleteMapping("/{id}")
	ResponseEntity<?> deleteUtilityLease(@PathVariable Integer id) {
		return utilityLeaseService.delete(id);
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
