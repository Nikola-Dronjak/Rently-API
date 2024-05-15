package com.nikoladronjak.rently.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nikoladronjak.rently.domain.Lease;

/**
 * Represents a repository interface for accessing and managing Lease entities
 * in the database. This interface extends the JpaRepository interface, which
 * provides the basic CRUD operations for Lease entities.
 * 
 * @author Nikola Dronjak
 */
@Repository
public interface LeaseRepository extends JpaRepository<Lease, Integer> {

	/**
	 * Retrieves a list of Lease entities by their propertyId.
	 * 
	 * @param propertyId The id of the property associated with the leases that are
	 *                   being queried.
	 * @return A list of Lease entities associated with a specific propertyId. If
	 *         there are no Lease entities for the given propertyId, it returns an
	 *         empty list.
	 */
	List<Lease> findAllByProperty_PropertyId(int propertyId);

	/**
	 * Retrieves a list of Lease entities by their customerId.
	 * 
	 * @param customerId The id of the customer associated with the leases that are
	 *                   being queried.
	 * @return A list of Lease entities associated with a specific customerId. If
	 *         there are no Lease entities for the given customerId, it returns an
	 *         empty list.
	 */
	List<Lease> findAllByCustomer_CustomerId(int customerId);

	/**
	 * Retrieves a Lease entity by its propertyId and customerId.
	 * 
	 * @param propertyId The id of the property associated with the lease that is
	 *                   being queried.
	 * @param customerId The id of the customer associated with the lease that is
	 *                   being queried.
	 * @return An Optional containing the Lease entity if found, or empty if not
	 *         found.
	 */
	Optional<Lease> findByProperty_PropertyIdAndCustomer_CustomerId(int propertyId, int customerId);
}
