package ge.join2play.join2playback.repository.inmemory;

import ge.join2play.join2playback.model.Event;
import ge.join2play.join2playback.model.enums.SportType;
import ge.join2play.join2playback.model.exceptions.EventAlreadyExistsException;
import ge.join2play.join2playback.model.exceptions.EventDoesNotExistException;
import ge.join2play.join2playback.repository.interfaces.EventRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "memory", matchIfMissing = true)
public class EventInMemoryRepository implements EventRepository {
    private final Map<UUID, Event> events = new ConcurrentHashMap<>();

    public EventInMemoryRepository() {
        UUID mockEventId = UUID.fromString("e5d1d580-a4cf-4677-8220-a50c5decccfa");
        UUID mockHostId = UUID.fromString("13fa5e4e-1d9e-4a2a-9a20-7385f24e9097"); // Kakha Salukvadze
        Instant mockEventTime = Instant.parse("2025-04-16T10:15:30.00Z");

        Event mockEvent = new Event(
                mockEventId,
                mockHostId,
                "kakha.salukvadze@example.com",
                "+995 555 123 456",
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
            throw new EventAlreadyExistsException("Cannot save: event with ID " + event.getId() + "already exists.");
        }
        events.put(event.getId(), event);
        return event;
    }

    @Override
    public Event update(Event event) {
        Event oldEvent = events.replace(event.getId(), event);
        if (oldEvent == null) {
            throw new EventDoesNotExistException("Cannot update: event with ID " + event.getId() + " does not exist.");
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
