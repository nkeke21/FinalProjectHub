package ge.join2play.join2playback.model;

import java.time.Instant;
import java.util.UUID;

public class TournamentRegistrationNotification {
    private UUID id;
    private UUID tournamentId;
    private String tournamentName;
    private UUID requesterId;
    private String requesterName;
    private UUID hostId;
    private String hostName;
    private UUID registrationId;
    private Instant createdAt;
    private boolean isRead;

    public TournamentRegistrationNotification() {
    }

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