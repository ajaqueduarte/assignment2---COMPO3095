package ca.gbc.post.repository;

import ca.gbc.post.model.Post;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface PostRepository extends MongoRepository<Post, String> {
    @DeleteQuery
    void deleteById(String postId); // Custom delete query method to delete a post by its ID
}
