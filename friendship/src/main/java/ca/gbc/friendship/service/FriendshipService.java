package ca.gbc.friendship.service;

import ca.gbc.friendship.dto.FriendshipRequest;
import ca.gbc.friendship.dto.FriendshipResponse;

public interface FriendshipService {

    // Method to add a friend
    void addFriend(FriendshipRequest friendshipRequest);

    // Method to get the friend list for a user
    FriendshipResponse getFriendList(Long userId);

    // Method to delete a friend
    void deleteFriend(FriendshipRequest friendshipRequest);
}
