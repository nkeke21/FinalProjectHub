package ge.join2play.join2playback.model;

import java.util.UUID;

public class TeamJoinRequestNotification {
    private UUID requestId;
    private UUID teamId;
    private UUID fromUserId;
    private String message;

    public TeamJoinRequestNotification() {
    }

    public TeamJoinRequestNotification(UUID requestId, UUID teamId, UUID fromUserId, String message) {
        this.requestId = requestId;
        this.teamId = teamId;
        this.fromUserId = fromUserId;
        this.message = message;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public void setTeamId(UUID teamId) {
        this.teamId = teamId;
    }

    public UUID getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(UUID fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
} 