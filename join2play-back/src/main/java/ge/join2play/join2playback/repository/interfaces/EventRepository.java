package ge.join2play.join2playback.repository.interfaces;

import ge.join2play.join2playback.model.Event;

import java.util.List;
import java.util.UUID;

public interface EventRepository {
    Event getById(UUID id);
    Event save(Event event);
    Event update(Event event);
    void delete(UUID id);
    List<Event> getAll();
    List<Event> getAllHostedBy(UUID userId);
    void clear();
}
