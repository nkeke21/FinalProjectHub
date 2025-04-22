package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.Event;
import ge.join2play.join2playback.model.errors.EventDoesNotExistError;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class EventInMemoryRepository implements EventRepository {
    private final Map<UUID, Event> events = new HashMap<>();

    @Override
    public Event getById(UUID id) {
        return events.getOrDefault(id, null);
    }

    @Override
    public void save(Event event) {
        events.put(event.getId(), event);
    }

    @Override
    public void update(Event event) {
        Event oldEvent = events.replace(event.getId(), event);
        if (oldEvent == null) {
            throw new EventDoesNotExistError("Cannot update: event with ID " + event.getId() + " does not exist.");
        }
    }

    @Override
    public void delete(UUID id) {
        events.remove(id);
    }

    @Override
    public List<Event> getAll() {
        return List.of();
    }
}
