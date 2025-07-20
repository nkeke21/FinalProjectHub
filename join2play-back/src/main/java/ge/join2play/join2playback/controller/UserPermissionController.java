package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.dto.PermissionRequest;
import ge.join2play.join2playback.model.dto.PermissionResponse;
import ge.join2play.join2playback.service.UserPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/permissions")
@CrossOrigin(origins = "*")
public class UserPermissionController {
    private final UserPermissionService userPermissionService;

    @Autowired
    public UserPermissionController(UserPermissionService userPermissionService) {
        this.userPermissionService = userPermissionService;
    }

    @GetMapping("/{userId}/can-host-tournaments")
    public ResponseEntity<Boolean> canHostTournaments(@PathVariable UUID userId) {
        try {
            boolean canHost = userPermissionService.canHostTournaments(userId);
            return ResponseEntity.ok(canHost);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<PermissionResponse> getUserPermission(@PathVariable UUID userId) {
        try {
            var permission = userPermissionService.getUserPermission(userId);
            if (permission == null) {
                return ResponseEntity.notFound().build();
            }
            
            PermissionResponse response = new PermissionResponse(
                permission.getUserId(),
                permission.isCanHostTournaments(),
                permission.getGrantedBy(),
                permission.getGrantedAt(),
                permission.getReason()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{userId}/grant")
    public ResponseEntity<PermissionResponse> grantPermission(
            @PathVariable UUID userId,
            @RequestBody PermissionRequest request) {
        try {
            var permission = userPermissionService.grantTournamentHostingPermission(
                userId, 
                request.getGrantedBy(), 
                request.getReason()
            );
            
            PermissionResponse response = new PermissionResponse(
                permission.getUserId(),
                permission.isCanHostTournaments(),
                permission.getGrantedBy(),
                permission.getGrantedAt(),
                permission.getReason()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{userId}/revoke")
    public ResponseEntity<PermissionResponse> revokePermission(
            @PathVariable UUID userId,
            @RequestBody PermissionRequest request) {
        try {
            var permission = userPermissionService.revokeTournamentHostingPermission(
                userId, 
                request.getGrantedBy(), 
                request.getReason()
            );
            
            PermissionResponse response = new PermissionResponse(
                permission.getUserId(),
                permission.isCanHostTournaments(),
                permission.getGrantedBy(),
                permission.getGrantedAt(),
                permission.getReason()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 