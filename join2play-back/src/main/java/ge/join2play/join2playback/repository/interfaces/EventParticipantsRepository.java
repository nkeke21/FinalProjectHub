package ge.join2play.join2playback.repository.interfaces;

import ge.join2play.join2playback.model.EventParticipant;

import java.util.List;
import java.util.UUID;

public interface EventParticipantsRepository {
    List<EventParticipant> getAllEventsParticipating(UUID userId);
    List<EventParticipant> getAllParticipantsOf(UUID eventId);
    List<EventParticipant> getAll();
    EventParticipant getById(UUID id);
    EventParticipant getByEventIdAndParticipantId(UUID eventId, UUID participantId);
    EventParticipant save(EventParticipant eventParticipant);
    EventParticipant update(EventParticipant eventParticipant);
    void delete(UUID id);
    void clear();
}
