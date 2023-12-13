package ca.gbc.post.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value="post")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Post {
    // Model class representing a post in a MongoDB database

    @Id
    private String id;       // Post's unique identifier
    private Long userId;
    private String title;    // Post's title
    private String content;  // Post's content
    private String author;   // Author of the post
}

