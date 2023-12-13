package ca.gbc.post.service;

import ca.gbc.post.dto.PostRequest;
import ca.gbc.post.dto.PostResponse;

import java.util.List;

public interface PostService {
    // Interface defining the contract for post-related operations

    void createPost(PostRequest postRequest);  // Method to create a new post

    String updatePost(String postId, PostRequest postRequest);  // Method to update a post

    void deletePost(String postId);  // Method to delete a post

    List<PostResponse> getAllPosts();  // Method to retrieve a list of all posts
}

