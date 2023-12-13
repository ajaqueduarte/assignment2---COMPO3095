package ca.gbc.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    private String postId;  // ID of the post the comment belongs to
    private String content; // Text content of the comment
    private String author;  // Author of the comment
}
