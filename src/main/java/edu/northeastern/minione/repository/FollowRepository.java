package edu.northeastern.minione.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.northeastern.minione.model.Follow;

/**
 * Create the interface FollowRepository.
 */
@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    // In that case the method name below creates property traversal x.followedSpace.id ("Property Expression")
    List<Follow> findAllByFollowedSpace_Id(Long id);
}
