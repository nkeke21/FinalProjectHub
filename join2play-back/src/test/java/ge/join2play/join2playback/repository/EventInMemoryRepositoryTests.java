package ge.join2play.join2playback.repository;


import ge.join2play.join2playback.model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EventInMemoryRepositoryTests {
    private EventInMemoryRepository repository;
    private Event sampleEvent;
    private Instant eventTime = Instant.parse("2025-04-16T10:15:30.00Z");
    private UUID eventId = UUID.fromString("e5d1d580-a4cf-4677-8220-a50c5decccfa");

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
    public void test() {
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
}
