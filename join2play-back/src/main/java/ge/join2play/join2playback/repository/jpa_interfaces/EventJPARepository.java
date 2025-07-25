package ge.join2play.join2playback.repository.jpa_interfaces;

import ge.join2play.join2playback.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EventJPARepository extends JpaRepository<Event, UUID> {

    List<Event> findByHostId(UUID userId);
}
