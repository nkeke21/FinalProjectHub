package ge.join2play.join2playback.model;

import ge.join2play.join2playback.model.enums.TeamRole;

import java.time.Instant;
import java.util.UUID;

public class TeamMember {
    private UUID id;
    private UUID teamId;
    private UUID userId;
    private TeamRole role;
    private Instant joinedAt;

    public TeamMember() {
    }

    public TeamMember(UUID id, UUID teamId, UUID userId, TeamRole role, Instant joinedAt) {
        this.id = id;
        this.teamId = teamId;
        this.userId = userId;
        this.role = role;
        this.joinedAt = joinedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public void setTeamId(UUID teamId) {
        this.teamId = teamId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public TeamRole getRole() {
        return role;
    }

    public void setRole(TeamRole role) {
        this.role = role;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }
} 