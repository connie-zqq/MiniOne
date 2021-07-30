package edu.northeastern.minione.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.northeastern.minione.model.Follow;

/**
 * Create the interface SpaceRepository.
 */
@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

}
