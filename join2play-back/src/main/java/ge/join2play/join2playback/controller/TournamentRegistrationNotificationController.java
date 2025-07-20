package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.User;
import ge.join2play.join2playback.model.dto.TournamentRegistrationNotificationDTO;
import ge.join2play.join2playback.service.TournamentRegistrationNotificationService;
import jakarta.servlet.http.HttpSession;
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

    @Autowired
    public TournamentRegistrationNotificationController(TournamentRegistrationNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/host")
    public ResponseEntity<List<TournamentRegistrationNotificationDTO>> getHostNotifications(HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(401).build(); // Unauthorized
            }
            
            List<TournamentRegistrationNotificationDTO> notifications = notificationService.getNotificationsByHostId(currentUser.getId());
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/host/unread-count")
    public ResponseEntity<Integer> getUnreadCount(HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(401).build(); // Unauthorized
            }
            
            int count = notificationService.getUnreadCountByHostId(currentUser.getId());
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/mark-read")
    public ResponseEntity<Void> markAsRead(@PathVariable UUID id, HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(401).build(); // Unauthorized
            }
            
            notificationService.markAsRead(id, currentUser.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 