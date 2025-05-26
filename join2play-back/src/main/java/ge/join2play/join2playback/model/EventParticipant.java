package ge.join2play.join2playback.model;

import java.util.UUID;

public class EventParticipant {
    private UUID id;
    private UUID eventId;
    private UUID participantId;

    public EventParticipant(UUID eventId, UUID id, UUID participantId) {
        this.eventId = eventId;
        this.id = id;
        this.participantId = participantId;
    }

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
