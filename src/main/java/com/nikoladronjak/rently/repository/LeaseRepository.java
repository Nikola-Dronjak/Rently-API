package com.nikoladronjak.rently.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nikoladronjak.rently.domain.Lease;

@Repository
public interface LeaseRepository extends JpaRepository<Lease, Integer> {

	List<Lease> findAllByProperty_PropertyId(int propertyId);

	List<Lease> findAllByCustomer_CustomerId(int customerId);

	Optional<Lease> findByProperty_PropertyIdAndCustomer_CustomerId(int propertyId, int customerId);
}
