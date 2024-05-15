package com.nikoladronjak.rently.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nikoladronjak.rently.domain.Customer;

/**
 * Represents a repository interface for accessing and managing Customer
 * entities in the database. This interface extends the JpaRepository interface,
 * which provides the basic CRUD operations for Customer entities.
 * 
 * @author Nikola Dronjak
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	/**
	 * Retrieves a Customer entity by its email address.
	 * 
	 * @param email The email address of the customer being queried.
	 * @return An Optional containing the Customer entity if found, or empty if not
	 *         found.
	 */
	Optional<Customer> findByEmail(String email);
}
