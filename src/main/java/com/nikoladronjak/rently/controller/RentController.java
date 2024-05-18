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

import com.nikoladronjak.rently.dto.RentDTO;
import com.nikoladronjak.rently.service.RentService;

/**
 * Represents a controller class for handling HTTP requests related to Rent
 * entities. This class provides end-points for retrieving, adding, updating,
 * and deleting Rent entities. It also handles validation errors by returning
 * appropriate responses.
 * 
 * @author Nikola Dronjak
 */
@RestController
@RequestMapping("/api/rents")
public class RentController {

	/**
	 * Service for handling operations related to Rent entities.
	 */
	@Autowired
	private RentService rentService;

	/**
	 * Retrieves all rents. Route: HTTP GET /api/rents
	 *
	 * @return ResponseEntity with HTTP status and response body containing a list
	 *         of rents or an error message.
	 */
	@GetMapping
	public ResponseEntity<?> getAllRents() {
		return rentService.getAll();
	}

	/**
	 * Retrieves a rent by its id. Route: HTTP GET /api/rents/id
	 *
	 * @param id The id of the rent thats is being queried.
	 * @return ResponseEntity with HTTP status and response body containing the rent
	 *         if found, or an error message.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getRentById(@PathVariable Integer id) {
		return rentService.getById(id);
	}

	/**
	 * Adds a new rent. Route: HTTP POST /api/rents
	 *
	 * @param rentDTO The RentDTO representing the rent that is being added.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         added rent if successful, or an error message.
	 */
	@PostMapping
	public ResponseEntity<?> addRent(@RequestBody RentDTO rentDTO) {
		return rentService.add(rentDTO);
	}

	/**
	 * Updates an existing rent. Route: HTTP PUT /api/rents/id
	 *
	 * @param id      The id of the rent that is being updated.
	 * @param rentDTO The RentDTO representing the updated rent information.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         updated rent if successful, or an error message.
	 */
	@PutMapping("/{id}")
	ResponseEntity<?> updateRent(@PathVariable Integer id, @RequestBody RentDTO rentDTO) {
		return rentService.update(id, rentDTO);
	}

	/**
	 * Deletes a rent by its id. Route: HTTP DELETE /api/rents/id
	 *
	 * @param id The id of the rent that is being deleted.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         deleted rent if successful, or an error message.
	 */
	@DeleteMapping("/{id}")
	ResponseEntity<?> deleteRent(@PathVariable Integer id) {
		return rentService.delete(id);
	}
}
