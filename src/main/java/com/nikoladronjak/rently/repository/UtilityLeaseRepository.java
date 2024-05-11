package com.nikoladronjak.rently.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nikoladronjak.rently.domain.UtilityLease;

@Repository
public interface UtilityLeaseRepository extends JpaRepository<UtilityLease, Integer> {

	List<UtilityLease> findAllByUtility_UtilityId(int utilityId);

	List<UtilityLease> findAllByProperty_PropertyId(int propertyId);

	Optional<UtilityLease> findByUtility_UtilityIdAndProperty_PropertyId(int utilityId, int propertyId);
}