package ge.join2play.join2playback.model.dto;

import ge.join2play.join2playback.model.enums.TeamRole;

import java.time.Instant;
import java.util.UUID;

public class TeamMemberResponse {
    private UUID userId;
    private String name;
    private String email;
    private Integer age;
    private TeamRole role;
    private Instant joinedAt;

    public TeamMemberResponse() {
    }

    public TeamMemberResponse(UUID userId, String name, String email, Integer age, TeamRole role, Instant joinedAt) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.age = age;
        this.role = role;
        this.joinedAt = joinedAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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