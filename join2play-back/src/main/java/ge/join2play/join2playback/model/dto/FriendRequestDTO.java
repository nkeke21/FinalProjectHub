package ge.join2play.join2playback.model.dto;

import java.util.UUID;

public class FriendRequestDTO {
    private UUID fromUserId;
    private UUID toUserId;

    public FriendRequestDTO() {}

    public FriendRequestDTO(UUID fromUserId, UUID toUserId) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
    }

    public UUID getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(UUID fromUserId) {
        this.fromUserId = fromUserId;
    }

    public UUID getToUserId() {
        return toUserId;
    }

    public void setToUserId(UUID toUserId) {
        this.toUserId = toUserId;
    }
}