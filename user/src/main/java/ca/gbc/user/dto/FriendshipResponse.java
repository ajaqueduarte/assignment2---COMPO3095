package ca.gbc.user.dto;

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
    private List<Long> friendList;  // List of IDs representing the user's friends
}
