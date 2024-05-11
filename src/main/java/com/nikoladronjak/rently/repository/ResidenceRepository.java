package com.nikoladronjak.rently.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nikoladronjak.rently.domain.Residence;

@Repository
public interface ResidenceRepository extends JpaRepository<Residence, Integer> {

	List<Residence> findAllByOwner_OwnerId(Integer ownerId);

	Optional<Residence> findByAddress(String address);
}
