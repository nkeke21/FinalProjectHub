package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.TeamMember;
import ge.join2play.join2playback.model.exceptions.TeamMemberAlreadyExistsException;
import ge.join2play.join2playback.model.exceptions.TeamMemberDoesNotExistException;
import ge.join2play.join2playback.repository.interfaces.TeamMemberRepository;
import ge.join2play.join2playback.repository.jpa_interfaces.TeamMemberJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("teamMemberRepositoryJPA")
@ConditionalOnProperty(name = "app.repository.type", havingValue = "jpa")
public class TeamMemberDBRepository implements TeamMemberRepository {

    private final TeamMemberJPARepository teamMemberJPARepository;

    @Autowired
    public TeamMemberDBRepository(TeamMemberJPARepository teamMemberJPARepository) {
        this.teamMemberJPARepository = teamMemberJPARepository;
    }

    @Override
    public TeamMember getById(UUID id) {
        return teamMemberJPARepository.findById(id).orElse(null);
    }

    @Override
    public TeamMember save(TeamMember teamMember) {
        // Check if user is already a member of this team
        Optional<TeamMember> existing = teamMemberJPARepository.findByTeamIdAndUserId(teamMember.getTeamId(), teamMember.getUserId());
        if (existing.isPresent()) {
            throw new TeamMemberAlreadyExistsException("User is already a member of this team.");
        }

        if (teamMember.getId() == null) {
            teamMember.setId(UUID.randomUUID());
        }

        return teamMemberJPARepository.save(teamMember);
    }

    @Override
    public TeamMember update(TeamMember teamMember) {
        if (teamMember.getId() == null || !teamMemberJPARepository.existsById(teamMember.getId())) {
            throw new TeamMemberDoesNotExistException("Cannot update: team member with ID " + teamMember.getId() + " does not exist.");
        }

        return teamMemberJPARepository.save(teamMember);
    }

    @Override
    public void delete(UUID id) {
        teamMemberJPARepository.deleteById(id);
    }

    @Override
    public List<TeamMember> getAll() {
        return teamMemberJPARepository.findAll();
    }

    @Override
    public List<TeamMember> getMembersByTeam(UUID teamId) {
        return teamMemberJPARepository.findByTeamId(teamId);
    }

    @Override
    public List<TeamMember> getTeamsByUser(UUID userId) {
        return teamMemberJPARepository.findByUserId(userId);
    }

    @Override
    public TeamMember getMemberByTeamAndUser(UUID teamId, UUID userId) {
        return teamMemberJPARepository.findByTeamIdAndUserId(teamId, userId).orElse(null);
    }

    @Override
    @Transactional
    public void removeUserFromTeam(UUID teamId, UUID userId) {
        teamMemberJPARepository.deleteByTeamIdAndUserId(teamId, userId);
    }

    @Override
    public void clear() {
        teamMemberJPARepository.deleteAll();
    }
}
