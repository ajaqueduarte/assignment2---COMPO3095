package ca.gbc.comment.controller;

import ca.gbc.comment.dto.CommentRequest;
import ca.gbc.comment.dto.CommentResponse;
import ca.gbc.comment.service.CommentServiceImpl;
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
@RequestMapping("/api/comment")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentServiceImpl commentService;
    // Creating a comment via POST request
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createComment(@RequestBody CommentRequest commentRequest) {
        commentService.createComment(commentRequest);
    }
    // Retrieving all comments via GET request
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponse> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    @CircuitBreaker(name = "spring", fallbackMethod = "errorFallback")
    @TimeLimiter(name = "spring")
    @Retry(name = "spring")
    public CompletableFuture<List<CommentResponse>> getComments(@PathVariable("postId") String postId) {
        return CompletableFuture.supplyAsync(() -> commentService.getComments(postId));
    }

    // Updating a comment via PUT request using commentId

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable("commentId") Long commentId, @RequestBody CommentRequest commentRequest) {
        Long updateCommentId = commentService.updateComment(commentId, commentRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/comment/" + updateCommentId);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    // Deleting a comment via DELETE request using commentId
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public CompletableFuture<String> errorFallback(RuntimeException exception) {
        log.error("{0}", exception);
        return CompletableFuture.supplyAsync(() -> "FALLBACK: intercommunication error");
    }
}
