package ca.gbc.friendship.service;

import ca.gbc.friendship.dto.FriendshipRequest;
import ca.gbc.friendship.dto.FriendshipResponse;
import ca.gbc.friendship.model.Friendship;
import ca.gbc.friendship.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Service
@RequiredArgsConstructor
@Slf4j
public class FriendshipImpl implements FriendshipService {
    private final FriendshipRepository friendshipRepository; // Repository for managing Friendships
    private final MongoTemplate mongoTemplate; // MongoDB template for executing queries

    // Method to add a friend
    @Override
    public void addFriend(FriendshipRequest friendshipRequest) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(friendshipRequest.getUserId()));
        Friendship fs = mongoTemplate.findOne(query, Friendship.class);

        if (fs == null) {
            List<Long> friends = new ArrayList<>();
            friends.add(friendshipRequest.getOtherUserId());
            mongoTemplate.save(Friendship
                    .builder()
                    .userId(friendshipRequest.getUserId())
                    .friends(friends)
                    .build());
            return;
        }

        fs.getFriends().add(friendshipRequest.getOtherUserId());
        mongoTemplate.save(fs);
    }

    // Method to delete a friend
    @Override
    public void deleteFriend(FriendshipRequest friendshipRequest) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(friendshipRequest.getUserId()));
        Friendship fs = mongoTemplate.findOne(query, Friendship.class);

        if (fs == null) {
            return;
        }

        fs.getFriends().removeIf(x -> Objects.equals(x, friendshipRequest.getOtherUserId()));
    }

    // Method to get a list of friends
    @Override
    public FriendshipResponse getFriendList(Long userId) {
        Query query = new Query().addCriteria(Criteria.where("userId").is(userId));
        Friendship fs = mongoTemplate.findOne(query, Friendship.class);

        if (fs == null) {
            return null;
        }

        return FriendshipResponse
                .builder()
                .userId(userId)
                .friendList(fs.getFriends())
                .build();
    }
}