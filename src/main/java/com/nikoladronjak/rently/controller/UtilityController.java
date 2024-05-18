package com.nikoladronjak.rently.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nikoladronjak.rently.dto.UtilityDTO;
import com.nikoladronjak.rently.service.UtilityService;

/**
 * Represents a controller class for handling HTTP requests related to Utility
 * entities. This class provides end-points for retrieving, adding, updating,
 * and deleting Utility entities. It also handles validation errors by returning
 * appropriate responses.
 * 
 * @author Nikola Dronjak
 */
@RestController
@RequestMapping("/api/utilities")
public class UtilityController {

	/**
	 * Service for handling operations related to Utility entities.
	 */
	@Autowired
	private UtilityService utilityService;

	/**
	 * Retrieves all utilities. Route: HTTP GET /api/utilities
	 *
	 * @return ResponseEntity with HTTP status and response body containing a list
	 *         of utilities or an error message.
	 */
	@GetMapping
	public ResponseEntity<?> getAllUtilities() {
		return utilityService.getAll();
	}

	/**
	 * Retrieves a utility by its id. Route: HTTP GET /api/utilities/id
	 *
	 * @param id The id of the utility thats is being queried.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         utility if found, or an error message.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getUtilityById(@PathVariable Integer id) {
		return utilityService.getById(id);
	}

	/**
	 * Adds a new utility. Route: HTTP POST /api/utilities
	 *
	 * @param utilityDTO The UtilityDTO representing the utility that is being
	 *                   added.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         added utility if successful, or an error message.
	 */
	@PostMapping
	public ResponseEntity<?> addUtility(@RequestBody UtilityDTO utilityDTO) {
		return utilityService.add(utilityDTO);
	}

	/**
	 * Updates an existing utility. Route: HTTP PUT /api/utilities/id
	 *
	 * @param id         The id of the utility that is being updated.
	 * @param utilityDTO The UtilityDTO representing the updated utility
	 *                   information.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         updated utility if successful, or an error message.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> updateUtility(@PathVariable Integer id, @RequestBody UtilityDTO utilityDTO) {
		return utilityService.update(id, utilityDTO);
	}

	/**
	 * Deletes a utility by its id. Route: HTTP DELETE /api/utilities/id
	 *
	 * @param id The id of the utility that is being deleted.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         deleted utility if successful, or an error message.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUtility(@PathVariable Integer id) {
		return utilityService.delete(id);
	}
}
