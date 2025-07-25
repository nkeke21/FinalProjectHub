package ge.join2play.join2playback.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_permissions")
public class UserPermission {
    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "can_host_tournaments", nullable = false)
    private boolean canHostTournaments;

    @Column(name = "granted_by")
    private String grantedBy;

    @Column(name = "granted_at")
    private String grantedAt;

    @Column(columnDefinition = "TEXT")
    private String reason;

    // Default constructor for JPA
    public UserPermission() {}

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
