package com.nikoladronjak.rently.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nikoladronjak.rently.domain.EventSpace;

/**
 * Represents a repository interface for accessing and managing EventSpace
 * entities in the database. This interface extends the JpaRepository interface,
 * which provides the basic CRUD operations for EventSpace entities.
 * 
 * @author Nikola Dronjak
 */
@Repository
public interface EventSpaceRepository extends JpaRepository<EventSpace, Integer> {

	/**
	 * Retrieves a list of EventSpace entities by their ownerId.
	 * 
	 * @param ownerId The id of the owner associated with the event spaces that are
	 *                being queried.
	 * @return A list of EventSpace entities associated with a specific ownerId. If
	 *         there are no EventSpace entities for the given ownerId, it returns an
	 *         empty list.
	 */
	List<EventSpace> findAllByOwner_OwnerId(Integer ownerId);

	/**
	 * Retrieves an EventSpace entity by its street address.
	 * 
	 * @param address The street address of the event space being queried.
	 * @return An Optional containing the EventSpace entity if found, or empty if
	 *         not found.
	 */
	Optional<EventSpace> findByAddress(String address);
}
