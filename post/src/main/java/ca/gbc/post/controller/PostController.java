package ca.gbc.post.controller;

import ca.gbc.post.dto.PostRequest;
import ca.gbc.post.dto.PostResponse;
import ca.gbc.post.service.PostServiceImpl;
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
@RequestMapping("/api/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostServiceImpl postService;

    // Endpoint for creating a new post
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPost(@RequestBody PostRequest postRequest) {
        postService.createPost(postRequest);
    }

    // Endpoint to retrieve a list of all posts
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @CircuitBreaker(name = "spring", fallbackMethod = "errorFallback")
    @TimeLimiter(name = "spring")
    @Retry(name = "spring")
    public CompletableFuture<List<PostResponse>> getAllPosts() {
        return CompletableFuture.supplyAsync(postService::getAllPosts);
    }

    // Endpoint to update a post
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable("postId") String postId, @RequestBody PostRequest postRequest) {
        String updatePostId = postService.updatePost(postId, postRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/post/" + updatePostId);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    // Endpoint to delete a post
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") String postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public CompletableFuture<String> errorFallback(RuntimeException exception) {
        log.error("{0}", exception);
        return CompletableFuture.supplyAsync(() -> "FALLBACK: intercommunication error");
    }
}
