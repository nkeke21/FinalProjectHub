package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.Team;
import ge.join2play.join2playback.model.enums.SportType;
import ge.join2play.join2playback.model.exceptions.TeamAlreadyExistsException;
import ge.join2play.join2playback.model.exceptions.TeamDoesNotExistException;
import ge.join2play.join2playback.repository.interfaces.TeamRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "memory", matchIfMissing = true)
public class TeamInMemoryRepository implements TeamRepository {
    private final Map<UUID, Team> teams = new ConcurrentHashMap<>();

    public TeamInMemoryRepository() {
        // Mock teams data
        UUID team1Id = UUID.fromString("a1b2c3d4-e5f6-7890-1234-567890abcdef");
        UUID team2Id = UUID.fromString("b2c3d4e5-f6a7-8901-2345-678901bcdef0");
        UUID team3Id = UUID.fromString("c3d4e5f6-a7b8-9012-3456-789012cdef01");

        // Existing user IDs from UserInMemoryRepository
        UUID kakhaId = UUID.fromString("13fa5e4e-1d9e-4a2a-9a20-7385f24e9097");
        UUID johnId = UUID.fromString("24fb6e3f-2dac-4eac-8df1-abc456789012");
        UUID mikeId = UUID.fromString("46bd8e5f-4abc-6a1c-8ef3-dcba87654321");

        Instant now = Instant.now();
        Instant yesterday = now.minusSeconds(86400);

        Team team1 = new Team(
                team1Id,
                "Vake Runners",
                "A running team for weekend enthusiasts in Vake park",
                SportType.RUNNING,
                kakhaId, // Kakha is captain
                15,
                18,
                45,
                yesterday,
                now
        );

        Team team2 = new Team(
                team2Id,
                "City Basketball Club",
                "Competitive basketball team looking for skilled players",
                SportType.BASKETBALL,
                johnId, // John is captain
                12,
                20,
                35,
                yesterday,
                now
        );

        Team team3 = new Team(
                team3Id,
                "Elite Football Squad",
                "Semi-professional football team",
                SportType.FOOTBALL,
                mikeId, // Mike is captain
                22,
                18,
                30,
                yesterday,
                now
        );

        teams.put(team1Id, team1);
        teams.put(team2Id, team2);
        teams.put(team3Id, team3);
    }

    @Override
    public Team getById(UUID id) {
        return teams.get(id);
    }

    @Override
    public Team save(Team team) {
        if (getById(team.getId()) != null) {
            throw new TeamAlreadyExistsException("Cannot save: team with ID " + team.getId() + " already exists.");
        }
        teams.put(team.getId(), team);
        return team;
    }

    @Override
    public Team update(Team team) {
        Team oldTeam = teams.replace(team.getId(), team);
        if (oldTeam == null) {
            throw new TeamDoesNotExistException("Cannot update: team with ID " + team.getId() + " does not exist.");
        }
        return team;
    }

    @Override
    public void delete(UUID id) {
        teams.remove(id);
    }

    @Override
    public List<Team> getAll() {
        return new ArrayList<>(teams.values());
    }

    @Override
    public List<Team> getTeamsByCaptain(UUID captainId) {
        return teams.values().stream()
                .filter(team -> team.getCaptainId().equals(captainId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Team> getAllTeams() {
        return new ArrayList<>(teams.values());
    }

    @Override
    public List<Team> getTeamsBySportType(SportType sportType) {
        return teams.values().stream()
                .filter(team -> team.getSportType().equals(sportType))
                .collect(Collectors.toList());
    }

    @Override
    public void clear() {
        teams.clear();
    }
} 