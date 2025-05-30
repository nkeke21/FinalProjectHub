package ge.join2play.join2playback.model;

import ge.join2play.join2playback.enums.FriendRequestStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class FriendRequest {
    private UUID requestId;
    private UUID fromUserId;
    private UUID toUserId;
    private LocalDateTime sentAt;
    private FriendRequestStatus status;

    public FriendRequest(UUID requestId, UUID fromUserId, UUID toUserId, LocalDateTime sentAt, FriendRequestStatus status) {
        this.requestId = requestId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.sentAt = sentAt;
        this.status = status;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
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

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public FriendRequestStatus getStatus() {
        return status;
    }

    public void setStatus(FriendRequestStatus status) {
        this.status = status;
    }
}
