package ca.gbc.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UserApplication {
    // Main application class for the User Management system

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
        // Launches the Spring Boot application
    }
}

