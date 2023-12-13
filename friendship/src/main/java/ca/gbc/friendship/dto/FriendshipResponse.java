package ca.gbc.friendship.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FriendshipResponse {
    private Long userId;          // ID of the user for which the friend list is being retrieved
    private List<Long> friendList;  // List of IDs representing the user's friends
}
