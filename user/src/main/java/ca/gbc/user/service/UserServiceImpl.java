package ca.gbc.user.service;

import ca.gbc.user.dto.FriendshipResponse;
import ca.gbc.user.dto.UserRequest;
import ca.gbc.user.dto.UserResponse;
import ca.gbc.user.dto.UserResponseFriends;
import ca.gbc.user.model.User;
import ca.gbc.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final WebClient webClient;
    private final UserRepository userRepository;

    @Value("${friendship.service.uri}")
    private String friendshipService;

    // Method to create a new user
    @Override
    public void createUser(UserRequest userRequest) {
        log.info("Creating a new user with username: {}", userRequest.getUsername());

        User user = User.builder()
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();

        userRepository.save(user);
        log.info("User {} is saved", user.getId());
    }

    // Method to update user information
    @Override
    public String updateUser(Long userId, UserRequest userRequest) {
        log.info("Updating user with ID: {}", userId);

        User query = User.builder().id(userId).build();
        Optional<User> user = userRepository.findOne(Example.of(query));

        if (user.isPresent()) {
            user.get().setUsername(userRequest.getUsername());
            user.get().setEmail(userRequest.getEmail());
            user.get().setPassword(userRequest.getPassword());

            log.info("User {} is updated", user.get().getId());

            return String.valueOf(userRepository.save(user.get()).getId());
        }

        return "";
    }

    // Method to authenticate and log in a user
    @Override
    public UserResponseFriends loginUser(String username, String password) {
        User query = User.builder().username(username).password(password).build();
        Optional<User> user = userRepository.findOne(Example.of(query));

        return user.map(this::mapToUserResponseFriendlist).orElse(null);
    }

    // Method to delete a user
    @Override
    public void deleteUser(Long userId) {
        log.info("Deleting user with ID: {}", userId);
        userRepository.deleteById(userId);
    }

    // Method to retrieve a list of all users
    @Override
    public List<UserResponse> getAllUsers() {
        log.info("Returning a list of users");
        List<User> users = userRepository.findAll();

        return users.stream().map(this::mapToUserResponse).collect(Collectors.toList());
    }

    // Method to retrieve user information by their ID
    @Override
    public UserResponse getUserById(Long userId) {
        log.info("Fetching user with ID: {}", userId);

        User query = User.builder().id(userId).build();
        Optional<User> user = userRepository.findOne(Example.of(query));

        if (user.isPresent()) {
            return mapToUserResponse(user.get());
        }

        log.warn("User with ID {} not found", userId);
        return null;  // Alternatively, you can throw a custom exception
    }

    // Helper method to map User entity to UserResponse DTO
    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    private UserResponseFriends mapToUserResponseFriendlist(User user) {
        UserResponseFriends userResponseFriends = UserResponseFriends.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        FriendshipResponse friendshipResponse = webClient.get()
                .uri(friendshipService + "/" + user.getId())
                .retrieve()
                .bodyToFlux(FriendshipResponse.class)
                .blockFirst();

        assert friendshipResponse != null;
        List<UserResponse> friendlist = new ArrayList<>();
        for (Long userId: friendshipResponse.getFriendList()) {
            friendlist.add(userRepository.findById(userId).map(this::mapToUserResponse).orElse(null));
        }

        userResponseFriends.setFriends(friendlist);
        return userResponseFriends;
    }
}
