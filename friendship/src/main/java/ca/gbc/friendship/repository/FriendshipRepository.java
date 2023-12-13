package ca.gbc.friendship.repository;

import ca.gbc.friendship.model.Friendship;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FriendshipRepository extends MongoRepository<Friendship, String> {

    @DeleteQuery
    void deleteById(String userId); // Custom delete method for deleting friendships by user ID
}