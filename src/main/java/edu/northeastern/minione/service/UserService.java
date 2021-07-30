package edu.northeastern.minione.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.northeastern.minione.model.User;

public interface UserService {

    boolean authentication(String userName, String password);
    List<User> findAllUsers();
    Page<User> findAll(Pageable pageable);
    User findUserByUserName(String userName);
    Optional<User> findUserById(Long id);
    User createUser(User user);
    User editUser(User user);
    void deleteUserById(Long id);
}

