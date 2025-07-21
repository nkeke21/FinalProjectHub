package ge.join2play.join2playback.service;

import ge.join2play.join2playback.model.Event;
import ge.join2play.join2playback.model.EventInvitation;
import ge.join2play.join2playback.model.EventInvitationNotification;
import ge.join2play.join2playback.model.User;
import ge.join2play.join2playback.model.enums.EventInvitationStatus;
import ge.join2play.join2playback.model.exceptions.*;
import ge.join2play.join2playback.repository.EventInvitationRepository;
import ge.join2play.join2playback.repository.EventRepository;
import ge.join2play.join2playback.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventInvitationService {
    private static final Logger logger = LoggerFactory.getLogger(EventInvitationService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault());

    private final EventInvitationRepository repository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public EventInvitationService(EventInvitationRepository repository,
                                  EventRepository eventRepository,
                                  UserRepository userRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public EventInvitation sendInvitation(UUID eventId, UUID fromUserId, UUID toUserId) {
        logger.info("Sending invitation for event {} from user {} to user {}", eventId, fromUserId, toUserId);

        if (fromUserId.equals(toUserId)) {
            throw new IllegalArgumentException("Cannot invite yourself to an event");
        }

        Event event = eventRepository.getById(eventId);
        if (event == null) {
            throw new EventDoesNotExistException("Event with id " + eventId + " does not exist");
        }

        Optional<User> sender = userRepository.getById(fromUserId);
        if (sender.isEmpty()) {
            throw new UserDoesNotExistException("Sender with id " + fromUserId + " does not exist");
        }

        Optional<User> recipient = userRepository.getById(toUserId);
        if (recipient.isEmpty()) {
            throw new UserDoesNotExistException("Recipient with id " + toUserId + " does not exist");
        }

        int recipientAge = calculateAge(recipient.get().getBirthDate());
        if (recipientAge < event.getMinAge() || recipientAge > event.getMaxAge()) {
            throw new UserAgeNotInRangeException(
                String.format("User age %d is outside the event's age range (%d-%d).", recipientAge, event.getMinAge(), event.getMaxAge())
            );
        }

        if (!event.getHostId().equals(fromUserId)) {
            throw new UnauthorizedException("Only event creator can send invitations");
        }

        if (repository.existsPendingInvitation(eventId, toUserId)) {
            throw new EventInvitationAlreadyExistsException("A pending invitation already exists for this event and user");
        }

        EventInvitation invitation = repository.saveInvitation(eventId, fromUserId, toUserId);
        logger.info("Invitation {} sent successfully", invitation.getInvitationId());

        return invitation;
    }

    public List<EventInvitation> getPendingInvitationsForUser(UUID userId) {
        validateUserExists(userId);
        return repository.getPendingInvitationsForUser(userId);
    }

    public List<EventInvitation> getAllInvitationsForUser(UUID userId) {
        validateUserExists(userId);
        return repository.getAllInvitationsForUser(userId);
    }

    public List<EventInvitation> getInvitationsSentByUser(UUID userId) {
        validateUserExists(userId);
        return repository.getInvitationsSentByUser(userId);
    }

    public EventInvitation respondToInvitation(UUID invitationId, EventInvitationStatus status, UUID userId) {
        logger.info("User {} responding to invitation {} with status {}", userId, invitationId, status);

        if (status == EventInvitationStatus.PENDING) {
            throw new IllegalArgumentException("Cannot set invitation status to PENDING");
        }

        Optional<EventInvitation> optional = repository.findById(invitationId);
        if (optional.isEmpty()) {
            throw new EventInvitationDoesNotExistException("Event invitation not found for id: " + invitationId);
        }

        EventInvitation invitation = optional.get();

        if (!invitation.getToUserId().equals(userId)) {
            throw new UnauthorizedException("Only the invited user can respond to the invitation");
        }

        if (invitation.getStatus() != EventInvitationStatus.PENDING) {
            throw new IllegalStateException("Cannot respond to an invitation that has already been processed");
        }

        repository.updateStatus(invitationId, status);
        invitation.setStatus(status);

        logger.info("Invitation {} status updated to {}", invitationId, status);
        return invitation;
    }

    public EventInvitationNotification buildNotification(EventInvitation invitation) {
        Optional<User> sender = userRepository.getById(invitation.getFromUserId());
        if (sender.isEmpty()) {
            throw new UserDoesNotExistException("Sender not found with id: " + invitation.getFromUserId());
        }

        Event event = eventRepository.getById(invitation.getEventId());
        if (event == null) {
            throw new EventDoesNotExistException("Event not found with id: " + invitation.getEventId());
        }

        String eventDescription = event.getDescription();
        if (eventDescription == null || eventDescription.trim().isEmpty()) {
            eventDescription = "Sports Event";
        }

        String formattedMessage = String.format(
                "%s invited you to join '%s' at %s",
                sender.get().getName(),
                eventDescription,
                event.getLocation()
        );

        return new EventInvitationNotification(
                invitation.getInvitationId().toString(),
                invitation.getEventId().toString(),
                invitation.getFromUserId().toString(),
                sender.get().getName(),
                eventDescription,
                DATE_FORMATTER.format(event.getEventTime()),
                event.getLocation(),
                formattedMessage
        );
    }

    public Optional<EventInvitation> findInvitationBetweenEventAndUser(UUID eventId, UUID userId) {
        Event event = eventRepository.getById(eventId);
        if (event == null) {
            throw new EventDoesNotExistException("Event with id " + eventId + " does not exist");
        }

        validateUserExists(userId);
        return repository.findInvitationBetweenEventAndUser(eventId, userId);
    }

    public void deleteInvitation(UUID invitationId, UUID userId) {
        logger.info("User {} attempting to delete invitation {}", userId, invitationId);

        Optional<EventInvitation> optional = repository.findById(invitationId);
        if (optional.isEmpty()) {
            throw new EventInvitationDoesNotExistException("Event invitation not found for id: " + invitationId);
        }

        EventInvitation invitation = optional.get();
        if (!invitation.getFromUserId().equals(userId) && !invitation.getToUserId().equals(userId)) {
            throw new UnauthorizedException("Only invitation sender or recipient can delete the invitation");
        }

        repository.deleteInvitation(invitationId);
        logger.info("Invitation {} deleted successfully", invitationId);
    }

    private void validateUserExists(UUID userId) {
        Optional<User> user = userRepository.getById(userId);
        if (user.isEmpty()) {
            throw new UserDoesNotExistException("User with id " + userId + " does not exist");
        }
    }

    private int calculateAge(Long birthDateMillis) {
        if (birthDateMillis == null) {
            return 0;
        }
        java.time.LocalDate birthDate = java.time.Instant.ofEpochMilli(birthDateMillis)
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
        java.time.LocalDate currentDate = java.time.LocalDate.now();
        return currentDate.getYear() - birthDate.getYear() -
                (currentDate.getDayOfYear() < birthDate.getDayOfYear() ? 1 : 0);
    }
}
