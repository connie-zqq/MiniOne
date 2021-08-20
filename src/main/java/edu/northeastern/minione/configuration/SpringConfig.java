package edu.northeastern.minione.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.northeastern.minione.service.CommentService;
import edu.northeastern.minione.service.CommentServiceImpl;
import edu.northeastern.minione.service.FollowService;
import edu.northeastern.minione.service.FollowServiceImpl;
import edu.northeastern.minione.service.MomentService;
import edu.northeastern.minione.service.MomentServiceImpl;
import edu.northeastern.minione.service.UserService;
import edu.northeastern.minione.service.UserServiceImpl;

/**
 * This is a configuration class that defines different service beans in the program.
 * The @Bean annotation tells Spring that a method annotated with @Bean will return an object
 * that should be registered as a bean in the Spring application context.
 */
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

    @Bean
    public FollowService followService() {
        return new FollowServiceImpl();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceImpl();
    }
}
