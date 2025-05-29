package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.enums.FriendRequestStatus;
import ge.join2play.join2playback.model.FriendRequest;
import ge.join2play.join2playback.model.FriendRequestNotification;
import ge.join2play.join2playback.model.FriendResponseNotification;
import ge.join2play.join2playback.model.dto.FriendRequestDTO;
import ge.join2play.join2playback.service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/friends")
public class FriendWebSocketController {

    private final FriendRequestService service;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public FriendWebSocketController(FriendRequestService service, SimpMessagingTemplate messagingTemplate) {
        this.service = service;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendFriendRequest(@RequestBody FriendRequestDTO dto) {
        FriendRequest request = service.sendRequest(dto.getFromUserId(), dto.getToUserId());

        FriendRequestNotification notification = new FriendRequestNotification();
        notification.setRequestId(request.getRequestId());
        notification.setFromUserId(dto.getFromUserId());
        notification.setMessage("You have a new friend request!");

        messagingTemplate.convertAndSend("/topic/friend-requests/" + dto.getToUserId(), notification);

        return ResponseEntity.ok("Friend request sent");
    }

    @PostMapping("/respond")
    public ResponseEntity<String> respondToRequest(
            @RequestParam UUID requestId,
            @RequestParam String status
    ) {
        FriendRequest request = service.respondToRequest(requestId, FriendRequestStatus.valueOf(status));

        FriendResponseNotification response = new FriendResponseNotification();
        response.setRequestId(requestId);
        response.setResponderId(request.getToUserId());
        response.setStatus(status);

        messagingTemplate.convertAndSend("/topic/friend-responses/" + request.getFromUserId(), response);

        return ResponseEntity.ok("Friend request " + status.toLowerCase());
    }
}