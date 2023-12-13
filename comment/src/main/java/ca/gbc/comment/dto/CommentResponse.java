package ca.gbc.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long id;       // Unique identifier for the comment
    private String postId;  // ID of the post associated with the comment
    private String content; // Text content of the comment
    private String author;  // Author of the comment
}
