package ca.gbc.friendship.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FriendshipRequest {
    private Long userId;        // ID of the user initiating the friendship action
    private Long otherUserId;   // ID of the user involved in the friendship action
}