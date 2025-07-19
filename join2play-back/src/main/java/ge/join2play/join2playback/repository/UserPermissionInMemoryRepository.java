package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.UserPermission;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class UserPermissionInMemoryRepository implements UserPermissionRepository {
    private final Map<UUID, UserPermission> userPermissions = new HashMap<>();

    @Override
    public UserPermission save(UserPermission userPermission) {
        userPermissions.put(userPermission.getUserId(), userPermission);
        return userPermission;
    }

    @Override
    public UserPermission getByUserId(UUID userId) {
        return userPermissions.get(userId);
    }

    @Override
    public UserPermission update(UserPermission userPermission) {
        if (userPermissions.containsKey(userPermission.getUserId())) {
            userPermissions.put(userPermission.getUserId(), userPermission);
            return userPermission;
        }
        return null;
    }

    @Override
    public void delete(UUID userId) {
        userPermissions.remove(userId);
    }
} 