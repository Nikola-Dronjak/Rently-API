package com.nikoladronjak.rently.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nikoladronjak.rently.domain.OfficeSpace;

/**
 * Represents a repository interface for accessing and managing OfficeSpace
 * entities in the database. This interface extends the JpaRepository interface,
 * which provides the basic CRUD operations for OfficeSpace entities.
 * 
 * @author Nikola Dronjak
 */
@Repository
public interface OfficeSpaceRepository extends JpaRepository<OfficeSpace, Integer> {

	/**
	 * Retrieves a list of OfficeSpace entities by their ownerId.
	 * 
	 * @param ownerId The id of the owner associated with the office spaces that are
	 *                being queried.
	 * @return A list of OfficeSpace entities associated with a specific ownerId. If
	 *         there are no OfficeSpace entities for the given ownerId, it returns
	 *         an empty list.
	 */
	List<OfficeSpace> findAllByOwner_OwnerId(Integer ownerId);

	/**
	 * Retrieves an OfficeSpace entity by its street address.
	 * 
	 * @param address The street address of the office space being queried.
	 * @return An Optional containing the OfficeSpace entity if found, or empty if
	 *         not found.
	 */
	Optional<OfficeSpace> findByAddress(String address);
}
