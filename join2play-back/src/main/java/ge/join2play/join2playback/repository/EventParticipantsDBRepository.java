package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.EventParticipant;
import ge.join2play.join2playback.model.exceptions.EventParticipantAlreadyExistsException;
import ge.join2play.join2playback.model.exceptions.EventParticipantDoesNotExistException;
import ge.join2play.join2playback.repository.interfaces.EventParticipantsRepository;
import ge.join2play.join2playback.repository.jpa_interfaces.EventParticipantsJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("eventParticipantsRepositoryJPA")
@ConditionalOnProperty(name = "app.repository.type", havingValue = "jpa")
public class EventParticipantsDBRepository implements EventParticipantsRepository {

    private final EventParticipantsJPARepository eventParticipantsJPARepository;

    @Autowired
    public EventParticipantsDBRepository(EventParticipantsJPARepository eventParticipantsJPARepository) {
        this.eventParticipantsJPARepository = eventParticipantsJPARepository;
    }

    @Override
    public List<EventParticipant> getAllEventsParticipating(UUID userId) {
        return eventParticipantsJPARepository.findByParticipantId(userId);
    }

    @Override
    public List<EventParticipant> getAllParticipantsOf(UUID eventId) {
        return eventParticipantsJPARepository.findByEventId(eventId);
    }

    @Override
    public List<EventParticipant> getAll() {
        return eventParticipantsJPARepository.findAll();
    }

    @Override
    public EventParticipant getById(UUID id) {
        return eventParticipantsJPARepository.findById(id).orElse(null);
    }

    @Override
    public EventParticipant save(EventParticipant eventParticipant) {
        if (eventParticipant.getId() != null && eventParticipantsJPARepository.existsById(eventParticipant.getId())) {
            throw new EventParticipantAlreadyExistsException("Cannot save: event-participant pair with ID " + eventParticipant.getId() + " already exists.");
        }

        if (eventParticipant.getId() == null) {
            eventParticipant.setId(UUID.randomUUID());
        }

        return eventParticipantsJPARepository.save(eventParticipant);
    }

    @Override
    public EventParticipant update(EventParticipant eventParticipant) {
        if (eventParticipant.getId() == null || !eventParticipantsJPARepository.existsById(eventParticipant.getId())) {
            throw new EventParticipantDoesNotExistException("Cannot update: event-participant pair with ID " + eventParticipant.getId() + " does not exist.");
        }

        return eventParticipantsJPARepository.save(eventParticipant);
    }

    @Override
    public void delete(UUID id) {
        eventParticipantsJPARepository.deleteById(id);
    }

    @Override
    public void clear() {
        eventParticipantsJPARepository.deleteAll();
    }
}
