package ge.join2play.join2playback.service;

import ge.join2play.join2playback.model.UserPermission;
import ge.join2play.join2playback.repository.interfaces.UserPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class UserPermissionService {
    private final UserPermissionRepository userPermissionRepository;

    @Autowired
    public UserPermissionService(UserPermissionRepository userPermissionRepository) {
        this.userPermissionRepository = userPermissionRepository;
    }

    public boolean canHostTournaments(UUID userId) {
        UserPermission permission = userPermissionRepository.getByUserId(userId);
        return permission != null && permission.isCanHostTournaments();
    }

    public UserPermission grantTournamentHostingPermission(UUID userId, String grantedBy, String reason) {
        UserPermission permission = userPermissionRepository.getByUserId(userId);
        
        if (permission == null) {
            // Create new permission record
            permission = new UserPermission();
            permission.setUserId(userId);
        }
        
        permission.setCanHostTournaments(true);
        permission.setGrantedBy(grantedBy);
        permission.setGrantedAt(Instant.now().toString());
        permission.setReason(reason);
        
        return userPermissionRepository.save(permission);
    }

    public UserPermission revokeTournamentHostingPermission(UUID userId, String revokedBy, String reason) {
        UserPermission permission = userPermissionRepository.getByUserId(userId);
        
        if (permission == null) {
            throw new RuntimeException("User permission not found");
        }
        
        permission.setCanHostTournaments(false);
        permission.setGrantedBy(revokedBy);
        permission.setGrantedAt(Instant.now().toString());
        permission.setReason(reason);
        
        return userPermissionRepository.update(permission);
    }

    public UserPermission getUserPermission(UUID userId) {
        return userPermissionRepository.getByUserId(userId);
    }

    public UserPermission createDefaultPermission(UUID userId) {
        // For testing purposes, set default permission to true
        // In production, this should be false by default
        UserPermission permission = new UserPermission(
            userId,
            true, // Set to true for testing, should be false in production
            "System",
            Instant.now().toString(),
            "Default permission for testing"
        );
        
        return userPermissionRepository.save(permission);
    }

    // Method to initialize permissions for existing users (for testing)
    public void initializePermissionsForExistingUsers() {
        // This method can be called during application startup to ensure
        // existing users have permissions set up
        // In a real application, this would be handled by database migrations
    }
} 