package edu.northeastern.minione.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.northeastern.minione.entity.User;
import edu.northeastern.minione.repository.UserRepository;

/**
 * This is a class that implements the user service.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * The default authentication status.
     *
     * @param userName the user name
     * @param password the user's password
     * @return true if the authentication succeed, otherwise false
     */
    @Override
    public boolean authentication(String userName, String password) {
        return false;
    }

    /**
     * Find all users in the database.
     *
     * @return the list of the User objects
     */
    @Override
    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    /**
     * Find all users in the database with pagination.
     *
     * @param pageable the Pageable object that contains the pagination information
     * @return the list of the User objects
     */
    @Override
    public Page<User> findAllUsers(Pageable pageable) {
        return this.userRepository.findAll(pageable);
    }

    /**
     * Find the user by userName.
     *
     * @param userName the user name
     * @return the Space object
     */
    @Override
    public User findUserByUserName(String userName) {
        return this.userRepository.findByUserName(userName);
    }

    /**
     * Find the user by id.
     *
     * @param id the user id
     * @return the User object
     */
    @Override
    public User findUserById(Long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    /**
     * Create a new user in the database.
     *
     * @param user the User object
     */
    @Override
    public void createUser(User user) {
        // Encode user's password before adding it to database
        user.setPasswordHash(bCryptPasswordEncoder.encode(user.getPasswordHash()));
        this.userRepository.save(user);
    }

    /**
     * Edit the user in the database.
     *
     * @param user the User object
     * @return the edited user
     */
    @Override
    public User editUser(User user) {
        return this.userRepository.save(user);
    }

    /**
     * Delete the user by the user id.
     *
     * @param id the user id
     */
    @Override
    public void deleteUserById(Long id) {
        this.userRepository.deleteById(id);
    }
}
