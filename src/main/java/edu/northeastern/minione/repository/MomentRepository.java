package edu.northeastern.minione.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.northeastern.minione.model.Moment;

/**
 * Create the interface SpaceRepository.
 */
@Repository
public interface MomentRepository extends JpaRepository<Moment, Long> {

}
