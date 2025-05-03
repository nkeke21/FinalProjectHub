package ge.join2play.join2playback.service;

import ge.join2play.join2playback.model.Event;
import ge.join2play.join2playback.model.EventRequest;
import ge.join2play.join2playback.model.EventResponse;
import ge.join2play.join2playback.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event convertEventRequestToEvent(EventRequest eventRequest) {
        String ageRange = eventRequest.getAgeRange();
        String[] ages = ageRange.split("-");
        int minAge = Integer.parseInt(ages[0]);
        int maxAge = Integer.parseInt(ages[1]);
        return new Event(
                UUID.randomUUID(),minAge, maxAge, eventRequest.getDescription(), Instant.ofEpochMilli(eventRequest.getEventTime()),
                eventRequest.getLatitude(), eventRequest.getLongitude(), eventRequest.getNumberOfParticipants(),
                eventRequest.getSportType()
        );
    }

    public EventResponse convertEventToEventResponse(Event event) {
        String ageRange = event.getMinAge() + "-" + event.getMaxAge();
        return new EventResponse(event.getId(), ageRange, event.getDescription(), event.getEventTime().toEpochMilli(),
                event.getLatitude(), event.getLongitude(), event.getNumberOfParticipants(), event.getSportType());
    }

    public EventResponse createEvent(EventRequest eventRequest) {
        return convertEventToEventResponse(eventRepository.save(convertEventRequestToEvent(eventRequest)));
    }

    public EventResponse getEvent(UUID id) {
        return convertEventToEventResponse(eventRepository.getById(id));
    }

    public EventResponse updateEvent(EventRequest eventRequest, UUID id) {
        return convertEventToEventResponse(eventRepository.update(convertEventRequestToEvent(eventRequest)));
    }

    public List<EventResponse> getAllEvents() {
        return eventRepository.getAll().stream().map(this::convertEventToEventResponse).collect(Collectors.toList());
    }
}
