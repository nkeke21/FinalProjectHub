package ge.join2play.join2playback.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tournament_registration_notifications")
public class TournamentRegistrationNotification {
    @Id
    private UUID id;

    @Column(name = "tournament_id", nullable = false)
    private UUID tournamentId;

    @Column(name = "tournament_name", nullable = false, length = 255)
    private String tournamentName;

    @Column(name = "requester_id", nullable = false)
    private UUID requesterId;

    @Column(name = "requester_name", nullable = false, length = 255)
    private String requesterName;

    @Column(name = "host_id", nullable = false)
    private UUID hostId;

    @Column(name = "host_name", nullable = false, length = 255)
    private String hostName;

    @Column(name = "registration_id", nullable = false)
    private UUID registrationId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    // Default constructor for JPA
    public TournamentRegistrationNotification() {}

    public TournamentRegistrationNotification(UUID tournamentId, String tournamentName,
                                              UUID requesterId, String requesterName,
                                              UUID hostId, String hostName,
                                              UUID registrationId) {
        this.id = UUID.randomUUID();
        this.tournamentId = tournamentId;
        this.tournamentName = tournamentName;
        this.requesterId = requesterId;
        this.requesterName = requesterName;
        this.hostId = hostId;
        this.hostName = hostName;
        this.registrationId = registrationId;
        this.createdAt = Instant.now();
        this.isRead = false;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(UUID tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public UUID getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(UUID requesterId) {
        this.requesterId = requesterId;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public UUID getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(UUID registrationId) {
        this.registrationId = registrationId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
