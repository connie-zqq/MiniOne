package edu.northeastern.minione.service;

import edu.northeastern.minione.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    boolean authentication(String userName, String password);

    List<User> findAll();
    Page<User> findAll(Pageable pageable);
    User findByUserName(String userName);
    User findById(Long id);
    User create(User user);
    User edit(User user);
    void deleteById(Long id);
}
