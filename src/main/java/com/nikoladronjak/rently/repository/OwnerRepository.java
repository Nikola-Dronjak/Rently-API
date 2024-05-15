package com.nikoladronjak.rently.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nikoladronjak.rently.domain.Owner;

/**
 * Represents a repository interface for accessing and managing Owner entities
 * in the database. This interface extends the JpaRepository interface, which
 * provides the basic CRUD operations for Owner entities.
 * 
 * @author Nikola Dronjak
 */
@Repository
public interface OwnerRepository extends JpaRepository<Owner, Integer> {

	/**
	 * Retrieves an Owner entity by its email address.
	 * 
	 * @param email The email address of the owner being queried.
	 * @return An Optional containing the Owner entity if found, or empty if not
	 *         found.
	 */
	Optional<Owner> findByEmail(String email);
}
