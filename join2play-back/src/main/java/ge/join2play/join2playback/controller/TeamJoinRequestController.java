package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.TeamJoinRequest;
import ge.join2play.join2playback.model.TeamJoinRequestNotification;
import ge.join2play.join2playback.model.dto.TeamJoinRequestDTO;
import ge.join2play.join2playback.model.dto.TeamJoinRequestResponseDTO;
import ge.join2play.join2playback.model.enums.TeamJoinRequestStatus;
import ge.join2play.join2playback.service.TeamJoinRequestService;
import ge.join2play.join2playback.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/team-requests")
@CrossOrigin(origins = "*")
public class TeamJoinRequestController {
    private final TeamJoinRequestService service;
    private final SimpMessagingTemplate messagingTemplate;
    private final JwtUtil jwtUtil;

    @Autowired
    public TeamJoinRequestController(TeamJoinRequestService service, SimpMessagingTemplate messagingTemplate, JwtUtil jwtUtil) {
        this.service = service;
        this.messagingTemplate = messagingTemplate;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendTeamJoinRequest(@RequestBody TeamJoinRequestDTO dto) {
        return jwtUtil.getCurrentUserId()
                .map(userId -> {
                    try {
                        TeamJoinRequest request = service.sendJoinRequest(dto.getTeamId(), userId);
                        TeamJoinRequestNotification notification = service.buildNotification(request);

                        // Get team captain ID and send notification to captain
                        UUID captainId = service.getTeamCaptainId(dto.getTeamId());
                        System.out.println("Sending team join request notification to captain: " + captainId);
                        System.out.println("Notification message: " + notification.getMessage());
                        messagingTemplate.convertAndSend("/topic/team-requests/" + captainId, notification);

                        return ResponseEntity.ok("Team join request sent");
                    } catch (Exception e) {
                        return ResponseEntity.badRequest().body(e.getMessage());
                    }
                })
                .orElse(ResponseEntity.<String>status(401).build());
    }

    @GetMapping("/team/{teamId}/pending")
    public ResponseEntity<List<TeamJoinRequest>> getPendingRequestsForTeam(@PathVariable UUID teamId) {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            List<TeamJoinRequest> requests = service.getPendingRequestsForTeam(teamId);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/pending")
    public ResponseEntity<List<TeamJoinRequest>> getPendingRequestsForUser() {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            List<TeamJoinRequest> requests = service.getPendingRequestsForUser(currentUserId.get());
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/captain/pending")
    public ResponseEntity<List<TeamJoinRequest>> getPendingRequestsForTeamCaptain() {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            List<TeamJoinRequest> requests = service.getPendingRequestsForTeamCaptain(currentUserId.get());
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/team/{teamId}/all")
    public ResponseEntity<List<TeamJoinRequest>> getAllRequestsForTeam(@PathVariable UUID teamId) {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            List<TeamJoinRequest> requests = service.getAllRequestsForTeam(teamId);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<TeamJoinRequest>> getAllRequestsForUser() {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            List<TeamJoinRequest> requests = service.getAllRequestsForUser(currentUserId.get());
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/respond")
    public ResponseEntity<TeamJoinRequest> respondToRequest(@RequestBody TeamJoinRequestResponseDTO responseDTO) {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            TeamJoinRequest request = service.respondToRequest(
                    responseDTO.getRequestId(), 
                    TeamJoinRequestStatus.valueOf(responseDTO.getStatus()),
                    currentUserId.get()
            );
            return ResponseEntity.ok(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/check-request/{teamId}/{userId}")
    public ResponseEntity<TeamJoinRequest> checkRequestBetweenTeamAndUser(@PathVariable UUID teamId, @PathVariable UUID userId) {
        Optional<TeamJoinRequest> request = service.findRequestBetweenTeamAndUser(teamId, userId);
        return request.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/current-user")
    public ResponseEntity<String> getCurrentUserId() {
        return jwtUtil.getCurrentUserIdString() != null ? 
                ResponseEntity.ok(jwtUtil.getCurrentUserIdString()) :
                ResponseEntity.<String>status(401).build();
    }
} 