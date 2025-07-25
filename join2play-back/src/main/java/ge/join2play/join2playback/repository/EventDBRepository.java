package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.Event;
import ge.join2play.join2playback.model.exceptions.EventAlreadyExistsException;
import ge.join2play.join2playback.model.exceptions.EventDoesNotExistException;
import ge.join2play.join2playback.repository.interfaces.EventRepository;
import ge.join2play.join2playback.repository.jpa_interfaces.EventJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("eventRepositoryJPA")
@ConditionalOnProperty(name = "app.repository.type", havingValue = "jpa")
public class EventDBRepository implements EventRepository {
    private final EventJPARepository eventJPARepository;

    @Autowired
    public EventDBRepository(EventJPARepository eventJPARepository) {
        this.eventJPARepository = eventJPARepository;
    }

    @Override
    public Event getById(UUID id) {
        return eventJPARepository.findById(id).orElse(null);
    }

    @Override
    public Event save(Event event) {
        if (event.getId() != null && eventJPARepository.existsById(event.getId())) {
            throw new EventAlreadyExistsException("Cannot save: event with ID " + event.getId() + " already exists.");
        }

        if (event.getId() == null) {
            event.setId(UUID.randomUUID());
        }

        return eventJPARepository.save(event);
    }

    @Override
    public Event update(Event event) {
        if (event.getId() == null || !eventJPARepository.existsById(event.getId())) {
            throw new EventDoesNotExistException("Cannot update: event with ID " + event.getId() + " does not exist.");
        }

        return eventJPARepository.save(event);
    }

    @Override
    public void delete(UUID id) {
        eventJPARepository.deleteById(id);
    }

    @Override
    public List<Event> getAll() {
        return eventJPARepository.findAll();
    }

    @Override
    public List<Event> getAllHostedBy(UUID userId) {
        return eventJPARepository.findByHostId(userId);
    }

    @Override
    public void clear() {
        eventJPARepository.deleteAll();
    }
}