package edu.northeastern.minione;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The is an application class.
 * The @SpringBootApplication annotation is equivalent to using @Configuration, @EnableAutoConfiguration,
 * and @ComponentScan with their default attributes.
 * <p>
 * ref: https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/using-boot-using-springbootapplication-annotation.html
 * https://docs.spring.io/spring-boot/docs/1.1.4.RELEASE/reference/html/getting-started-first-application.html
 *
 * @author Qiangqiang Zhang, Shujun Xiao
 * @version 1.0
 * @since 2021_08_19
 */
@SpringBootApplication
public class MiniOneApplication {
    /**
     * The is the main method of the application. This is just a standard method that follows the Java convention
     * for an application entry point. Our main method delegates to Spring Boot’s SpringApplication class by calling
     * run.
     *
     * @param args The args array is passed through to expose any command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(MiniOneApplication.class, args);
    }

    @PostConstruct
    /**
     * Set the default time zone of the application
     */
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("PST"));
    }
}
