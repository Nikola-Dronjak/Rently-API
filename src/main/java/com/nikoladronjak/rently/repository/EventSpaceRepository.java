package com.nikoladronjak.rently.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nikoladronjak.rently.domain.EventSpace;

@Repository
public interface EventSpaceRepository extends JpaRepository<EventSpace, Integer> {

	List<EventSpace> findAllByOwner_OwnerId(Integer ownerId);

	Optional<EventSpace> findByAddress(String address);
}
