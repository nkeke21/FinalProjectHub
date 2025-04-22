package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.Event;

import java.util.List;
import java.util.UUID;

public interface EventRepository {
    public Event getById(UUID id);
    public void save(Event event);
    public void update(Event event);
    public void delete(UUID id);
    public List<Event> getAll();
}
