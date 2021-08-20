package edu.northeastern.minione.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The class is used to customize the default Spring MVC configuration.
 */
@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * The method is to map urls to the corresponding views.
     */
    public void addViewControllers(ViewControllerRegistry registry) {
        //  Add view controllers to create a direct mapping between a URL path and view name without
        //  the need for a controller in between.
        registry.addViewController("/home").setViewName("index");
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("users/login");
        registry.addViewController("/register").setViewName("users/register");
    }

    /**
     * The method is used to add handlers to serve static resources from specific locations.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                "/webjars/**",
                "/img/**",
                "/css/**",
                "/js/**")
                .addResourceLocations(
                        "/webjars/**",
                        "classpath:/public/img/",
                        "classpath:/public/css/",
                        "classpath:/public/js/");

    }

    /**
     * Define the BCryptPasswordEncoder as a bean in the configuration.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }
}
