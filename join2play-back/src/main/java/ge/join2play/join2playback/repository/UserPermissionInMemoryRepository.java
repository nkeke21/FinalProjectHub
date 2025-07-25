package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.UserPermission;
import ge.join2play.join2playback.repository.interfaces.UserPermissionRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "memory", matchIfMissing = true)
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
