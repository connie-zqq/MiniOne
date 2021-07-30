package edu.northeastern.minione.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.northeastern.minione.model.User;

/**
 * Create the interface UserRepository. 
 * The repositories allow us to access the information stored in the database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByUserName(String userName);
}
