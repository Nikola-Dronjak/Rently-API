package com.nikoladronjak.rently.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nikoladronjak.rently.domain.UtilityLease;

/**
 * Represents a repository interface for accessing and managing UtilityLease
 * entities in the database. This interface extends the JpaRepository interface,
 * which provides the basic CRUD operations for UtilityLease entities.
 * 
 * @author Nikola Dronjak
 */
@Repository
public interface UtilityLeaseRepository extends JpaRepository<UtilityLease, Integer> {

	/**
	 * Retrieves a list of UtilityLease entities by their utilityId.
	 * 
	 * @param utilityId The id of the utility associated with the utility leases
	 *                  that are being queried.
	 * @return A list of UtilityLease entities associated with a specific utilityId.
	 *         If there are no UtilityLease entities for the given utilityId, it
	 *         returns an empty list.
	 */
	List<UtilityLease> findAllByUtility_UtilityId(int utilityId);

	/**
	 * Retrieves a list of UtilityLease entities by their propertyId.
	 * 
	 * @param propertyId The id of the property associated with the utility leases
	 *                   that are being queried.
	 * @return A list of UtilityLease entities associated with a specific
	 *         propertyId. If there are no UtilityLease entities for the given
	 *         propertyId, it returns an empty list.
	 */
	List<UtilityLease> findAllByProperty_PropertyId(int propertyId);

	/**
	 * Retrieves a UtilityLease entity by its utilityId and propertyId.
	 * 
	 * @param utilityId  The id of the utility associated with the utility lease
	 *                   that is being queried.
	 * @param propertyId The id of the property associated with the utility lease
	 *                   that is being queried.
	 * @return An Optional containing the UtilityLease entity if found, or empty if
	 *         not found.
	 */
	Optional<UtilityLease> findByUtility_UtilityIdAndProperty_PropertyId(int utilityId, int propertyId);
}