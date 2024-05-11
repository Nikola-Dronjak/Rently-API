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

import com.nikoladronjak.rently.dto.UtilityDTO;
import com.nikoladronjak.rently.service.UtilityService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/utilities")
public class UtilityController {

	@Autowired
	private UtilityService utilityService;

	@GetMapping
	public ResponseEntity<?> getAllUtilities() {
		return utilityService.getAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getUtilityById(@PathVariable Integer id) {
		return utilityService.getById(id);
	}

	@PostMapping
	public ResponseEntity<?> addUtility(@Valid @RequestBody UtilityDTO utilityDTO) {
		return utilityService.add(utilityDTO);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateUtility(@PathVariable Integer id, @Valid @RequestBody UtilityDTO utilityDTO) {
		return utilityService.update(id, utilityDTO);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUtility(@PathVariable Integer id) {
		return utilityService.delete(id);
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
