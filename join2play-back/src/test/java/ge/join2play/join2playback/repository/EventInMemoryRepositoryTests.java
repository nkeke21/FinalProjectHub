package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.Event;
import ge.join2play.join2playback.model.enums.SportType;
import ge.join2play.join2playback.model.exceptions.EventAlreadyExistsException;
import ge.join2play.join2playback.model.exceptions.EventDoesNotExistException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class EventInMemoryRepositoryTests {
    private EventInMemoryRepository repository;
    private Event sampleEvent;
    private final Instant eventTime = Instant.parse("2025-04-16T10:15:30.00Z");
    private final UUID eventId = UUID.fromString("e5d1d580-a4cf-4677-8220-a50c5decccfa");
    private final UUID hostId = UUID.fromString("3d6005f9-f84d-43fa-a9f5-e15b51cdbe56");

    @BeforeEach
    void setUp() {
        repository = new EventInMemoryRepository();
        sampleEvent = new Event(
                eventId,
                hostId,
                18,
                25,
                "Football Match",
                eventTime,
                41.725788,
                44.727753,
                "Tbilisi",
                10,
                5,
                SportType.FOOTBALL
        );
    }

    @AfterEach
    void cleanUp() {
        repository.delete(sampleEvent.getId());
    }

    @Test
    public void testSaveAndGetById() {
        repository.save(sampleEvent);
        Event result = repository.getById(eventId);
        assertNotNull(result);
        assertEquals(result.getId(), sampleEvent.getId());
        assertEquals(result.getHostId(), sampleEvent.getHostId());
        assertEquals(result.getMinAge(), sampleEvent.getMinAge());
        assertEquals(result.getMaxAge(), sampleEvent.getMaxAge());
        assertEquals(result.getDescription(), sampleEvent.getDescription());
        assertEquals(result.getEventTime(), sampleEvent.getEventTime());
        assertEquals(result.getLatitude(), sampleEvent.getLatitude());
        assertEquals(result.getLongitude(), sampleEvent.getLongitude());
        assertEquals(result.getLocation(), sampleEvent.getLocation());
        assertEquals(result.getNumberOfParticipantsTotal(), sampleEvent.getNumberOfParticipantsTotal());
        assertEquals(result.getNumberOfParticipantsRegistered(), sampleEvent.getNumberOfParticipantsRegistered());
        assertEquals(result.getSportType(), sampleEvent.getSportType());
    }

    @Test
    public void testUpdateExistingEvent() {
        repository.save(sampleEvent);
        Event updatedEvent = new Event(
                sampleEvent.getId(),
                sampleEvent.getHostId(),
                20,
                30,
                "Updated Football Match",
                eventTime,
                41.725788,
                44.727753,
                "Tbilisi",
                15,
                7,
                SportType.FOOTBALL
        );

        Event result = repository.update(updatedEvent);
        assertEquals(updatedEvent, result);
        assertEquals(repository.getById(eventId), updatedEvent);
    }

    @Test
    public void testUpdateNonexistentEventThrowsException() {
        Event nonExistentEvent = new Event(
                UUID.randomUUID(),
                hostId,
                20,
                30,
                "Nonexistent Event",
                eventTime,
                41.725788,
                44.727753,
                "Tbilisi"
                , 15,
                9,
                SportType.BASKETBALL
        );

        assertThrows(EventDoesNotExistException.class, () -> repository.update(nonExistentEvent),
                "Updating a nonexistent event should throw EventDoesNotExistError.");
    }

    @Test
    public void testDeleteExistingEvent() {
        repository.save(sampleEvent);
        repository.delete(eventId);
        Event result = repository.getById(eventId);
        assertNull(result, "Event should be null after being deleted.");
    }

    @Test
    public void testDeleteNonexistentEvent() {
        UUID randomId = UUID.randomUUID();
        assertDoesNotThrow(() -> repository.delete(randomId),
                "Deleting a nonexistent event should not throw an exception.");
    }

    @Test
    public void testGetAllEvents() {
        Event anotherEvent = new Event(
                UUID.randomUUID(),
                hostId,
                16,
                40,
                "Basketball Game",
                Instant.parse("2025-05-01T15:20:00.00Z"),
                40.123456,
                43.654321,
                "New location",
                12,
                4,
                SportType.BASKETBALL
        );

        repository.save(sampleEvent);
        repository.save(anotherEvent);

        List<Event> events = repository.getAll();
        assertEquals(2, events.size());
        assertTrue(events.contains(sampleEvent));
        assertTrue(events.contains(anotherEvent));
    }

    @Test
    public void testGetAllForEmptyRepository() {
        List<Event> events = repository.getAll();
        assertTrue(events.isEmpty());
    }

    @Test
    public void testSaveExistingEventThrowsException() {
        repository.save(sampleEvent);
        Event sameIdEvent = new Event(
                sampleEvent.getId(),
                sampleEvent.getHostId(),
                19,
                26,
                "Modified Football Match",
                eventTime,
                41.725788,
                44.727753,
                "Tbilisi",
                11,
                6,
                SportType.FOOTBALL
        );

        assertThrows(EventAlreadyExistsException.class, () -> repository.save(sameIdEvent));
    }

}
