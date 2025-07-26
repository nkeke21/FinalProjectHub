package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.EventInvitation;
import ge.join2play.join2playback.model.EventInvitationNotification;
import ge.join2play.join2playback.model.dto.EventInvitationDTO;
import ge.join2play.join2playback.model.dto.EventInvitationResponseDTO;
import ge.join2play.join2playback.model.enums.EventInvitationStatus;
import ge.join2play.join2playback.service.EventInvitationService;
import ge.join2play.join2playback.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    private final JwtUtil jwtUtil;

    @Autowired
    public EventInvitationController(EventInvitationService service, SimpMessagingTemplate messagingTemplate, JwtUtil jwtUtil) {
        this.service = service;
        this.messagingTemplate = messagingTemplate;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendEventInvitation(@Valid @RequestBody EventInvitationDTO dto) {
        try {
            return jwtUtil.getCurrentUserId()
                    .map(userId -> {
                        try {
                            EventInvitation invitation = service.sendInvitation(
                                    UUID.fromString(dto.getEventId()), 
                                    userId, 
                                    UUID.fromString(dto.getToUserId())
                            );
                            EventInvitationNotification notification = service.buildNotification(invitation);

                            logger.info("Sending event invitation notification to user: {}", dto.getToUserId());
                            logger.info("Notification message: {}", notification.getMessage());
                            messagingTemplate.convertAndSend("/topic/event-invitations/" + dto.getToUserId(), notification);

                            return ResponseEntity.ok(Map.of("message", "Event invitation sent successfully"));
                        } catch (Exception e) {
                            logger.error("Error sending event invitation", e);
                            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
                        }
                    })
                    .orElse(ResponseEntity.status(401).body(Map.of("error", "User not authenticated")));
        } catch (Exception e) {
            logger.error("Unexpected error in sendEventInvitation", e);
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to send invitation"));
        }
    }

    @GetMapping("/user/pending")
    public ResponseEntity<List<EventInvitation>> getCurrentUserPendingInvitations() {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            List<EventInvitation> invitations = service.getPendingInvitationsForUser(currentUserId.get());
            return ResponseEntity.ok(invitations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<EventInvitation>> getCurrentUserAllInvitations() {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            List<EventInvitation> invitations = service.getAllInvitationsForUser(currentUserId.get());
            return ResponseEntity.ok(invitations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/sent")
    public ResponseEntity<List<EventInvitation>> getCurrentUserSentInvitations() {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            List<EventInvitation> invitations = service.getInvitationsSentByUser(currentUserId.get());
            return ResponseEntity.ok(invitations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/respond")
    public ResponseEntity<EventInvitation> respondToInvitation(@Valid @RequestBody EventInvitationResponseDTO responseDTO) {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            EventInvitation invitation = service.respondToInvitation(
                    UUID.fromString(responseDTO.getInvitationId()),
                    EventInvitationStatus.valueOf(responseDTO.getStatus()),
                    currentUserId.get()
            );
            return ResponseEntity.ok(invitation);
        } catch (Exception e) {
            logger.error("Error responding to invitation", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{invitationId}")
    public ResponseEntity<Map<String, String>> deleteInvitation(@PathVariable UUID invitationId) {
        try {
            return jwtUtil.getCurrentUserId()
                    .map(userId -> {
                        try {
                            service.deleteInvitation(invitationId, userId);
                            return ResponseEntity.ok(Map.of("message", "Invitation deleted successfully"));
                        } catch (Exception e) {
                            logger.error("Error deleting invitation", e);
                            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
                        }
                    })
                    .orElse(ResponseEntity.status(401).body(Map.of("error", "User not authenticated")));
        } catch (Exception e) {
            logger.error("Unexpected error in deleteInvitation", e);
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to delete invitation"));
        }
    }

    @GetMapping("/check-invitation/{eventId}/{userId}")
    public ResponseEntity<EventInvitation> checkInvitationBetweenEventAndUser(@PathVariable UUID eventId, @PathVariable UUID userId) {
        Optional<EventInvitation> invitation = service.findInvitationBetweenEventAndUser(eventId, userId);
        return invitation.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
