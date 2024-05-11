package com.nikoladronjak.rently.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nikoladronjak.rently.domain.OfficeSpace;

@Repository
public interface OfficeSpaceRepository extends JpaRepository<OfficeSpace, Integer> {

	List<OfficeSpace> findAllByOwner_OwnerId(Integer ownerId);

	Optional<OfficeSpace> findByAddress(String address);
}
