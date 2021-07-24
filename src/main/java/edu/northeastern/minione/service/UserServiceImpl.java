package edu.northeastern.minione.service;

import edu.northeastern.minione.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import edu.northeastern.minione.repository.UserRepository;

import java.util.List;

@Service
@Primary
public class UserServiceImpl implements UserService{

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean authentication(String userName, String password) {

        return false;
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return this.userRepository.findAll(pageable);
    }

    @Override
    public User findByUserName(String userName) {
        return this.userRepository.findByUserName(userName);
    }

    @Override
    public User findById(Long id) {
        return this.userRepository.getOne(id);
    }

    @Override
    public User create(User user) {
        // Encode user's password before adding it to database
        user.setPasswordHash(bCryptPasswordEncoder.encode(user.getPasswordHash()));
        return this.userRepository.save(user);
    }

    @Override
    public User edit(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        this.userRepository.deleteById(id);
    }
}
