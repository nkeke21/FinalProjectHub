package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.Event;
import ge.join2play.join2playback.model.SportType;
import ge.join2play.join2playback.model.errors.EventAlreadyExistsError;
import ge.join2play.join2playback.model.errors.EventDoesNotExistError;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;

@Repository
public class EventInMemoryRepository implements EventRepository {
    private final Map<UUID, Event> events = new HashMap<>();

    public EventInMemoryRepository() {
        UUID mockEventId = UUID.fromString("e5d1d580-a4cf-4677-8220-a50c5decccfa");
        UUID mockHostId = UUID.fromString("13fa5e4e-1d9e-4a2a-9a20-7385f24e9097"); // Kakha Salukvadze
        Instant mockEventTime = Instant.parse("2025-04-16T10:15:30.00Z");
        
        Event mockEvent = new Event(
                mockEventId,
                mockHostId,
                18,
                35,
                "Weekend Football Match - Join us for a friendly game!",
                mockEventTime,
                41.725788,
                44.727753,
                "Vake Park, Tbilisi",
                10,
                3,
                SportType.FOOTBALL
        );
        
        events.put(mockEventId, mockEvent);
    }
    @Override
    public Event getById(UUID id) {
        return events.getOrDefault(id, null);
    }

    @Override
    public Event save(Event event) {
        if (getById(event.getId()) != null) {
            throw new EventAlreadyExistsError("Cannot save: event with ID " + event.getId() + "already exists.");
        }
        events.put(event.getId(), event);
        return event;
    }

    @Override
    public Event update(Event event) {
        Event oldEvent = events.replace(event.getId(), event);
        if (oldEvent == null) {
            throw new EventDoesNotExistError("Cannot update: event with ID " + event.getId() + " does not exist.");
        }
        return event;
    }

    @Override
    public void delete(UUID id) {
        events.remove(id);
    }

    @Override
    public List<Event> getAll() {
        return new ArrayList<>(events.values());
    }

    @Override
    public List<Event> getAllHostedBy(UUID userId) {
        return getAll().stream().filter(event -> event.getHostId().equals(userId)).toList();
    }

    @Override
    public void clear() {
        events.clear();
    }
}
