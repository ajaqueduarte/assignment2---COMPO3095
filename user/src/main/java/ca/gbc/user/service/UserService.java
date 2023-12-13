package ca.gbc.user.service;

import ca.gbc.user.dto.UserRequest;
import ca.gbc.user.dto.UserResponse;
import ca.gbc.user.dto.UserResponseFriends;

import java.util.List;

public interface UserService {
    // Interface defining the contract for user-related operations

    void createUser(UserRequest userRequest);  // Method to create a new user

    String updateUser(Long userId, UserRequest userRequest);  // Method to update user information

    UserResponseFriends loginUser(String username, String password);  // Method to authenticate and log in a user

    void deleteUser(Long userId);  // Method to delete a user

    List<UserResponse> getAllUsers();  // Method to retrieve a list of all users

    UserResponse getUserById(Long userId);  // Method to retrieve user information by their ID
}
