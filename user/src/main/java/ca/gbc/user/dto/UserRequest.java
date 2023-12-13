package ca.gbc.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    // Data transfer object (DTO) for creating or updating a user

    private String username; // User's username
    private String email;    // User's email address
    private String password; // User's password

}
