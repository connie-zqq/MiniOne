package edu.northeastern.minione.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import edu.northeastern.minione.model.User;
import edu.northeastern.minione.repository.UserRepository;

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
    public User findUserByUserName(String userName) {
        return this.userRepository.findByUserName(userName);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return this.userRepository.findById(id);
    }

    @Override
    public User createUser(User user) {
        user.setPasswordHash(bCryptPasswordEncoder.encode(user.getPasswordHash()));
        return this.userRepository.save(user);
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
