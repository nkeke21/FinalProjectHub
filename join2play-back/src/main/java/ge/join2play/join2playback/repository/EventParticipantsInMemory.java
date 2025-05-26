package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.EventParticipant;
import ge.join2play.join2playback.model.errors.EventParticipantAlreadyExistsError;
import ge.join2play.join2playback.model.errors.EventParticipantDoesNotExistError;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class EventParticipantsInMemory implements EventParticipantsRepository {
    private final Map<UUID, EventParticipant> eventsParticipants = new HashMap<>();

    public EventParticipantsInMemory() {
        // Add a sample participant for testing
        UUID id = UUID.fromString("13fa5e4e-1d9e-4a2a-9a20-7385f24e9097");
        UUID eventId =UUID.fromString("13fa5e4e-1d9e-4a2a-9a20-7385f24e9097");       // Could be hardcoded if needed
        UUID participantId = UUID.fromString("13fa5e4e-1d9e-4a2a-9a20-7385f24e9097"); // Example participant ID

        EventParticipant participant = new EventParticipant(eventId, id, participantId);
        eventsParticipants.put(id, participant);
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
            throw new EventParticipantAlreadyExistsError("Cannot save: event-participant pair with ID " + eventParticipant.getId() + "already exists.");
        }
        eventsParticipants.put(eventParticipant.getId(), eventParticipant);
        return eventParticipant;
    }

    @Override
    public EventParticipant update(EventParticipant eventParticipant) {
        EventParticipant oldEventParticipant = eventsParticipants.replace(eventParticipant.getId(), eventParticipant);
        if (oldEventParticipant == null) {
            throw new EventParticipantDoesNotExistError("Cannot update: event-participant pair with ID " + eventParticipant.getId() + " does not exist.");
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
