package ca.gbc.comment.repository;

import ca.gbc.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
//The CommentRepository interface extends JpaRepository to manage Comment entities.
// This interface provides methods to perform CRUD operations (Create, Read, Update, Delete)
// and other querying functionalities for the 'Comment' entity in the database.