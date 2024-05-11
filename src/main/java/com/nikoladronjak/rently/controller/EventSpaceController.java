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

import com.nikoladronjak.rently.dto.EventSpaceDTO;
import com.nikoladronjak.rently.service.EventSpaceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/eventspaces")
public class EventSpaceController {

	@Autowired
	private EventSpaceService eventSpaceService;

	@GetMapping
	public ResponseEntity<?> getAllEventSpaces() {
		return eventSpaceService.getAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getEventSpaceById(@PathVariable Integer id) {
		return eventSpaceService.getById(id);
	}

	@PostMapping
	public ResponseEntity<?> addEventSpace(@Valid @RequestBody EventSpaceDTO eventSpaceDTO) {
		return eventSpaceService.add(eventSpaceDTO);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateEventSpace(@PathVariable Integer id,
			@Valid @RequestBody EventSpaceDTO eventSpaceDTO) {
		return eventSpaceService.update(id, eventSpaceDTO);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteEventSpace(@PathVariable Integer id) {
		return eventSpaceService.delete(id);
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
