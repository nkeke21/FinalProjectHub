package ge.join2play.join2playback.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.join2play.join2playback.model.Event;
import ge.join2play.join2playback.model.EventRequest;
import ge.join2play.join2playback.model.EventResponse;
import ge.join2play.join2playback.repository.EventInMemoryRepository;
import ge.join2play.join2playback.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventInMemoryRepository eventRepository;

    @Autowired
    private EventService eventService;

    @BeforeEach
    void setUp() {
        eventRepository.clear();
    }

    @Test
    public void testCreateEvent() {
        EventRequest request = new EventRequest(
                "18-25",
                "Football Match",
                Instant.parse("2025-04-16T10:15:30.00Z").toEpochMilli(),
                41.725788,
                44.727753,
                10,
                "Football"
        );

        ResponseEntity<EventResponse> response = restTemplate.postForEntity(createURL("/events"), request, EventResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        EventResponse responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getAgeRange()).isEqualTo(request.getAgeRange());
        assertThat(responseBody.getDescription()).isEqualTo(request.getDescription());
        assertThat(responseBody.getLatitude()).isEqualTo(request.getLatitude());
        assertThat(responseBody.getLongitude()).isEqualTo(request.getLongitude());
        assertThat(responseBody.getNumberOfParticipants()).isEqualTo(request.getNumberOfParticipants());
        assertThat(responseBody.getSportType()).isEqualTo(request.getSportType());
    }

    @Test
    public void testGetEventById() {
        UUID eventId = UUID.randomUUID();
        Event event = new Event(
                eventId,
                18,
                25,
                "Basketball Game",
                Instant.parse("2025-05-10T10:00:00.00Z"),
                41.0,
                44.0,
                20,
                "Basketball"
        );
        eventRepository.save(event);

        ResponseEntity<EventResponse> response = restTemplate.getForEntity(createURL("/events/" + eventId), EventResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        EventResponse responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getEventId()).isEqualTo(event.getId());
        assertThat(responseBody.getDescription()).isEqualTo(event.getDescription());
        assertThat(responseBody.getAgeRange()).isEqualTo(event.getMinAge() + "-" + event.getMaxAge());
        assertThat(responseBody.getEventTime()).isEqualTo(event.getEventTime().toEpochMilli());
        assertThat(responseBody.getLatitude()).isEqualTo(event.getLatitude());
        assertThat(responseBody.getLongitude()).isEqualTo(event.getLongitude());
        assertThat(responseBody.getNumberOfParticipants()).isEqualTo(event.getNumberOfParticipants());
        assertThat(responseBody.getSportType()).isEqualTo(event.getSportType());
    }

    @Test
    public void testUpdateEvent() {
        UUID eventId = UUID.randomUUID();
        Event existingEvent = new Event(
                eventId,
                18,
                25,
                "Original Match",
                Instant.parse("2025-06-15T08:00:00.00Z"),
                41.5,
                44.5,
                15,
                "Soccer"
        );
        eventRepository.save(existingEvent);

        EventRequest updatedRequest = new EventRequest(
                "25-35",
                "Updated Soccer Match",
                Instant.parse("2025-06-20T10:00:00.00Z").toEpochMilli(),
                41.8,
                44.8,
                25,
                "Soccer"
        );

        restTemplate.put(createURL("/events/" + eventId), updatedRequest);

        Event updatedEvent = eventRepository.getById(eventId);
        assertThat(updatedEvent).isNotNull();
        assertThat(updatedEvent.getDescription()).isEqualTo(updatedRequest.getDescription());
        assertThat(updatedEvent.getEventTime().toEpochMilli()).isEqualTo(updatedRequest.getEventTime());
        assertThat(updatedEvent.getLatitude()).isEqualTo(updatedRequest.getLatitude());
        assertThat(updatedEvent.getLongitude()).isEqualTo(updatedRequest.getLongitude());
        assertThat(updatedEvent.getNumberOfParticipants()).isEqualTo(updatedRequest.getNumberOfParticipants());
    }

    @Test
    public void testGetAllEvents() {
        Event event1 = new Event(
                UUID.randomUUID(),
                18,
                25,
                "Football Match",
                Instant.parse("2025-04-16T10:00:00.00Z"),
                41.725788,
                44.727753,
                10,
                "Football"
        );
        Event event2 = new Event(
                UUID.randomUUID(),
                25,
                35,
                "Basketball Game",
                Instant.parse("2025-05-20T10:00:00.00Z"),
                42.0,
                45.0,
                20,
                "Basketball"
        );
        eventRepository.save(event1);
        eventRepository.save(event2);

        ResponseEntity<EventResponse[]> response = restTemplate.getForEntity(createURL("/events"), EventResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        EventResponse[] events = response.getBody();
        assertThat(events).isNotNull();
        assertThat(events.length).isEqualTo(2);

        List<EventResponse> eventResponses = Arrays.asList(events);

        assertThat(eventResponses).anyMatch(event -> event.getEventId().equals(event1.getId()) &&
                event.getDescription().equals(event1.getDescription()) &&
                event.getAgeRange().equals(event1.getMinAge() + "-" + event1.getMaxAge()) &&
                event.getEventTime() == event1.getEventTime().toEpochMilli() &&
                event.getLatitude() == (event1.getLatitude()) &&
                event.getLongitude() == (event1.getLongitude()) &&
                event.getNumberOfParticipants() == event1.getNumberOfParticipants() &&
                event.getSportType().equals(event1.getSportType()));

        assertThat(eventResponses).anyMatch(event -> event.getEventId().equals(event2.getId()) &&
                event.getDescription().equals(event2.getDescription()) &&
                event.getAgeRange().equals(event2.getMinAge() + "-" + event2.getMaxAge()) &&
                event.getEventTime() == event2.getEventTime().toEpochMilli() &&
                event.getLatitude() == (event2.getLatitude()) &&
                event.getLongitude() == (event2.getLongitude()) &&
                event.getNumberOfParticipants() == event2.getNumberOfParticipants() &&
                event.getSportType().equals(event2.getSportType()));

    }

    private String createURL(String uri) {
        return "http://localhost:" + port + uri;
    }
}
