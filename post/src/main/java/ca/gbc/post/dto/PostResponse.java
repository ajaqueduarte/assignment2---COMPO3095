package ca.gbc.post.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    // Data transfer object (DTO) for representing a post in responses

    private String id;       // Post's unique identifier
    private Long userId;
    private String title;    // Post's title
    private String content;  // Post's content
    private String author;   // Author of the post
    private UserResponse user;
    private List<CommentResponse> comments;
}

