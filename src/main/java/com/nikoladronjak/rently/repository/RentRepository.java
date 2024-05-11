package com.nikoladronjak.rently.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nikoladronjak.rently.domain.Rent;

@Repository
public interface RentRepository extends JpaRepository<Rent, Integer> {

	List<Rent> findAllByLease_LeaseId(Integer leaseId);

	List<Rent> findAllByUtilityLeases_UtilityLeaseId(Integer utilityLeaseId);

	Optional<Rent> findByLease_LeaseId(Integer leaseId);
}
