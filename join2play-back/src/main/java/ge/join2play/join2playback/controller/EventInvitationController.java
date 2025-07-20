package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.EventInvitation;
import ge.join2play.join2playback.model.EventInvitationNotification;
import ge.join2play.join2playback.model.User;
import ge.join2play.join2playback.model.dto.EventInvitationDTO;
import ge.join2play.join2playback.model.dto.EventInvitationResponseDTO;
import ge.join2play.join2playback.model.enums.EventInvitationStatus;
import ge.join2play.join2playback.service.EventInvitationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/event-invitations")
@CrossOrigin(origins = "*")
@Validated
public class EventInvitationController {
    private static final Logger logger = LoggerFactory.getLogger(EventInvitationController.class);

    private final EventInvitationService service;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public EventInvitationController(EventInvitationService service, SimpMessagingTemplate messagingTemplate) {
        this.service = service;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendEventInvitation(@Valid @RequestBody EventInvitationDTO dto, HttpSession session) {
        User currentUser = getCurrentUser(session);
        if (currentUser == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Authentication required"));
        }

        EventInvitation invitation = service.sendInvitation(
                UUID.fromString(dto.getEventId()),
                currentUser.getId(),
                UUID.fromString(dto.getToUserId())
        );

        EventInvitationNotification notification = service.buildNotification(invitation);
        messagingTemplate.convertAndSend("/topic/event-invitations/" + dto.getToUserId(), notification);

        logger.info("Event invitation sent successfully by user {}", currentUser.getId());
        return ResponseEntity.ok(Map.of(
                "message", "Event invitation sent successfully",
                "invitationId", invitation.getInvitationId().toString()
        ));
    }

    @GetMapping("/user/pending")
    public ResponseEntity<List<EventInvitation>> getCurrentUserPendingInvitations(HttpSession session) {
        User currentUser = getCurrentUser(session);
        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }

        List<EventInvitation> invitations = service.getPendingInvitationsForUser(currentUser.getId());
        return ResponseEntity.ok(invitations);
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<EventInvitation>> getCurrentUserAllInvitations(HttpSession session) {
        User currentUser = getCurrentUser(session);
        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }

        List<EventInvitation> invitations = service.getAllInvitationsForUser(currentUser.getId());
        return ResponseEntity.ok(invitations);
    }

    @GetMapping("/user/sent")
    public ResponseEntity<List<EventInvitation>> getCurrentUserSentInvitations(HttpSession session) {
        User currentUser = getCurrentUser(session);
        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }

        List<EventInvitation> invitations = service.getInvitationsSentByUser(currentUser.getId());
        return ResponseEntity.ok(invitations);
    }

    @PostMapping("/respond")
    public ResponseEntity<EventInvitation> respondToInvitation(@Valid @RequestBody EventInvitationResponseDTO responseDTO, HttpSession session) {
        User currentUser = getCurrentUser(session);
        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }

        EventInvitation invitation = service.respondToInvitation(
                UUID.fromString(responseDTO.getInvitationId()),
                EventInvitationStatus.valueOf(responseDTO.getStatus()),
                currentUser.getId()
        );

        logger.info("User {} responded to invitation {} with status {}",
                currentUser.getId(), responseDTO.getInvitationId(), responseDTO.getStatus());

        return ResponseEntity.ok(invitation);
    }

    @GetMapping("/check-invitation/{eventId}/{userId}")
    public ResponseEntity<EventInvitation> checkInvitationBetweenEventAndUser(@PathVariable UUID eventId, @PathVariable UUID userId) {
        Optional<EventInvitation> invitation = service.findInvitationBetweenEventAndUser(eventId, userId);
        return invitation.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{invitationId}")
    public ResponseEntity<Map<String, String>> deleteInvitation(@PathVariable UUID invitationId, HttpSession session) {
        User currentUser = getCurrentUser(session);
        if (currentUser == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Authentication required"));
        }

        service.deleteInvitation(invitationId, currentUser.getId());
        return ResponseEntity.ok(Map.of("message", "Invitation deleted successfully"));
    }

    private User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }
}
