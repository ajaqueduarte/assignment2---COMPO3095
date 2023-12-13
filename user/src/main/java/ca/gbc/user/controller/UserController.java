package ca.gbc.user.controller;

import ca.gbc.user.dto.LoginRequest;
import ca.gbc.user.dto.UserRequest;
import ca.gbc.user.dto.UserResponse;
import ca.gbc.user.dto.UserResponseFriends;
import ca.gbc.user.service.UserServiceImpl;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    // Constructor-based dependency injection
    private final UserServiceImpl userService;

    // Endpoint for creating a new user
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserRequest userRequest) {
        userService.createUser(userRequest);
    }

    // Endpoint for user login
    @PostMapping("/loginUser")
    @ResponseStatus(HttpStatus.OK)
    @CircuitBreaker(name = "spring", fallbackMethod = "errorFallback")
    @TimeLimiter(name = "spring")
    @Retry(name = "spring")
    public CompletableFuture<UserResponseFriends> loginUser(@RequestBody LoginRequest userLogin) {
        return CompletableFuture.supplyAsync(() -> userService.loginUser(userLogin.getUsername(), userLogin.getPassword()));
    }

    // Endpoint to get a user by their ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Endpoint to update user information
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        String updatedUserId = userService.updateUser(id, userRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/users/" + updatedUserId);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    // Endpoint to delete a user by their ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Endpoint to get a list of all users
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    public CompletableFuture<String> errorFallback(RuntimeException exception) {
        log.error("{0}", exception);
        return CompletableFuture.supplyAsync(() -> "FALLBACK: intercommunication error");
    }
}
