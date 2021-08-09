package edu.northeastern.minione.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.northeastern.minione.model.Moment;

/**
 * Create the interface MomentRepository.
 */
@Repository
public interface MomentRepository extends JpaRepository<Moment, Long> {

    Page<Moment> findAllBySpaceId(Long spaceId, Pageable pageable);
}
