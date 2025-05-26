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
        UUID eventId = UUID.fromString("13fa5e4e-1d9e-4a2a-9a20-7385f24e9097");

        UUID hostId = UUID.fromString("13fa5e4e-1d9e-4a2a-9a20-7385f24e9097");
        int minAge = 18;
        int maxAge = 35;
        String description = "Evening Football Match at the Park";
        Instant eventTime = Instant.now().plusSeconds(3600 * 24);
        double latitude = 41.7151;
        double longitude = 44.8271;
        String location = "Tbilisi Central Park";
        int totalParticipants = 10;
        int registeredParticipants = 3;
        SportType sportType = SportType.FOOTBALL;

        Event event = new Event(
                eventId,
                hostId,
                minAge,
                maxAge,
                description,
                eventTime,
                latitude,
                longitude,
                location,
                totalParticipants,
                registeredParticipants,
                sportType
        );

        events.put(eventId, event);
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
