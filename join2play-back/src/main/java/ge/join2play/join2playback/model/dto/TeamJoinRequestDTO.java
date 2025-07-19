package ge.join2play.join2playback.model.dto;

import java.util.UUID;

public class TeamJoinRequestDTO {
    private UUID teamId;
    private UUID fromUserId;

    public TeamJoinRequestDTO() {
    }

    public TeamJoinRequestDTO(UUID teamId, UUID fromUserId) {
        this.teamId = teamId;
        this.fromUserId = fromUserId;
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
} 