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

import com.nikoladronjak.rently.dto.UtilityLeaseDTO;
import com.nikoladronjak.rently.service.UtilityLeaseService;

/**
 * Represents a controller class for handling HTTP requests related to
 * UtilityLease entities. This class provides end-points for retrieving, adding,
 * updating, and deleting UtilityLease entities. It also handles validation
 * errors by returning appropriate responses.
 * 
 * @author Nikola Dronjak
 */
@RestController
@RequestMapping("/api/utilityleases")
public class UtilityLeaseController {

	/**
	 * Service for handling operations related to UtilityLease entities.
	 */
	@Autowired
	private UtilityLeaseService utilityLeaseService;

	/**
	 * Retrieves all utility leases. Route: HTTP GET /api/utilityleases
	 *
	 * @return ResponseEntity with HTTP status and response body containing a list
	 *         of utility leases or an error message.
	 */
	@GetMapping
	public ResponseEntity<?> getAllUtilityLeases() {
		return utilityLeaseService.getAll();
	}

	/**
	 * Retrieves all utility leases for a specified utilityId. Route: HTTP GET
	 * /api/utilityleases/utility/utilityId
	 *
	 * @param utilityId The id of the utility for which the utility leases are being
	 *                  queried.
	 * @return ResponseEntity with HTTP status and response body containing a list
	 *         of utility leases or an error message.
	 */
	@GetMapping("/utility/{utilityId}")
	public ResponseEntity<?> getAllUtilityLeasesByUtilityId(@PathVariable Integer utilityId) {
		return utilityLeaseService.getAllByUtilityId(utilityId);
	}

	/**
	 * Retrieves all utility leases for a specified propertyId. Route: HTTP GET
	 * /api/utilityleases/property/propertyId
	 *
	 * @param propertyId The id of the property for which the utility leases are
	 *                   being queried.
	 * @return ResponseEntity with HTTP status and response body containing a list
	 *         of utility leases or an error message.
	 */
	@GetMapping("/property/{propertyId}")
	public ResponseEntity<?> getAllUtilityLeasesByPropertyId(@PathVariable Integer propertyId) {
		return utilityLeaseService.getAllByPropertyId(propertyId);
	}

	/**
	 * Retrieves a utility lease by its id. Route: HTTP GET /api/utilityleases/id
	 *
	 * @param id The id of the utility lease thats is being queried.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         utility lease if found, or an error message.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getUtilityLeaseById(@PathVariable Integer id) {
		return utilityLeaseService.getById(id);
	}

	/**
	 * Adds a new utility lease. Route: HTTP POST /api/utilityleases
	 *
	 * @param utilityLeaseDTO The UtilityLeaseDTO representing the utiliy lease that
	 *                        is being added.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         added utility lease if successful, or an error message.
	 */
	@PostMapping
	public ResponseEntity<?> addUtilityLease(@RequestBody UtilityLeaseDTO utilityLeaseDTO) {
		return utilityLeaseService.add(utilityLeaseDTO);
	}

	/**
	 * Updates an existing utility lease. Route: HTTP PUT /api/utilityleases/id
	 *
	 * @param id              The id of the utility lease that is being updated.
	 * @param utilityLeaseDTO The UtilityLeaseDTO representing the updated utility
	 *                        lease information.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         updated utility lease if successful, or an error message.
	 */
	@PutMapping("/{id}")
	ResponseEntity<?> updateUtilityLease(@PathVariable Integer id, @RequestBody UtilityLeaseDTO utilityLeaseDTO) {
		return utilityLeaseService.update(id, utilityLeaseDTO);
	}

	/**
	 * Deletes a utility lease by its id. Route: HTTP DELETE /api/utilityleases/id
	 *
	 * @param id The id of the utility lease that is being deleted.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         deleted utility lease if successful, or an error message.
	 */
	@DeleteMapping("/{id}")
	ResponseEntity<?> deleteUtilityLease(@PathVariable Integer id) {
		return utilityLeaseService.delete(id);
	}
}
