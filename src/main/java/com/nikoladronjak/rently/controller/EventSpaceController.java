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

import com.nikoladronjak.rently.dto.EventSpaceDTO;
import com.nikoladronjak.rently.service.EventSpaceService;

/**
 * Represents a controller class for handling HTTP requests related to
 * EventSpace entities. This class provides end-points for retrieving, adding,
 * updating, and deleting EventSpace entities. It also handles validation errors
 * by returning appropriate responses.
 * 
 * @author Nikola Dronjak
 */
@RestController
@RequestMapping("/api/eventspaces")
public class EventSpaceController {

	/**
	 * Service for handling operations related to EventSpace entities.
	 */
	@Autowired
	private EventSpaceService eventSpaceService;

	/**
	 * Retrieves all event spaces. Route: HTTP GET /api/eventspaces
	 *
	 * @return ResponseEntity with HTTP status and response body containing a list
	 *         of event spaces or an error message.
	 */
	@GetMapping
	public ResponseEntity<?> getAllEventSpaces() {
		return eventSpaceService.getAll();
	}

	/**
	 * Retrieves an event space by its id. Route: HTTP GET /api/eventspaces/id
	 *
	 * @param id The id of the event space thats is being queried.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         event space if found, or an error message.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getEventSpaceById(@PathVariable Integer id) {
		return eventSpaceService.getById(id);
	}

	/**
	 * Adds a new event space. Route: HTTP POST /api/eventspaces
	 *
	 * @param eventSpaceDTO The EventSpaceDTO representing the event space that is
	 *                      being added.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         added event space if successful, or an error message.
	 */
	@PostMapping
	public ResponseEntity<?> addEventSpace(@RequestBody EventSpaceDTO eventSpaceDTO) {
		return eventSpaceService.add(eventSpaceDTO);
	}

	/**
	 * Updates an existing event space. Route: HTTP PUT /api/eventspaces/id
	 *
	 * @param id            The id of the event space that is being updated.
	 * @param eventSpaceDTO The EventSpaceDTO representing the updated event space
	 *                      information.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         updated event space if successful, or an error message.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> updateEventSpace(@PathVariable Integer id, @RequestBody EventSpaceDTO eventSpaceDTO) {
		return eventSpaceService.update(id, eventSpaceDTO);
	}

	/**
	 * Deletes an event space by its id. Route: HTTP DELETE /api/eventspaces/id
	 *
	 * @param id The id of the event space that is being deleted.
	 * @return ResponseEntity with HTTP status and response body containing the
	 *         deleted event space if successful, or an error message.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteEventSpace(@PathVariable Integer id) {
		return eventSpaceService.delete(id);
	}
}
