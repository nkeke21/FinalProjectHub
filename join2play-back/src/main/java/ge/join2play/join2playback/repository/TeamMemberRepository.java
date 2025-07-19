package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.TeamMember;

import java.util.List;
import java.util.UUID;

public interface TeamMemberRepository {
    TeamMember getById(UUID id);
    TeamMember save(TeamMember teamMember);
    TeamMember update(TeamMember teamMember);
    void delete(UUID id);
    List<TeamMember> getAll();
    List<TeamMember> getMembersByTeam(UUID teamId);
    List<TeamMember> getTeamsByUser(UUID userId);
    TeamMember getMemberByTeamAndUser(UUID teamId, UUID userId);
    void removeUserFromTeam(UUID teamId, UUID userId);
    void clear();
} 