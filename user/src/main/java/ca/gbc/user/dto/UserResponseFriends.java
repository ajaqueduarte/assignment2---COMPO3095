package ca.gbc.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseFriends {
    // Data transfer object (DTO) for representing user information in responses

    private Long id;         // User's unique identifier
    private String username; // User's username
    private String email;    // User's email address
    private String password; // User's password (Note: Be cautious about exposing passwords in responses)
    private List<UserResponse> friends;
}