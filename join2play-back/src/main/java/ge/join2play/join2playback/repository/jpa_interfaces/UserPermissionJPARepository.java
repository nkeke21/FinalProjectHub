package ge.join2play.join2playback.repository.jpa_interfaces;

import ge.join2play.join2playback.model.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserPermissionJPARepository extends JpaRepository<UserPermission, UUID> {
}
