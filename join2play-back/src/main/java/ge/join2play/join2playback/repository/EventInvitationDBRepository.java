package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.EventInvitation;
import ge.join2play.join2playback.model.enums.EventInvitationStatus;
import ge.join2play.join2playback.repository.interfaces.EventInvitationRepository;
import ge.join2play.join2playback.repository.jpa_interfaces.EventInvitationJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("eventInvitationRepositoryJPA")
@ConditionalOnProperty(name = "app.repository.type", havingValue = "jpa")
public class EventInvitationDBRepository implements EventInvitationRepository {

    private final EventInvitationJPARepository eventInvitationJPARepository;

    @Autowired
    public EventInvitationDBRepository(EventInvitationJPARepository eventInvitationJPARepository) {
        this.eventInvitationJPARepository = eventInvitationJPARepository;
    }

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
        return eventInvitationJPARepository.save(invitation);
    }

    @Override
    public List<EventInvitation> getPendingInvitationsForUser(UUID userId) {
        return eventInvitationJPARepository.findByToUserIdAndStatusOrderBySentAtDesc(userId, EventInvitationStatus.PENDING);
    }

    @Override
    public List<EventInvitation> getAllInvitationsForUser(UUID userId) {
        return eventInvitationJPARepository.findByToUserIdOrderBySentAtDesc(userId);
    }

    @Override
    public List<EventInvitation> getInvitationsForEvent(UUID eventId) {
        return eventInvitationJPARepository.findByEventIdOrderBySentAtDesc(eventId);
    }

    @Override
    public Optional<EventInvitation> findById(UUID invitationId) {
        return eventInvitationJPARepository.findById(invitationId);
    }

    @Override
    public void updateStatus(UUID invitationId, EventInvitationStatus status) {
        Optional<EventInvitation> invitation = eventInvitationJPARepository.findById(invitationId);
        if (invitation.isPresent()) {
            EventInvitation inv = invitation.get();
            inv.setStatus(status);
            eventInvitationJPARepository.save(inv);
        }
    }

    @Override
    public Optional<EventInvitation> findInvitationBetweenEventAndUser(UUID eventId, UUID userId) {
        return eventInvitationJPARepository.findByEventIdAndToUserId(eventId, userId);
    }

    @Override
    public boolean existsPendingInvitation(UUID eventId, UUID userId) {
        return eventInvitationJPARepository.existsByEventIdAndToUserIdAndStatus(eventId, userId, EventInvitationStatus.PENDING);
    }

    @Override
    public void deleteInvitation(UUID invitationId) {
        eventInvitationJPARepository.deleteById(invitationId);
    }

    @Override
    public List<EventInvitation> getInvitationsSentByUser(UUID userId) {
        return eventInvitationJPARepository.findByFromUserIdOrderBySentAtDesc(userId);
    }
}
