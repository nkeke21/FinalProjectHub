package ge.join2play.join2playback.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "event_participants")
public class EventParticipant {
    @Id
    private UUID id;

    @Column(name = "event_id", nullable = false)
    private UUID eventId;

    @Column(name = "participant_id", nullable = false)
    private UUID participantId;

    // Default constructor for JPA
    public EventParticipant() {}

    public EventParticipant(UUID eventId, UUID id, UUID participantId) {
        this.eventId = eventId;
        this.id = id;
        this.participantId = participantId;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public UUID getParticipantId() {
        return participantId;
    }

    public void setParticipantId(UUID participantId) {
        this.participantId = participantId;
    }
}
