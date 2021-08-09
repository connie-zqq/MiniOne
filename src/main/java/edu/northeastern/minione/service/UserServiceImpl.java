package edu.northeastern.minione.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.northeastern.minione.model.User;
import edu.northeastern.minione.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;

    @Override
    public boolean authentication(String userName, String password) {
        return false;
    }

    @Override
    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public Page<User> findAllUsers(Pageable pageable) {
        return this.userRepository.findAll(pageable);
    }

    @Override
    public User findUserByUserName(String userName) {
        return this.userRepository.findByUserName(userName);
    }

    @Override
    public User findUserById(Long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    @Override
    public void createUser(User user) {
        // Encode user's password before adding it to database
        user.setPasswordHash(bCryptPasswordEncoder.encode(user.getPasswordHash()));
        this.userRepository.save(user);
    }

    @Override
    public User editUser(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        this.userRepository.deleteById(id);
    }
}
