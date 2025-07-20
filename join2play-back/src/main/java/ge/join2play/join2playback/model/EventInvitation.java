package ge.join2play.join2playback.model;

import ge.join2play.join2playback.model.enums.EventInvitationStatus;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class EventInvitation {
    private UUID invitationId;
    private UUID eventId;
    private UUID fromUserId;
    private UUID toUserId;
    private LocalDateTime sentAt;
    private EventInvitationStatus status;

    public EventInvitation() {}

    public EventInvitation(UUID invitationId, UUID eventId, UUID fromUserId, UUID toUserId, LocalDateTime sentAt, EventInvitationStatus status) {
        this.invitationId = invitationId;
        this.eventId = eventId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.sentAt = sentAt;
        this.status = status;
    }

    public UUID getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(UUID invitationId) {
        this.invitationId = invitationId;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public UUID getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(UUID fromUserId) {
        this.fromUserId = fromUserId;
    }

    public UUID getToUserId() {
        return toUserId;
    }

    public void setToUserId(UUID toUserId) {
        this.toUserId = toUserId;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public EventInvitationStatus getStatus() {
        return status;
    }

    public void setStatus(EventInvitationStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventInvitation that = (EventInvitation) o;
        return Objects.equals(invitationId, that.invitationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invitationId);
    }
}
