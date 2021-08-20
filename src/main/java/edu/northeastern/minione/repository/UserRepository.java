package edu.northeastern.minione.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.northeastern.minione.model.User;

/**
 * Create the interface UserRepository.
 * The repository allows us to access the information stored in the database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find out the User object based on the input userName.
     *
     * @param userName The userName of the User that is searching for
     * @return User The user with the input userName
     */
    User findByUserName(String userName);
}
