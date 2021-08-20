package edu.northeastern.minione.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.northeastern.minione.model.Like;
import edu.northeastern.minione.model.Moment;
import edu.northeastern.minione.model.User;

/**
 * Create the interface LikeRepository.
 */
@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    /**
     * Find out the Like object based on the Fan (User object) and the moment.
     *
     * @param user   The Fan of the Like object that is searching for
     * @param moment The likedMoment of the Like object that is searching for
     * @return Like RETURN the Like object with the input moment and user
     */
    Like findByFanAndLikedMoment(User user, Moment moment);
}
