package edu.northeastern.minione.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.northeastern.minione.entity.Space;

/**
 * Create the interface SpaceRepository.
 */
@Repository
public interface SpaceRepository extends JpaRepository<Space, Long> {

}
