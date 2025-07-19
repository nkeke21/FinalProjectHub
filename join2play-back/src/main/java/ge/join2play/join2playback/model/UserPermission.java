package ge.join2play.join2playback.model;

import java.util.UUID;

public class UserPermission {
    private UUID userId;
    private boolean canHostTournaments;
    private String grantedBy;
    private String grantedAt;
    private String reason;

    public UserPermission() {
        // Default constructor for Spring
    }

    public UserPermission(UUID userId, boolean canHostTournaments, String grantedBy, String grantedAt, String reason) {
        this.userId = userId;
        this.canHostTournaments = canHostTournaments;
        this.grantedBy = grantedBy;
        this.grantedAt = grantedAt;
        this.reason = reason;
    }

    // Getters and Setters
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public boolean isCanHostTournaments() {
        return canHostTournaments;
    }

    public void setCanHostTournaments(boolean canHostTournaments) {
        this.canHostTournaments = canHostTournaments;
    }

    public String getGrantedBy() {
        return grantedBy;
    }

    public void setGrantedBy(String grantedBy) {
        this.grantedBy = grantedBy;
    }

    public String getGrantedAt() {
        return grantedAt;
    }

    public void setGrantedAt(String grantedAt) {
        this.grantedAt = grantedAt;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
} 