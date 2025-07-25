package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.EventInvitation;
import ge.join2play.join2playback.model.enums.EventInvitationStatus;
import ge.join2play.join2playback.repository.interfaces.EventInvitationRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "memory", matchIfMissing = true)
public class EventInvitationInMemoryRepository implements EventInvitationRepository {
    private final Map<UUID, EventInvitation> invitations = new ConcurrentHashMap<>();

    @Override
    public EventInvitation saveInvitation(UUID eventId, UUID fromUserId, UUID toUserId) {
        EventInvitation invitation = new EventInvitation(
                UUID.randomUUID(),
                eventId,
                fromUserId,
                toUserId,
                LocalDateTime.now(),
                EventInvitationStatus.PENDING
        );
        invitations.put(invitation.getInvitationId(), invitation);
        return invitation;
    }

    @Override
    public List<EventInvitation> getPendingInvitationsForUser(UUID userId) {
        return invitations.values().stream()
                .filter(inv -> inv.getToUserId().equals(userId) && inv.getStatus() == EventInvitationStatus.PENDING)
                .sorted((a, b) -> b.getSentAt().compareTo(a.getSentAt()))
                .collect(Collectors.toList());
    }

    @Override
    public List<EventInvitation> getAllInvitationsForUser(UUID userId) {
        return invitations.values().stream()
                .filter(inv -> inv.getToUserId().equals(userId))
                .sorted((a, b) -> b.getSentAt().compareTo(a.getSentAt()))
                .collect(Collectors.toList());
    }

    @Override
    public List<EventInvitation> getInvitationsForEvent(UUID eventId) {
        return invitations.values().stream()
                .filter(inv -> inv.getEventId().equals(eventId))
                .sorted((a, b) -> b.getSentAt().compareTo(a.getSentAt()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EventInvitation> findById(UUID invitationId) {
        return Optional.ofNullable(invitations.get(invitationId));
    }

    @Override
    public void updateStatus(UUID invitationId, EventInvitationStatus status) {
        EventInvitation invitation = invitations.get(invitationId);
        if (invitation != null) {
            invitation.setStatus(status);
        }
    }

    @Override
    public Optional<EventInvitation> findInvitationBetweenEventAndUser(UUID eventId, UUID userId) {
        return invitations.values().stream()
                .filter(inv -> inv.getEventId().equals(eventId) && inv.getToUserId().equals(userId))
                .findFirst();
    }

    @Override
    public boolean existsPendingInvitation(UUID eventId, UUID userId) {
        return invitations.values().stream()
                .anyMatch(inv -> inv.getEventId().equals(eventId)
                        && inv.getToUserId().equals(userId)
                        && inv.getStatus() == EventInvitationStatus.PENDING);
    }

    @Override
    public void deleteInvitation(UUID invitationId) {
        invitations.remove(invitationId);
    }

    @Override
    public List<EventInvitation> getInvitationsSentByUser(UUID userId) {
        return invitations.values().stream()
                .filter(inv -> inv.getFromUserId().equals(userId))
                .sorted((a, b) -> b.getSentAt().compareTo(a.getSentAt()))
                .collect(Collectors.toList());
    }
}

