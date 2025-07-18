package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.enums.FriendRequestStatus;
import ge.join2play.join2playback.model.FriendRequest;
import ge.join2play.join2playback.model.FriendRequestNotification;
import ge.join2play.join2playback.model.FriendResponseNotification;
import ge.join2play.join2playback.model.dto.FriendRequestDTO;
import ge.join2play.join2playback.service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/{id}/requests")
    public ResponseEntity<List<FriendRequest>> getFriendRequests(@PathVariable UUID id) {
        List<FriendRequest> requests = service.getPendingRequests(id);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}/")
    public ResponseEntity<List<UUID>> getFriends(@PathVariable UUID id) {
        List<UUID> requests = service.getFriends(id);
        return ResponseEntity.ok(requests);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<String> deleteFriend(@PathVariable UUID userId, @PathVariable UUID friendId) {
        service.deleteFriend(userId, friendId);
        return ResponseEntity.ok("Friend deleted successfully");
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendFriendRequest(@RequestBody FriendRequestDTO dto) {
        FriendRequest request = service.saveRequest(dto.getFromUserId(), dto.getToUserId());

        FriendRequestNotification notification = service.buildNotification(request);

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