package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.UserPermission;
import ge.join2play.join2playback.repository.interfaces.UserPermissionRepository;
import ge.join2play.join2playback.repository.jpa_interfaces.UserPermissionJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("userPermissionRepositoryJPA")
@ConditionalOnProperty(name = "app.repository.type", havingValue = "jpa")
public class UserPermissionDBRepository implements UserPermissionRepository {

    private final UserPermissionJPARepository userPermissionJPARepository;

    @Autowired
    public UserPermissionDBRepository(UserPermissionJPARepository userPermissionJPARepository) {
        this.userPermissionJPARepository = userPermissionJPARepository;
    }

    @Override
    public UserPermission save(UserPermission userPermission) {
        return userPermissionJPARepository.save(userPermission);
    }

    @Override
    public UserPermission getByUserId(UUID userId) {
        return userPermissionJPARepository.findById(userId).orElse(null);
    }

    @Override
    public UserPermission update(UserPermission userPermission) {
        if (userPermission.getUserId() != null && userPermissionJPARepository.existsById(userPermission.getUserId())) {
            return userPermissionJPARepository.save(userPermission);
        }
        return null;
    }

    @Override
    public void delete(UUID userId) {
        userPermissionJPARepository.deleteById(userId);
    }
}
