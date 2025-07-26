package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.dto.TournamentRegistrationNotificationDTO;
import ge.join2play.join2playback.service.TournamentRegistrationNotificationService;
import ge.join2play.join2playback.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/tournament-registration-notifications")
@CrossOrigin(origins = "*")
public class TournamentRegistrationNotificationController {

    private final TournamentRegistrationNotificationService notificationService;
    private final JwtUtil jwtUtil;

    @Autowired
    public TournamentRegistrationNotificationController(TournamentRegistrationNotificationService notificationService, JwtUtil jwtUtil) {
        this.notificationService = notificationService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/host")
    public ResponseEntity<List<TournamentRegistrationNotificationDTO>> getHostNotifications() {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            List<TournamentRegistrationNotificationDTO> notifications = notificationService.getNotificationsByHostId(currentUserId.get());
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/host/unread-count")
    public ResponseEntity<Integer> getUnreadCount() {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            Integer count = notificationService.getUnreadCountByHostId(currentUserId.get());
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/mark-read")
    public ResponseEntity<Void> markAsRead(@PathVariable UUID id) {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            notificationService.markAsRead(id, currentUserId.get());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 