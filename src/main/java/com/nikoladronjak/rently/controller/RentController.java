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

import com.nikoladronjak.rently.dto.RentDTO;
import com.nikoladronjak.rently.service.RentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/rents")
public class RentController {

	@Autowired
	private RentService rentService;

	@GetMapping
	public ResponseEntity<?> getAllRents() {
		return rentService.getAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getRentById(@PathVariable Integer id) {
		return rentService.getById(id);
	}

	@PostMapping
	public ResponseEntity<?> addRent(@Valid @RequestBody RentDTO rentDTO) {
		return rentService.add(rentDTO);
	}

	@PutMapping("/{id}")
	ResponseEntity<?> updateRent(@PathVariable Integer id, @Valid @RequestBody RentDTO rentDTO) {
		return rentService.update(id, rentDTO);
	}

	@DeleteMapping("/{id}")
	ResponseEntity<?> deleteRent(@PathVariable Integer id) {
		return rentService.delete(id);
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
