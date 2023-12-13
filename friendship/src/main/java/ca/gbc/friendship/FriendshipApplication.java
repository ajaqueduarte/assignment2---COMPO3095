package ca.gbc.friendship;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FriendshipApplication {

    public static void main(String[] args) {
        SpringApplication.run(FriendshipApplication.class, args);
    }

}
//This is the main entry point for the Friendship Application.