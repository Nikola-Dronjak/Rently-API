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

import com.nikoladronjak.rently.dto.ResidenceDTO;
import com.nikoladronjak.rently.service.ResidenceService;

/**
 * Represents a controller class for handling HTTP requests related to Residence
 * entities. This class provides end-points for retrieving, adding, updating,
 * and deleting Residence entities. It also handles validation errors by
 * returning appropriate responses.
 * 
 * @author Nikola Dronjak
 */
@RestController
@RequestMapping("/api/residences")
public class ResidenceController {

	/**
	 * Service for handling operations related to Residence entities.
	 */
	@Autowired
	private ResidenceService residenceService;

	/**
	 * Retrieves all residences. Route: HTTP GET /api/residences
	 *
	 * @return ResponseEntity with HTTP status and response body containing a list
	 *         of residences or an error message.
	 */
	@GetMapping
	public ResponseEntity<?> getAllResidences() {
		return residenceService.getAll();
	}

	/**
	 * Retrieves a residence by its id. Route: HTTP GET /api/residences/id
	 *
	 * @param id The id of the residence thats is being queried.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         residence if found, or an error message.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getResidenceById(@PathVariable Integer id) {
		return residenceService.getById(id);
	}

	/**
	 * Adds a new residence. Route: HTTP POST /api/residences
	 *
	 * @param residenceDTO The ResidenceDTO representing the residence that is being
	 *                     added.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         added residence if successful, or an error message.
	 */
	@PostMapping
	public ResponseEntity<?> addResidence(@RequestBody ResidenceDTO residenceDTO) {
		return residenceService.add(residenceDTO);
	}

	/**
	 * Updates an existing residence. Route: HTTP PUT /api/residences/id
	 *
	 * @param id           The id of the residence that is being updated.
	 * @param residenceDTO The ResidenceDTO representing the updated residence
	 *                     information.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         updated residence if successful, or an error message.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> updateResidence(@PathVariable Integer id, @RequestBody ResidenceDTO residenceDTO) {
		return residenceService.update(id, residenceDTO);
	}

	/**
	 * Deletes a residence by its id. Route: HTTP DELETE /api/residences/id
	 *
	 * @param id The id of the residence that is being deleted.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         deleted residence if successful, or an error message.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteResidence(@PathVariable Integer id) {
		return residenceService.delete(id);
	}
}
