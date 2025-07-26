package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.enums.FriendRequestStatus;
import ge.join2play.join2playback.model.FriendRequest;
import ge.join2play.join2playback.model.FriendRequestNotification;
import ge.join2play.join2playback.model.dto.FriendRequestDTO;
import ge.join2play.join2playback.service.FriendRequestService;
import ge.join2play.join2playback.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@RestController
@RequestMapping("/api/friends")
@CrossOrigin(origins = "*")
public class FriendRequestController {

    private final FriendRequestService service;
    private final SimpMessagingTemplate messagingTemplate;
    private final JwtUtil jwtUtil;

    @Autowired
    public FriendRequestController(FriendRequestService service, SimpMessagingTemplate messagingTemplate, JwtUtil jwtUtil) {
        this.service = service;
        this.messagingTemplate = messagingTemplate;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/{id}/requests")
    public ResponseEntity<List<FriendRequest>> getFriendRequests(@PathVariable UUID id) {
        List<FriendRequest> requests = service.getPendingRequests(id);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<FriendRequest>> getCurrentUserFriendRequests() {
        return jwtUtil.getCurrentUserId()
                .map(userId -> ResponseEntity.ok(service.getPendingRequests(userId)))
                .orElse(ResponseEntity.status(401).build());
    }

    @GetMapping("/{id}/")
    public ResponseEntity<List<UUID>> getFriends(@PathVariable UUID id) {
        List<UUID> requests = service.getFriends(id);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/")
    public ResponseEntity<List<UUID>> getCurrentUserFriends() {
        return jwtUtil.getCurrentUserId()
                .map(userId -> ResponseEntity.ok(service.getFriends(userId)))
                .orElse(ResponseEntity.status(401).build());
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
    public ResponseEntity<FriendRequest> respondToRequest(@RequestBody FriendRequestResponseDTO responseDTO) {
        FriendRequest request = service.respondToRequest(responseDTO.getRequestId(), FriendRequestStatus.valueOf(responseDTO.getStatus()));

        return ResponseEntity.ok(request);
    }

    @GetMapping("/check-request/{user1Id}/{user2Id}")
    public ResponseEntity<FriendRequest> checkRequestBetweenUsers(@PathVariable UUID user1Id, @PathVariable UUID user2Id) {
        Optional<FriendRequest> request = service.findRequestBetweenUsers(user1Id, user2Id);
        return request.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    public static class FriendRequestResponseDTO {
        private UUID requestId;
        private String status;

        public UUID getRequestId() {
            return requestId;
        }

        public void setRequestId(UUID requestId) {
            this.requestId = requestId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}