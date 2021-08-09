package edu.northeastern.minione;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MiniOneApplication {
    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("PST"));
    }

    public static void main(String[] args) {
        SpringApplication.run(MiniOneApplication.class, args);
    }
}
