package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.*;
import ge.join2play.join2playback.repository.EventInMemoryRepository;
import ge.join2play.join2playback.repository.UserInMemoryRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.time.LocalDate;
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
    private EventInMemoryRepository eventRepository;
    @Autowired
    private  UserInMemoryRepository userRepository;

    private final UUID hostId = UUID.fromString("3d6005f9-f84d-43fa-a9f5-e15b51cdbe56");

    @BeforeEach
    void setUp() {
        eventRepository.clear();
        userRepository.clear();

        User user = new User(
                hostId,
                "Jane Doe",
                "j.doe@gmail.com",
                "+999",
                LocalDate.parse("2000-04-16"),
                "I love sport",
                "secure_passworD"
        );

        userRepository.save(user);
    }

    @Test
    public void testCreateEvent() {
        EventRequest request = new EventRequest(
                hostId,
                "18-25",
                "Football Match",
                Instant.parse("2025-04-16T10:15:30.00Z").toEpochMilli(),
                41.725788,
                44.727753,
                "Tbilisi",
                10,
                5,
                SportType.FOOTBALL.toString()
        );

        ResponseEntity<EventResponse> response = restTemplate.postForEntity(createURL("/api/events"), request, EventResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        EventResponse responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getHostId()).isEqualTo(hostId);
        assertThat(responseBody.getAgeRange()).isEqualTo(request.getAgeRange());
        assertThat(responseBody.getDescription()).isEqualTo(request.getDescription());
        assertThat(responseBody.getLatitude()).isEqualTo(request.getLatitude());
        assertThat(responseBody.getLongitude()).isEqualTo(request.getLongitude());
        assertThat(responseBody.getLocation()).isEqualTo(request.getLocation());
        assertThat(responseBody.getNumberOfParticipantsTotal()).isEqualTo(request.getNumberOfParticipantsTotal());
        assertThat(responseBody.getNumberOfParticipantsRegistered()).isEqualTo(request.getNumberOfParticipantsRegistered());
        assertThat(responseBody.getSportType()).isEqualTo(request.getSportType());
    }

    @Test
    public void testGetEventById() {
        UUID eventId = UUID.randomUUID();
        Event event = new Event(
                eventId,
                hostId,
                18,
                25,
                "Basketball Game",
                Instant.parse("2025-05-10T10:00:00.00Z"),
                41.0,
                44.0,
                "Tbilisi",
                20,
                13,
                SportType.BASKETBALL
        );
        eventRepository.save(event);

        ResponseEntity<EventResponse> response = restTemplate.getForEntity(createURL("/api/events/" + eventId), EventResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        EventResponse responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getEventId()).isEqualTo(event.getId());
        assertThat(responseBody.getHostId()).isEqualTo(hostId);
        assertThat(responseBody.getDescription()).isEqualTo(event.getDescription());
        assertThat(responseBody.getAgeRange()).isEqualTo(event.getMinAge() + "-" + event.getMaxAge());
        assertThat(responseBody.getEventTime()).isEqualTo(event.getEventTime().toEpochMilli());
        assertThat(responseBody.getLatitude()).isEqualTo(event.getLatitude());
        assertThat(responseBody.getLongitude()).isEqualTo(event.getLongitude());
        assertThat(responseBody.getLocation()).isEqualTo(event.getLocation());
        assertThat(responseBody.getNumberOfParticipantsTotal()).isEqualTo(event.getNumberOfParticipantsTotal());
        assertThat(responseBody.getNumberOfParticipantsRegistered()).isEqualTo(event.getNumberOfParticipantsRegistered());
        assertThat(responseBody.getSportType()).isEqualTo(event.getSportType().toString());
    }

    @Test
    public void testUpdateEvent() {
        UUID eventId = UUID.randomUUID();
        Event existingEvent = new Event(
                eventId,
                hostId,
                18,
                25,
                "Original Match",
                Instant.parse("2025-06-15T08:00:00.00Z"),
                41.5,
                44.5,
                "Tbilisi",
                15,
                10,
                SportType.FOOTBALL
        );
        eventRepository.save(existingEvent);

        EventRequest updatedRequest = new EventRequest(
                hostId,
                "25-35",
                "Updated Football Match",
                Instant.parse("2025-06-20T10:00:00.00Z").toEpochMilli(),
                41.8,
                44.8,
                "Near Tbilisi",
                25,
                18,
                SportType.FOOTBALL.toString()
        );

        restTemplate.put(createURL("/api/events/" + eventId), updatedRequest);

        Event updatedEvent = eventRepository.getById(eventId);
        assertThat(updatedEvent).isNotNull();
        assertThat(updatedEvent.getDescription()).isEqualTo(updatedRequest.getDescription());
        assertThat(updatedEvent.getEventTime().toEpochMilli()).isEqualTo(updatedRequest.getEventTime());
        assertThat(updatedEvent.getLatitude()).isEqualTo(updatedRequest.getLatitude());
        assertThat(updatedEvent.getLongitude()).isEqualTo(updatedRequest.getLongitude());
        assertThat(updatedEvent.getNumberOfParticipantsTotal()).isEqualTo(updatedRequest.getNumberOfParticipantsTotal());
        assertThat(updatedEvent.getNumberOfParticipantsRegistered()).isEqualTo(updatedRequest.getNumberOfParticipantsRegistered());
    }

    @Test
    public void testGetAllEvents() {
        Event event1 = new Event(
                UUID.randomUUID(),
                hostId,
                18,
                25,
                "Football Match",
                Instant.parse("2025-04-16T10:00:00.00Z"),
                41.725788,
                44.727753,
                "Location",
                10,
                6,
                SportType.FOOTBALL
        );
        Event event2 = new Event(
                UUID.randomUUID(),
                hostId,
                25,
                35,
                "Basketball Game",
                Instant.parse("2025-05-20T10:00:00.00Z"),
                42.0,
                45.0,
                "Location 2",
                20,
                14,
                SportType.BASKETBALL
        );
        eventRepository.save(event1);
        eventRepository.save(event2);

        ResponseEntity<EventResponse[]> response = restTemplate.getForEntity(createURL("/api/events"), EventResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        EventResponse[] events = response.getBody();
        assertThat(events).isNotNull();
        assertThat(events.length).isEqualTo(2);

        List<EventResponse> eventResponses = Arrays.asList(events);

        assertThat(eventResponses).anyMatch(event -> event.getEventId().equals(event1.getId()) &&
                event.getHostId().equals(event1.getHostId()) &&
                event.getDescription().equals(event1.getDescription()) &&
                event.getAgeRange().equals(event1.getMinAge() + "-" + event1.getMaxAge()) &&
                event.getEventTime() == event1.getEventTime().toEpochMilli() &&
                event.getLatitude() == (event1.getLatitude()) &&
                event.getLongitude() == (event1.getLongitude()) &&
                event.getLocation().equals(event1.getLocation()) &&
                event.getNumberOfParticipantsTotal() == event1.getNumberOfParticipantsTotal() &&
                event.getNumberOfParticipantsRegistered() == event1.getNumberOfParticipantsRegistered() &&
                event.getSportType().equals(event1.getSportType().toString()));

        assertThat(eventResponses).anyMatch(event -> event.getEventId().equals(event2.getId()) &&
                event.getHostId().equals(event2.getHostId()) &&
                event.getDescription().equals(event2.getDescription()) &&
                event.getAgeRange().equals(event2.getMinAge() + "-" + event2.getMaxAge()) &&
                event.getEventTime() == event2.getEventTime().toEpochMilli() &&
                event.getLatitude() == (event2.getLatitude()) &&
                event.getLongitude() == (event2.getLongitude()) &&
                event.getLocation().equals(event2.getLocation()) &&
                event.getNumberOfParticipantsTotal() == event2.getNumberOfParticipantsTotal() &&
                event.getNumberOfParticipantsRegistered() == event2.getNumberOfParticipantsRegistered() &&
                event.getSportType().equals(event2.getSportType().toString()));

    }

    private String createURL(String uri) {
        return "http://localhost:" + port + uri;
    }
}
