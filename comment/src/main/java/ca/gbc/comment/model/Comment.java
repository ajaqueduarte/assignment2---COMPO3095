package ca.gbc.comment.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;         // Unique identifier for the comment
    private String postId;   // ID of the post this comment belongs to
    private String content;  // Text content of the comment
    private String author;   // Author of the comment
}