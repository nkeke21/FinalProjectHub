package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.Event;
import ge.join2play.join2playback.model.errors.EventAlreadyExistsError;
import ge.join2play.join2playback.model.errors.EventDoesNotExistError;
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

    @BeforeEach
    void setUp() {
        repository = new EventInMemoryRepository();
        sampleEvent = new Event(
                eventId,
                18,
                25,
                "Football Match",
                eventTime,
                41.725788,
                44.727753,
                10,
                "Football"
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
        assertEquals(result.getMinAge(), sampleEvent.getMinAge());
        assertEquals(result.getMaxAge(), sampleEvent.getMaxAge());
        assertEquals(result.getDescription(), sampleEvent.getDescription());
        assertEquals(result.getEventTime(), sampleEvent.getEventTime());
        assertEquals(result.getLatitude(), sampleEvent.getLatitude());
        assertEquals(result.getLongitude(), sampleEvent.getLongitude());
        assertEquals(result.getNumberOfParticipants(), sampleEvent.getNumberOfParticipants());
        assertEquals(result.getSportType(), sampleEvent.getSportType());
    }

    @Test
    public void testUpdateExistingEvent() {
        repository.save(sampleEvent);
        Event updatedEvent = new Event(
                sampleEvent.getId(),
                20,
                30,
                "Updated Football Match",
                eventTime,
                41.725788,
                44.727753,
                15,
                "Football"
        );

        Event result = repository.update(updatedEvent);
        assertEquals(updatedEvent, result);
        assertEquals(repository.getById(eventId), updatedEvent);
    }

    @Test
    public void testUpdateNonexistentEventThrowsException() {
        Event nonExistentEvent = new Event(
                UUID.randomUUID(),
                20,
                30,
                "Nonexistent Event",
                eventTime,
                41.725788,
                44.727753,
                15,
                "Basketball"
        );

        assertThrows(EventDoesNotExistError.class, () -> repository.update(nonExistentEvent),
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
                16,
                40,
                "Basketball Game",
                Instant.parse("2025-05-01T15:20:00.00Z"),
                40.123456,
                43.654321,
                12,
                "Basketball"
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
                19,
                26,
                "Modified Football Match",
                eventTime,
                41.725788,
                44.727753,
                11,
                "Football"
        );

        assertThrows(EventAlreadyExistsError.class, () -> repository.save(sameIdEvent));
    }

}
