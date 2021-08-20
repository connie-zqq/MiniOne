package edu.northeastern.minione.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.northeastern.minione.model.User;

public interface UserService {

    boolean authentication(String userName, String password);

    List<User> findAllUsers();

    Page<User> findAllUsers(Pageable pageable);

    User findUserByUserName(String userName);

    User findUserById(Long id);

    void createUser(User user);

    User editUser(User user);

    void deleteUserById(Long id);
}

