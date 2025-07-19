package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.UserPermission;

import java.util.UUID;

public interface UserPermissionRepository {
    UserPermission save(UserPermission userPermission);
    UserPermission getByUserId(UUID userId);
    UserPermission update(UserPermission userPermission);
    void delete(UUID userId);
} 