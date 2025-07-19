package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.TeamMember;
import ge.join2play.join2playback.model.enums.TeamRole;
import ge.join2play.join2playback.model.exceptions.TeamMemberAlreadyExistsException;
import ge.join2play.join2playback.model.exceptions.TeamMemberDoesNotExistException;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TeamMemberInMemoryRepository implements TeamMemberRepository {
    private final Map<UUID, TeamMember> teamMembers = new HashMap<>();

    public TeamMemberInMemoryRepository() {
        // Team IDs from TeamInMemoryRepository
        UUID team1Id = UUID.fromString("a1b2c3d4-e5f6-7890-1234-567890abcdef"); // Vake Runners
        UUID team2Id = UUID.fromString("b2c3d4e5-f6a7-8901-2345-678901bcdef0"); // City Basketball Club
        UUID team3Id = UUID.fromString("c3d4e5f6-a7b8-9012-3456-789012cdef01"); // Elite Football Squad

        // User IDs from UserInMemoryRepository
        UUID kakhaId = UUID.fromString("13fa5e4e-1d9e-4a2a-9a20-7385f24e9097");
        UUID johnId = UUID.fromString("24fb6e3f-2dac-4eac-8df1-abc456789012");
        UUID janeId = UUID.fromString("35ac7d4e-3fac-5fab-9ed2-cba987654321");
        UUID mikeId = UUID.fromString("46bd8e5f-4abc-6a1c-8ef3-dcba87654321");
        UUID emilyId = UUID.fromString("57ce9f60-5bcd-7b2d-9f04-edcba7654321");

        Instant now = Instant.now();
        Instant yesterday = now.minusSeconds(86400);

        // Team 1 (Vake Runners) members - Kakha is captain
        TeamMember member1 = new TeamMember(
                UUID.randomUUID(),
                team1Id,
                kakhaId,
                TeamRole.CAPTAIN,
                yesterday
        );

        TeamMember member2 = new TeamMember(
                UUID.randomUUID(),
                team1Id,
                janeId,
                TeamRole.MEMBER,
                yesterday
        );

        TeamMember member3 = new TeamMember(
                UUID.randomUUID(),
                team1Id,
                emilyId,
                TeamRole.MEMBER,
                now
        );

        // Team 2 (City Basketball Club) members - John is captain  
        TeamMember member4 = new TeamMember(
                UUID.randomUUID(),
                team2Id,
                johnId,
                TeamRole.CAPTAIN,
                yesterday
        );

        TeamMember member5 = new TeamMember(
                UUID.randomUUID(),
                team2Id,
                mikeId,
                TeamRole.MEMBER,
                yesterday
        );

        // Team 3 (Elite Football Squad) members - Mike is captain
        TeamMember member6 = new TeamMember(
                UUID.randomUUID(),
                team3Id,
                mikeId,
                TeamRole.CAPTAIN,
                yesterday
        );

        TeamMember member7 = new TeamMember(
                UUID.randomUUID(),
                team3Id,
                kakhaId,
                TeamRole.MEMBER,
                now
        );

        // Store all members
        teamMembers.put(member1.getId(), member1);
        teamMembers.put(member2.getId(), member2);
        teamMembers.put(member3.getId(), member3);
        teamMembers.put(member4.getId(), member4);
        teamMembers.put(member5.getId(), member5);
        teamMembers.put(member6.getId(), member6);
        teamMembers.put(member7.getId(), member7);
    }

    @Override
    public TeamMember getById(UUID id) {
        return teamMembers.get(id);
    }

    @Override
    public TeamMember save(TeamMember teamMember) {
        // Check if user is already a member of this team
        TeamMember existing = getMemberByTeamAndUser(teamMember.getTeamId(), teamMember.getUserId());
        if (existing != null) {
            throw new TeamMemberAlreadyExistsException("User is already a member of this team.");
        }
        teamMembers.put(teamMember.getId(), teamMember);
        return teamMember;
    }

    @Override
    public TeamMember update(TeamMember teamMember) {
        TeamMember oldMember = teamMembers.replace(teamMember.getId(), teamMember);
        if (oldMember == null) {
            throw new TeamMemberDoesNotExistException("Cannot update: team member with ID " + teamMember.getId() + " does not exist.");
        }
        return teamMember;
    }

    @Override
    public void delete(UUID id) {
        teamMembers.remove(id);
    }

    @Override
    public List<TeamMember> getAll() {
        return new ArrayList<>(teamMembers.values());
    }

    @Override
    public List<TeamMember> getMembersByTeam(UUID teamId) {
        return teamMembers.values().stream()
                .filter(member -> member.getTeamId().equals(teamId))
                .collect(Collectors.toList());
    }

    @Override
    public List<TeamMember> getTeamsByUser(UUID userId) {
        return teamMembers.values().stream()
                .filter(member -> member.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public TeamMember getMemberByTeamAndUser(UUID teamId, UUID userId) {
        return teamMembers.values().stream()
                .filter(member -> member.getTeamId().equals(teamId) && member.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void removeUserFromTeam(UUID teamId, UUID userId) {
        TeamMember member = getMemberByTeamAndUser(teamId, userId);
        if (member != null) {
            teamMembers.remove(member.getId());
        }
    }

    @Override
    public void clear() {
        teamMembers.clear();
    }
} 