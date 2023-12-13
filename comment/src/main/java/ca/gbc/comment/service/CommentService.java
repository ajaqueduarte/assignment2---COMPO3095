package ca.gbc.comment.service;

import ca.gbc.comment.dto.CommentRequest;
import ca.gbc.comment.dto.CommentResponse;

import java.util.List;

public interface CommentService {

    // Method to create a comment
    void createComment(CommentRequest commentRequest);

    // Method to update a comment by commentId
    Long updateComment(Long commentId, CommentRequest commentRequest);

    // Method to delete a comment by commentId
    void deleteComment(Long commentId);

    // Method to retrieve all comments
    List<CommentResponse> getAllComments();

    List<CommentResponse> getComments(String postId);
}
