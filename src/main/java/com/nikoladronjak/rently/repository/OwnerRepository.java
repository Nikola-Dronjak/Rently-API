package com.nikoladronjak.rently.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nikoladronjak.rently.domain.Owner;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Integer> {

	Optional<Owner> findByEmail(String email);
}
