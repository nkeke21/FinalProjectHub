package ge.join2play.join2playback.model;

import ge.join2play.join2playback.model.enums.EventInvitationStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "event_invitations")
public class EventInvitation {
    @Id
    @Column(name = "invitation_id")
    private UUID invitationId;

    @Column(name = "event_id", nullable = false)
    private UUID eventId;

    @Column(name = "from_user_id", nullable = false)
    private UUID fromUserId;

    @Column(name = "to_user_id", nullable = false)
    private UUID toUserId;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EventInvitationStatus status;

    // Default constructor for JPA
    public EventInvitation() {}

    public EventInvitation(UUID invitationId, UUID eventId, UUID fromUserId, UUID toUserId, LocalDateTime sentAt, EventInvitationStatus status) {
        this.invitationId = invitationId;
        this.eventId = eventId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.sentAt = sentAt;
        this.status = status;
    }

    // Getters and Setters
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
