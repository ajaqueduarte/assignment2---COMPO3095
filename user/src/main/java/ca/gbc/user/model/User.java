package ca.gbc.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "social_user")
public class User {
    // Entity class representing user information in the database

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;         // User's unique identifier
    private String username; // User's username
    private String email;    // User's email address
    private String password; // User's password
}
