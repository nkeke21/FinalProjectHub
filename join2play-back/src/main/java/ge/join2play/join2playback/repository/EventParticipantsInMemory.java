package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.EventParticipant;
import ge.join2play.join2playback.model.exceptions.EventParticipantAlreadyExistsException;
import ge.join2play.join2playback.model.exceptions.EventParticipantDoesNotExistException;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class EventParticipantsInMemory implements EventParticipantsRepository {
    private final Map<UUID, EventParticipant> eventsParticipants = new HashMap<>();

    public EventParticipantsInMemory() {
        UUID footballEventId = UUID.fromString("e5d1d580-a4cf-4677-8220-a50c5decccfa"); // Our football event
        
        UUID participant1Id = UUID.fromString("24fb6e3f-2dac-4eac-8df1-abc456789012");
        EventParticipant participant1 = new EventParticipant(footballEventId, UUID.randomUUID(), participant1Id);
        eventsParticipants.put(participant1.getId(), participant1);
        
        UUID participant2Id = UUID.fromString("35ac7d4e-3fac-5fab-9ed2-cba987654321");
        EventParticipant participant2 = new EventParticipant(footballEventId, UUID.randomUUID(), participant2Id);
        eventsParticipants.put(participant2.getId(), participant2);
        
        UUID participant3Id = UUID.fromString("46bd8e5f-4abc-6a1c-8ef3-dcba87654321");
        EventParticipant participant3 = new EventParticipant(footballEventId, UUID.randomUUID(), participant3Id);
        eventsParticipants.put(participant3.getId(), participant3);
    }

    @Override
    public List<EventParticipant> getAllEventsParticipating(UUID userId) {
        return getAll().stream().filter(eventParticipant -> eventParticipant.getParticipantId().equals(userId)).toList();
    }

    @Override
    public List<EventParticipant> getAllParticipantsOf(UUID eventId) {
        return getAll().stream().filter(eventParticipant -> eventParticipant.getEventId().equals(eventId)).toList();
    }

    @Override
    public List<EventParticipant> getAll() {
        return new ArrayList<>(eventsParticipants.values());
    }

    @Override
    public EventParticipant getById(UUID id) {
        return eventsParticipants.getOrDefault(id, null);
    }

    @Override
    public EventParticipant save(EventParticipant eventParticipant) {
        if (getById(eventParticipant.getId()) != null) {
            throw new EventParticipantAlreadyExistsException("Cannot save: event-participant pair with ID " + eventParticipant.getId() + "already exists.");
        }
        eventsParticipants.put(eventParticipant.getId(), eventParticipant);
        return eventParticipant;
    }

    @Override
    public EventParticipant update(EventParticipant eventParticipant) {
        EventParticipant oldEventParticipant = eventsParticipants.replace(eventParticipant.getId(), eventParticipant);
        if (oldEventParticipant == null) {
            throw new EventParticipantDoesNotExistException("Cannot update: event-participant pair with ID " + eventParticipant.getId() + " does not exist.");
        }
        return eventParticipant;
    }

    @Override
    public void delete(UUID id) {
        eventsParticipants.remove(id);
    }

    @Override
    public void clear() {
        eventsParticipants.clear();
    }
}
