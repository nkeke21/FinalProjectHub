package ge.join2play.join2playback.service;

import ge.join2play.join2playback.model.Event;
import ge.join2play.join2playback.model.EventInvitation;
import ge.join2play.join2playback.model.User;
import ge.join2play.join2playback.model.enums.EventInvitationStatus;
import ge.join2play.join2playback.model.enums.SportType;
import ge.join2play.join2playback.model.exceptions.*;
import ge.join2play.join2playback.repository.interfaces.EventInvitationRepository;
import ge.join2play.join2playback.repository.interfaces.EventRepository;
import ge.join2play.join2playback.repository.interfaces.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventInvitationServiceTest {

    @Mock
    private EventInvitationRepository repository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EventInvitationService service;

    private UUID eventId;
    private UUID hostId;
    private UUID inviteeId;
    private Event event;
    private User host;
    private User invitee;
    private EventInvitation invitation;

    @BeforeEach
    void setUp() {
        eventId = UUID.randomUUID();
        hostId = UUID.randomUUID();
        inviteeId = UUID.randomUUID();

        event = new Event(eventId, hostId, "host@gmail.com",
                "host_phone", 18, 40, "Test Event", Instant.now().plusSeconds(86400),
                41.0, 44.0, "Test Location", 20, 5, SportType.FOOTBALL);

        host = new User(
                hostId,
                "Host User",
                "host@test.com",
                "1234567890",
                Timestamp.valueOf("2000-04-16 00:00:00").getTime(),
                "I love sport",
                "password123"
        );

        invitee = new User(
                inviteeId,
                "Invitee User",
                "invitee@test.com",
                "0987654321",
                Timestamp.valueOf("2001-05-20 00:00:00").getTime(),
                "I love music",
                "password456"
        );

        invitation = new EventInvitation(
                UUID.randomUUID(),
                eventId,
                hostId,
                inviteeId,
                LocalDateTime.now(),
                EventInvitationStatus.PENDING
        );
    }

    @Test
    void sendInvitation_Success() {
        when(eventRepository.getById(eventId)).thenReturn(event);
        when(userRepository.getById(hostId)).thenReturn(Optional.of(host));
        when(userRepository.getById(inviteeId)).thenReturn(Optional.of(invitee));
        when(repository.existsPendingInvitation(eventId, inviteeId)).thenReturn(false);
        when(repository.saveInvitation(eventId, hostId, inviteeId)).thenReturn(invitation);

        EventInvitation result = service.sendInvitation(eventId, hostId, inviteeId);

        assertNotNull(result);
        assertEquals(invitation.getInvitationId(), result.getInvitationId());
        verify(repository).saveInvitation(eventId, hostId, inviteeId);
    }

    @Test
    void sendInvitation_EventNotFound() {
        when(eventRepository.getById(eventId)).thenReturn(null);

        assertThrows(EventDoesNotExistException.class,
                () -> service.sendInvitation(eventId, hostId, inviteeId));
    }

    @Test
    void sendInvitation_SenderNotFound() {
        when(eventRepository.getById(eventId)).thenReturn(event);
        when(userRepository.getById(hostId)).thenReturn(Optional.empty());

        assertThrows(UserDoesNotExistException.class,
                () -> service.sendInvitation(eventId, hostId, inviteeId));
    }

    @Test
    void sendInvitation_RecipientNotFound() {
        when(eventRepository.getById(eventId)).thenReturn(event);
        when(userRepository.getById(hostId)).thenReturn(Optional.of(host));
        when(userRepository.getById(inviteeId)).thenReturn(Optional.empty());

        assertThrows(UserDoesNotExistException.class,
                () -> service.sendInvitation(eventId, hostId, inviteeId));
    }

    @Test
    void sendInvitation_NotEventCreator() {
        UUID nonHostId = UUID.randomUUID();
        when(eventRepository.getById(eventId)).thenReturn(event);
        when(userRepository.getById(nonHostId)).thenReturn(Optional.of(host));
        when(userRepository.getById(inviteeId)).thenReturn(Optional.of(invitee));

        assertThrows(UnauthorizedException.class,
                () -> service.sendInvitation(eventId, nonHostId, inviteeId));
    }

    @Test
    void sendInvitation_SelfInvitation() {
        assertThrows(IllegalArgumentException.class,
                () -> service.sendInvitation(eventId, hostId, hostId));
    }

    @Test
    void sendInvitation_DuplicateInvitation() {
        when(eventRepository.getById(eventId)).thenReturn(event);
        when(userRepository.getById(hostId)).thenReturn(Optional.of(host));
        when(userRepository.getById(inviteeId)).thenReturn(Optional.of(invitee));
        when(repository.existsPendingInvitation(eventId, inviteeId)).thenReturn(true);

        assertThrows(EventInvitationAlreadyExistsException.class,
                () -> service.sendInvitation(eventId, hostId, inviteeId));
    }

    @Test
    void respondToInvitation_Accept_Success() {
        when(repository.findById(invitation.getInvitationId())).thenReturn(Optional.of(invitation));

        EventInvitation result = service.respondToInvitation(
                invitation.getInvitationId(),
                EventInvitationStatus.ACCEPTED,
                inviteeId
        );

        assertEquals(EventInvitationStatus.ACCEPTED, result.getStatus());
        verify(repository).updateStatus(invitation.getInvitationId(), EventInvitationStatus.ACCEPTED);
    }

    @Test
    void respondToInvitation_Reject_Success() {
        when(repository.findById(invitation.getInvitationId())).thenReturn(Optional.of(invitation));

        EventInvitation result = service.respondToInvitation(
                invitation.getInvitationId(),
                EventInvitationStatus.REJECTED,
                inviteeId
        );

        assertEquals(EventInvitationStatus.REJECTED, result.getStatus());
        verify(repository).updateStatus(invitation.getInvitationId(), EventInvitationStatus.REJECTED);
    }

    @Test
    void respondToInvitation_InvitationNotFound() {
        when(repository.findById(invitation.getInvitationId())).thenReturn(Optional.empty());

        assertThrows(EventInvitationDoesNotExistException.class,
                () -> service.respondToInvitation(
                        invitation.getInvitationId(),
                        EventInvitationStatus.ACCEPTED,
                        inviteeId
                ));
    }

    @Test
    void respondToInvitation_UnauthorizedUser() {
        UUID unauthorizedUserId = UUID.randomUUID();
        when(repository.findById(invitation.getInvitationId())).thenReturn(Optional.of(invitation));

        assertThrows(UnauthorizedException.class,
                () -> service.respondToInvitation(
                        invitation.getInvitationId(),
                        EventInvitationStatus.ACCEPTED,
                        unauthorizedUserId
                ));
    }

    @Test
    void respondToInvitation_InvalidStatus() {
        lenient().when(repository.findById(invitation.getInvitationId())).thenReturn(Optional.of(invitation));

        assertThrows(IllegalArgumentException.class,
                () -> service.respondToInvitation(
                        invitation.getInvitationId(),
                        EventInvitationStatus.PENDING,
                        inviteeId
                ));
    }

    @Test
    void respondToInvitation_AlreadyProcessed() {
        invitation.setStatus(EventInvitationStatus.ACCEPTED);
        when(repository.findById(invitation.getInvitationId())).thenReturn(Optional.of(invitation));

        assertThrows(IllegalStateException.class,
                () -> service.respondToInvitation(
                        invitation.getInvitationId(),
                        EventInvitationStatus.REJECTED,
                        inviteeId
                ));
    }

    @Test
    void buildNotification_Success() {
        when(userRepository.getById(hostId)).thenReturn(Optional.of(host));
        when(eventRepository.getById(eventId)).thenReturn(event);

        var notification = service.buildNotification(invitation);

        assertNotNull(notification);
        assertEquals(invitation.getInvitationId().toString(), notification.getInvitationId());
        assertEquals(host.getName(), notification.getFromUserName());
        assertEquals(event.getDescription(), notification.getEventDescription());
        assertTrue(notification.getMessage().contains(host.getName()));
        assertTrue(notification.getMessage().contains(event.getDescription() != null ? event.getDescription() : "Sports Event"));
    }

    @Test
    void deleteInvitation_Success() {
        when(repository.findById(invitation.getInvitationId())).thenReturn(Optional.of(invitation));

        service.deleteInvitation(invitation.getInvitationId(), hostId);

        verify(repository).deleteInvitation(invitation.getInvitationId());
    }

    @Test
    void deleteInvitation_UnauthorizedUser() {
        UUID unauthorizedUserId = UUID.randomUUID();
        when(repository.findById(invitation.getInvitationId())).thenReturn(Optional.of(invitation));

        assertThrows(UnauthorizedException.class,
                () -> service.deleteInvitation(invitation.getInvitationId(), unauthorizedUserId));
    }

    @Test
    void getPendingInvitationsForUser_Success() {
        when(userRepository.getById(inviteeId)).thenReturn(Optional.of(invitee));
        when(repository.getPendingInvitationsForUser(inviteeId)).thenReturn(List.of(invitation));

        var result = service.getPendingInvitationsForUser(inviteeId);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(invitation.getInvitationId(), result.getFirst().getInvitationId());
    }
}
