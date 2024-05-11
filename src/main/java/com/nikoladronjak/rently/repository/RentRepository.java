package com.nikoladronjak.rently.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nikoladronjak.rently.domain.Rent;

@Repository
public interface RentRepository extends JpaRepository<Rent, Integer> {

	Optional<Rent> findByLeaseLeaseId(Integer leaseId);

	Optional<Rent> findByUtilityLeasesUtilityLeaseId(Integer utilityLeaseId);
}
