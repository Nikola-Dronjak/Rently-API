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

import com.nikoladronjak.rently.dto.OfficeSpaceDTO;
import com.nikoladronjak.rently.service.OfficeSpaceService;

/**
 * Represents a controller class for handling HTTP requests related to
 * OfficeSpace entities. This class provides end-points for retrieving, adding,
 * updating, and deleting OfficeSpace entities. It also handles validation
 * errors by returning appropriate responses.
 * 
 * @author Nikola Dronjak
 */
@RestController
@RequestMapping("/api/officespaces")
public class OfficeSpaceController {

	/**
	 * Service for handling operations related to OfficeSpace entities.
	 */
	@Autowired
	private OfficeSpaceService officeSpaceService;

	/**
	 * Retrieves all office spaces. Route: HTTP GET /api/officespaces
	 *
	 * @return ResponseEntity with HTTP status and response body containing a list
	 *         of office spaces or an error message.
	 */
	@GetMapping
	public ResponseEntity<?> getAllOfficeSpaces() {
		return officeSpaceService.getAll();
	}

	/**
	 * Retrieves a office space by its id. Route: HTTP GET /api/officespaces/id
	 *
	 * @param id The id of the office space thats is being queried.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         office space if found, or an error message.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getOfficeSpaceById(@PathVariable Integer id) {
		return officeSpaceService.getById(id);
	}

	/**
	 * Adds a new office space. Route: HTTP POST /api/officespaces
	 *
	 * @param officeSpaceDTO The OfficeSpaceDTO representing the office space that
	 *                       is being added.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         added office space if successful, or an error message.
	 */
	@PostMapping
	public ResponseEntity<?> addOfficeSpace(@RequestBody OfficeSpaceDTO officeSpaceDTO) {
		return officeSpaceService.add(officeSpaceDTO);
	}

	/**
	 * Updates an existing office space. Route: HTTP PUT /api/officespaces/id
	 *
	 * @param id             The id of the office space that is being updated.
	 * @param officeSpaceDTO The OfficeSpaceDTO representing the updated office
	 *                       space information.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         updated office space if successful, or an error message.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> updateOfficeSpace(@PathVariable Integer id, @RequestBody OfficeSpaceDTO officeSpaceDTO) {
		return officeSpaceService.update(id, officeSpaceDTO);
	}

	/**
	 * Deletes a office space by its id. Route: HTTP DELETE /api/officespaces/id
	 *
	 * @param id The id of the office space that is being deleted.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         deleted office space if successful, or an error message.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOfficeSpace(@PathVariable Integer id) {
		return officeSpaceService.delete(id);
	}
}
