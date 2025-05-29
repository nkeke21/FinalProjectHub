package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invite")
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping
    public void sendInvite(@RequestParam String targetUserId, @RequestBody NotificationDTO notification) {
        messagingTemplate.convertAndSend("/topic/user/" + targetUserId, notification);
    }
}
