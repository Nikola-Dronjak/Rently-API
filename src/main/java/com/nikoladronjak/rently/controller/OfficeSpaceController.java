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

import com.nikoladronjak.rently.dto.OfficeSpaceDTO;
import com.nikoladronjak.rently.service.OfficeSpaceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/officespaces")
public class OfficeSpaceController {

	@Autowired
	private OfficeSpaceService officeSpaceService;

	@GetMapping
	public ResponseEntity<?> getAllOfficeSpaces() {
		return officeSpaceService.getAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOfficeSpaceById(@PathVariable Integer id) {
		return officeSpaceService.getById(id);
	}

	@PostMapping
	public ResponseEntity<?> addOfficeSpace(@Valid @RequestBody OfficeSpaceDTO officeSpaceDTO) {
		return officeSpaceService.add(officeSpaceDTO);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateOfficeSpace(@PathVariable Integer id,
			@Valid @RequestBody OfficeSpaceDTO officeSpaceDTO) {
		return officeSpaceService.update(id, officeSpaceDTO);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOfficeSpace(@PathVariable Integer id) {
		return officeSpaceService.delete(id);
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
