package ge.join2play.join2playback.repository.interfaces;

import ge.join2play.join2playback.model.EventInvitation;
import ge.join2play.join2playback.model.enums.EventInvitationStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventInvitationRepository {
    EventInvitation saveInvitation(UUID eventId, UUID fromUserId, UUID toUserId);
    List<EventInvitation> getPendingInvitationsForUser(UUID userId);
    List<EventInvitation> getAllInvitationsForUser(UUID userId);
    List<EventInvitation> getInvitationsForEvent(UUID eventId);
    Optional<EventInvitation> findById(UUID invitationId);
    void updateStatus(UUID invitationId, EventInvitationStatus status);
    Optional<EventInvitation> findInvitationBetweenEventAndUser(UUID eventId, UUID userId);
    boolean existsPendingInvitation(UUID eventId, UUID userId);
    void deleteInvitation(UUID invitationId);
    List<EventInvitation> getInvitationsSentByUser(UUID userId);
}
