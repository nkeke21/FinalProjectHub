package ge.join2play.join2playback.model;

import ge.join2play.join2playback.model.enums.TeamJoinRequestStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "team_join_requests")
public class TeamJoinRequest {
    @Id
    @Column(name = "request_id")
    private UUID requestId;

    @Column(name = "team_id", nullable = false)
    private UUID teamId;

    @Column(name = "from_user_id", nullable = false)
    private UUID fromUserId;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TeamJoinRequestStatus status;

    // Default constructor for JPA
    public TeamJoinRequest() {}

    public TeamJoinRequest(UUID requestId, UUID teamId, UUID fromUserId, LocalDateTime sentAt, TeamJoinRequestStatus status) {
        this.requestId = requestId;
        this.teamId = teamId;
        this.fromUserId = fromUserId;
        this.sentAt = sentAt;
        this.status = status;
    }

    // Getters and Setters
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
