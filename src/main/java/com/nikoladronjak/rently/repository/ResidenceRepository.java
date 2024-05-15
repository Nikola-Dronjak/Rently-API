package com.nikoladronjak.rently.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nikoladronjak.rently.domain.Residence;

/**
 * Represents a repository interface for accessing and managing Residence
 * entities in the database. This interface extends the JpaRepository interface,
 * which provides the basic CRUD operations for Residence entities.
 * 
 * @author Nikola Dronjak
 */
@Repository
public interface ResidenceRepository extends JpaRepository<Residence, Integer> {

	/**
	 * Retrieves a list of Residence entities by their ownerId.
	 * 
	 * @param ownerId The id of the owner associated with the residences that are
	 *                being queried.
	 * @return A list of Residence entities associated with a specific ownerId. If
	 *         there are no Residence entities for the given ownerId, it returns an
	 *         empty list.
	 */
	List<Residence> findAllByOwner_OwnerId(Integer ownerId);

	/**
	 * Retrieves a Residence entity by its street address.
	 * 
	 * @param address The street address of the residence being queried.
	 * @return An Optional containing the Residence entity if found, or empty if not
	 *         found.
	 */
	Optional<Residence> findByAddress(String address);
}
