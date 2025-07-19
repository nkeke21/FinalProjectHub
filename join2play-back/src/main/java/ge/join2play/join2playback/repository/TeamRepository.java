package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.Team;
import ge.join2play.join2playback.model.enums.SportType;

import java.util.List;
import java.util.UUID;

public interface TeamRepository {
    Team getById(UUID id);
    Team save(Team team);
    Team update(Team team);
    void delete(UUID id);
    List<Team> getAll();
    List<Team> getTeamsByCaptain(UUID captainId);
    List<Team> getPublicTeams();
    List<Team> getTeamsBySportType(SportType sportType);
    void clear();
} 