package com.nikoladronjak.rently.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nikoladronjak.rently.domain.Utility;

/**
 * Represents a repository interface for accessing and managing Utility entities
 * in the database. This interface extends the JpaRepository interface, which
 * provides the basic CRUD operations for Utility entities.
 * 
 * @author Nikola Dronjak
 */
@Repository
public interface UtilityRepository extends JpaRepository<Utility, Integer> {

	/**
	 * Retrieves a Utility entity by its name.
	 * 
	 * @param name The name of the utility being queried.
	 * @return An Optional containing the Utility entity if found, or empty if not
	 *         found.
	 */
	Optional<Utility> findByName(String name);
}
