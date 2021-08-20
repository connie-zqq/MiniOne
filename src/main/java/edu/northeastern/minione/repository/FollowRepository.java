package edu.northeastern.minione.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.northeastern.minione.model.Follow;
import edu.northeastern.minione.model.Space;
import edu.northeastern.minione.model.User;

/**
 * Create the interface FollowRepository.
 */
@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    // In that case the method name below creates property traversal x.followedSpace.id ("Property Expression")
    /**
     * Find out all the follows that indexed with the input space id and store the info in a list.
     *
     * @param id The id of this Space
     * @return List RETURN a list contain all the Follow with the same input space id
     */
    List<Follow> findAllByFollowedSpaceId(Long id);

    // To query the database that should match both follower and followed space,
    // we create the method below. Behind the scenes, Data JPA will create a SQL query.
    /**
     * Find out the Follow object based on the input user and space.
     *
     * @param user  The follower of the Follow
     * @param space The followedSpace of the Follow
     * @return Follow The Follow that has the input user and space value as its follower and followedSpace
     */
    Follow findByFollowerAndFollowedSpace(User user, Space space);
}
