package com.nikoladronjak.rently.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nikoladronjak.rently.domain.Utility;

@Repository
public interface UtilityRepository extends JpaRepository<Utility, Integer> {

	Optional<Utility> findByName(String name);
}
