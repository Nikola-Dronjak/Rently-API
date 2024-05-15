package com.nikoladronjak.rently.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nikoladronjak.rently.domain.Rent;

/**
 * Represents a repository interface for accessing and managing Rent entities in
 * the database. This interface extends the JpaRepository interface, which
 * provides the basic CRUD operations for Rent entities.
 * 
 * @author Nikola Dronjak
 */
@Repository
public interface RentRepository extends JpaRepository<Rent, Integer> {

	/**
	 * Retrieves a list of Rent entities by their leaseId.
	 * 
	 * @param leaseId The id of the lease associated with the rents that are being
	 *                queried.
	 * @return A list of Rent entities associated with a specific leaseId. If there
	 *         are no Rent entities for the given leaseId, it returns an empty list.
	 */
	List<Rent> findAllByLease_LeaseId(Integer leaseId);

	/**
	 * Retrieves a list of Rent entities by their utilityLeaseId.
	 * 
	 * @param utilityLeaseId The id of the utility lease associated with the rents
	 *                       that are being queried.
	 * @return A list of Rent entities associated with a specific utilityLeaseId. If
	 *         there are no Rent entities for the given utilityLeaseId, it returns
	 *         an empty list.
	 */
	List<Rent> findAllByUtilityLeases_UtilityLeaseId(Integer utilityLeaseId);

	/**
	 * Retrieves a Rent entity by its leaseId.
	 * 
	 * @param leaseId The id of the lease associated with the rent that is being
	 *                queried.
	 * @return An Optional containing the Rent entity if found, or empty if not
	 *         found.
	 */
	Optional<Rent> findByLease_LeaseId(Integer leaseId);
}
