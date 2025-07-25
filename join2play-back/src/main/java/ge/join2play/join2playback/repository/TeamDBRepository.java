package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.Team;
import ge.join2play.join2playback.model.enums.SportType;
import ge.join2play.join2playback.model.exceptions.TeamAlreadyExistsException;
import ge.join2play.join2playback.model.exceptions.TeamDoesNotExistException;
import ge.join2play.join2playback.repository.interfaces.TeamRepository;
import ge.join2play.join2playback.repository.jpa_interfaces.TeamJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("teamRepositoryJPA")
@ConditionalOnProperty(name = "app.repository.type", havingValue = "jpa")
public class TeamDBRepository implements TeamRepository {

    private final TeamJPARepository teamJPARepository;

    @Autowired
    public TeamDBRepository(TeamJPARepository teamJPARepository) {
        this.teamJPARepository = teamJPARepository;
    }

    @Override
    public Team getById(UUID id) {
        return teamJPARepository.findById(id).orElse(null);
    }

    @Override
    public Team save(Team team) {
        if (team.getId() != null && teamJPARepository.existsById(team.getId())) {
            throw new TeamAlreadyExistsException("Cannot save: team with ID " + team.getId() + " already exists.");
        }

        if (team.getId() == null) {
            team.setId(UUID.randomUUID());
        }

        return teamJPARepository.save(team);
    }

    @Override
    public Team update(Team team) {
        if (team.getId() == null || !teamJPARepository.existsById(team.getId())) {
            throw new TeamDoesNotExistException("Cannot update: team with ID " + team.getId() + " does not exist.");
        }

        return teamJPARepository.save(team);
    }

    @Override
    public void delete(UUID id) {
        teamJPARepository.deleteById(id);
    }

    @Override
    public List<Team> getAll() {
        return teamJPARepository.findAll();
    }

    @Override
    public List<Team> getTeamsByCaptain(UUID captainId) {
        return teamJPARepository.findByCaptainId(captainId);
    }

    @Override
    public List<Team> getAllTeams() {
        return teamJPARepository.findAll();
    }

    @Override
    public List<Team> getTeamsBySportType(SportType sportType) {
        return teamJPARepository.findBySportType(sportType);
    }

    @Override
    public void clear() {
        teamJPARepository.deleteAll();
    }
}
