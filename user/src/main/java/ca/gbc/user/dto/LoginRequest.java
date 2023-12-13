package ca.gbc.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    // Data transfer object (DTO) for user login request

    private String username; // User's username
    private String password; // User's password
}
