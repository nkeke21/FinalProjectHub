package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.Tournament;
import ge.join2play.join2playback.model.enums.SportType;
import ge.join2play.join2playback.model.enums.TournamentFormat;
import ge.join2play.join2playback.repository.interfaces.TournamentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "memory", matchIfMissing = true)
public class TournamentInMemoryRepository implements TournamentRepository {
    private final Map<UUID, Tournament> tournaments = new HashMap<>();

    public TournamentInMemoryRepository() {
        // Initialize with some sample tournaments for testing
        initializeSampleTournaments();
    }

    private void initializeSampleTournaments() {
        UUID hostId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

        Tournament sampleTournament1 = new Tournament(
                UUID.randomUUID(),
                "Tbilisi Football Championship",
                "Annual football tournament featuring the best teams from Tbilisi. Join us for an exciting competition with great prizes!",
                SportType.FOOTBALL,
                TournamentFormat.SINGLE_ELIMINATION,
                "team",
                hostId,
                "John Doe",
                "Tbilisi, Georgia",
                41.7151,
                44.8271,
                Instant.parse("2025-06-15T09:00:00Z"),
                Instant.parse("2025-06-20T18:00:00Z"),
                Instant.parse("2025-06-10T23:59:59Z"),
                16,
                8,
                50.0,
                1000.0,
                18,
                35,
                List.of("All participants must follow fair play rules.", "Teams must have 11 players.", "Matches are 90 minutes long."),
                Instant.now(),
                Instant.now()
        );

        Tournament sampleTournament2 = new Tournament(
                UUID.randomUUID(),
                "Basketball 3v3 Tournament",
                "Fast-paced 3v3 basketball tournament. Perfect for teams looking for quick, exciting games!",
                SportType.BASKETBALL,
                TournamentFormat.DOUBLE_ELIMINATION,
                "team",
                hostId,
                "John Doe",
                "Tbilisi Sports Complex",
                41.7151,
                44.8271,
                Instant.parse("2025-07-01T10:00:00Z"),
                Instant.parse("2025-07-03T18:00:00Z"),
                Instant.parse("2025-06-25T23:59:59Z"),
                32,
                12,
                25.0,
                500.0,
                16,
                40,
                List.of("3v3 format", "First to 21 points wins", "Shot clock: 12 seconds"),
                Instant.now(),
                Instant.now()
        );

        tournaments.put(sampleTournament1.getId(), sampleTournament1);
        tournaments.put(sampleTournament2.getId(), sampleTournament2);
    }

    @Override
    public Tournament save(Tournament tournament) {
        tournaments.put(tournament.getId(), tournament);
        return tournament;
    }

    @Override
    public Tournament getById(UUID id) {
        return tournaments.get(id);
    }

    @Override
    public List<Tournament> getAllTournaments() {
        return new ArrayList<>(tournaments.values());
    }

    @Override
    public List<Tournament> getTournamentsByHost(UUID hostId) {
        return tournaments.values().stream()
                .filter(tournament -> tournament.getHostId().equals(hostId))
                .toList();
    }

    @Override
    public Tournament update(Tournament tournament) {
        if (tournaments.containsKey(tournament.getId())) {
            tournaments.put(tournament.getId(), tournament);
            return tournament;
        }
        return null;
    }

    @Override
    public void delete(UUID id) {
        tournaments.remove(id);
    }
} 