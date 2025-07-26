package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.dto.TournamentRegistrationNotificationDTO;
import ge.join2play.join2playback.service.TournamentRegistrationNotificationService;
import ge.join2play.join2playback.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        try {
            return jwtUtil.getCurrentUserId()
                    .map(userId -> {
                        try {
                            List<TournamentRegistrationNotificationDTO> notifications = notificationService.getNotificationsByHostId(userId);
                            return ResponseEntity.ok(notifications);
                        } catch (Exception e) {
                            return ResponseEntity.<List<TournamentRegistrationNotificationDTO>>badRequest().build();
                        }
                    })
                    .orElse(ResponseEntity.<List<TournamentRegistrationNotificationDTO>>status(401).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/host/unread-count")
    public ResponseEntity<Integer> getUnreadCount() {
        try {
            return jwtUtil.getCurrentUserId()
                    .map(userId -> {
                        try {
                            Integer count = notificationService.getUnreadCountByHostId(userId);
                            return ResponseEntity.ok(count);
                        } catch (Exception e) {
                            return ResponseEntity.<Integer>badRequest().build();
                        }
                    })
                    .orElse(ResponseEntity.<Integer>status(401).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/mark-read")
    public ResponseEntity<Void> markAsRead(@PathVariable UUID id) {
        try {
            return jwtUtil.getCurrentUserId()
                    .map(userId -> {
                        try {
                            notificationService.markAsRead(id, userId);
                            return ResponseEntity.<Void>ok().build();
                        } catch (Exception e) {
                            return ResponseEntity.<Void>badRequest().build();
                        }
                    })
                    .orElse(ResponseEntity.<Void>status(401).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 