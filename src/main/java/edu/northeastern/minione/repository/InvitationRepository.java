package edu.northeastern.minione.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.northeastern.minione.entity.Invitation;

/**
 * Create the interface FollowRepository.
 */
@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    /**
     * Find out all the Invitation that indexed with the input space id and store the info in a list.
     *
     * @param id The space id of the Invitation that is looking for
     * @return List RETURN a list contains all the Invitation that indexed with the input space id
     */
    List<Invitation> findAllBySpaceId(Long id);

    /**
     * Find out all the Invitation that indexed with the input email and store the info in a list.
     *
     * @param email The email of the Invitation that is looking for
     * @return List RETURN a list contains all the Invitation that indexed with the input email
     */
    List<Invitation> findAllByEmail(String email);
}
