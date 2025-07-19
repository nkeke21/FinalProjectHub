package ge.join2play.join2playback.model;

import ge.join2play.join2playback.model.enums.TeamJoinRequestStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class TeamJoinRequest {
    private UUID requestId;
    private UUID teamId;
    private UUID fromUserId;
    private LocalDateTime sentAt;
    private TeamJoinRequestStatus status;

    public TeamJoinRequest(UUID requestId, UUID teamId, UUID fromUserId, LocalDateTime sentAt, TeamJoinRequestStatus status) {
        this.requestId = requestId;
        this.teamId = teamId;
        this.fromUserId = fromUserId;
        this.sentAt = sentAt;
        this.status = status;
    }

    public TeamJoinRequest() {
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

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public TeamJoinRequestStatus getStatus() {
        return status;
    }

    public void setStatus(TeamJoinRequestStatus status) {
        this.status = status;
    }
} 