package edu.northeastern.minione.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.northeastern.minione.service.MomentService;
import edu.northeastern.minione.service.MomentServiceImpl;
import edu.northeastern.minione.service.UserService;
import edu.northeastern.minione.service.UserServiceImpl;

@Configuration
public class SpringConfig {

    @Bean
    public MomentService momentService() {
        return new MomentServiceImpl();
    }

    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }
}
