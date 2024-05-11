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

import com.nikoladronjak.rently.dto.ResidenceDTO;
import com.nikoladronjak.rently.service.ResidenceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/residences")
public class ResidenceController {

	@Autowired
	private ResidenceService residenceService;

	@GetMapping
	public ResponseEntity<?> getAllResidences() {
		return residenceService.getAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getResidenceById(@PathVariable Integer id) {
		return residenceService.getById(id);
	}

	@PostMapping
	public ResponseEntity<?> addResidence(@Valid @RequestBody ResidenceDTO residenceDTO) {
		return residenceService.add(residenceDTO);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateResidence(@PathVariable Integer id, @Valid @RequestBody ResidenceDTO residenceDTO) {
		return residenceService.update(id, residenceDTO);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteResidence(@PathVariable Integer id) {
		return residenceService.delete(id);
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
