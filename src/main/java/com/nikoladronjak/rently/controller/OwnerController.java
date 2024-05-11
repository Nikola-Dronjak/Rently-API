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

@RestController
@RequestMapping("/api/owners")
public class OwnerController {

	@Autowired
	private OwnerService ownerService;

	@GetMapping
	public ResponseEntity<?> getAllOwners() {
		return ownerService.getAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOwnerById(@PathVariable Integer id) {
		return ownerService.getById(id);
	}

	@PostMapping
	public ResponseEntity<?> addOwner(@Valid @RequestBody OwnerDTO ownerDTO) {
		return ownerService.add(ownerDTO);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateOwner(@PathVariable Integer id, @Valid @RequestBody OwnerDTO ownerDTO) {
		return ownerService.update(id, ownerDTO);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOwner(@PathVariable Integer id) {
		return ownerService.delete(id);
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
