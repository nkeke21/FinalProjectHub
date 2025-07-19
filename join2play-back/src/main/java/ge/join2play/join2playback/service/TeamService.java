package ge.join2play.join2playback.service;

import ge.join2play.join2playback.model.Team;
import ge.join2play.join2playback.model.TeamMember;
import ge.join2play.join2playback.model.User;
import ge.join2play.join2playback.model.dto.TeamMemberResponse;
import ge.join2play.join2playback.model.dto.TeamRequest;
import ge.join2play.join2playback.model.dto.TeamResponse;
import ge.join2play.join2playback.model.enums.SportType;
import ge.join2play.join2playback.model.enums.TeamRole;
import ge.join2play.join2playback.model.exceptions.TeamDoesNotExistException;
import ge.join2play.join2playback.model.exceptions.TeamMemberAlreadyExistsException;
import ge.join2play.join2playback.model.exceptions.UserDoesNotExistException;
import ge.join2play.join2playback.repository.TeamMemberRepository;
import ge.join2play.join2playback.repository.TeamRepository;
import ge.join2play.join2playback.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserRepository userRepository;

    public TeamService(TeamRepository teamRepository, TeamMemberRepository teamMemberRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.userRepository = userRepository;
    }

    public TeamResponse createTeam(TeamRequest teamRequest, UUID captainId) {
        // Validate captain exists
        Optional<User> captain = userRepository.getById(captainId);
        if (captain.isEmpty()) {
            throw new UserDoesNotExistException("Captain with id " + captainId + " does not exist.");
        }

        // Create team
        Team team = new Team(
                UUID.randomUUID(),
                teamRequest.getName(),
                teamRequest.getDescription(),
                teamRequest.getSportType(),
                captainId,
                teamRequest.getMaxMembers(),
                teamRequest.getMinAge(),
                teamRequest.getMaxAge(),
                Instant.now(),
                Instant.now()
        );

        Team savedTeam = teamRepository.save(team);

        // Add captain as team member
        TeamMember captainMember = new TeamMember(
                UUID.randomUUID(),
                savedTeam.getId(),
                captainId,
                TeamRole.CAPTAIN,
                Instant.now()
        );
        teamMemberRepository.save(captainMember);

        return convertTeamToTeamResponse(savedTeam);
    }

    public TeamResponse getTeam(UUID teamId) {
        Team team = teamRepository.getById(teamId);
        if (team == null) {
            throw new TeamDoesNotExistException("Team with id " + teamId + " does not exist.");
        }
        return convertTeamToTeamResponse(team);
    }

    public List<TeamResponse> getUserTeams(UUID userId) {
        // Get teams where user is a member
        List<TeamMember> userTeamMemberships = teamMemberRepository.getTeamsByUser(userId);
        return userTeamMemberships.stream()
                .map(membership -> teamRepository.getById(membership.getTeamId()))
                .filter(team -> team != null)
                .map(this::convertTeamToTeamResponse)
                .collect(Collectors.toList());
    }

    public List<TeamResponse> getAvailableTeams(UUID userId) {
        // Get all teams where user is not a member
        List<Team> allTeams = teamRepository.getAllTeams();
        List<TeamMember> userMemberships = teamMemberRepository.getTeamsByUser(userId);
        
        // Get team IDs where user is already a member
        List<UUID> userTeamIds = userMemberships.stream()
                .map(TeamMember::getTeamId)
                .collect(Collectors.toList());

        return allTeams.stream()
                .filter(team -> !userTeamIds.contains(team.getId()))
                .filter(team -> hasAvailableSlots(team.getId(), team.getMaxMembers()))
                .map(this::convertTeamToTeamResponse)
                .collect(Collectors.toList());
    }

    public TeamResponse joinTeam(UUID teamId, UUID userId) {
        // Validate team exists
        Team team = teamRepository.getById(teamId);
        if (team == null) {
            throw new TeamDoesNotExistException("Team with id " + teamId + " does not exist.");
        }

        // Validate user exists
        Optional<User> user = userRepository.getById(userId);
        if (user.isEmpty()) {
            throw new UserDoesNotExistException("User with id " + userId + " does not exist.");
        }

        // Check if user is already a member
        TeamMember existing = teamMemberRepository.getMemberByTeamAndUser(teamId, userId);
        if (existing != null) {
            throw new TeamMemberAlreadyExistsException("User is already a member of this team.");
        }

        // Check if team has available slots
        if (!hasAvailableSlots(teamId, team.getMaxMembers())) {
            throw new RuntimeException("Team is full.");
        }

        // Add user as team member
        TeamMember newMember = new TeamMember(
                UUID.randomUUID(),
                teamId,
                userId,
                TeamRole.MEMBER,
                Instant.now()
        );
        teamMemberRepository.save(newMember);

        return convertTeamToTeamResponse(team);
    }

    public TeamResponse updateTeam(UUID teamId, TeamRequest teamRequest, UUID userId) {
        Team team = teamRepository.getById(teamId);
        if (team == null) {
            throw new TeamDoesNotExistException("Team with id " + teamId + " does not exist.");
        }

        // Check if user is captain
        if (!team.getCaptainId().equals(userId)) {
            throw new RuntimeException("Only team captain can update team settings.");
        }

        // Update team
        team.setName(teamRequest.getName());
        team.setDescription(teamRequest.getDescription());
        team.setSportType(teamRequest.getSportType());
        team.setMaxMembers(teamRequest.getMaxMembers());
        team.setMinAge(teamRequest.getMinAge());
        team.setMaxAge(teamRequest.getMaxAge());
        team.setUpdatedAt(Instant.now());

        Team updatedTeam = teamRepository.update(team);
        return convertTeamToTeamResponse(updatedTeam);
    }

    public void leaveTeam(UUID teamId, UUID userId) {
        Team team = teamRepository.getById(teamId);
        if (team == null) {
            throw new TeamDoesNotExistException("Team with id " + teamId + " does not exist.");
        }

        TeamMember member = teamMemberRepository.getMemberByTeamAndUser(teamId, userId);
        if (member == null) {
            throw new RuntimeException("User is not a member of this team.");
        }

        // Captain cannot leave unless they transfer captaincy
        if (member.getRole() == TeamRole.CAPTAIN) {
            throw new RuntimeException("Captain cannot leave team. Transfer captaincy first.");
        }

        teamMemberRepository.removeUserFromTeam(teamId, userId);
    }

    public void removeTeamMember(UUID teamId, UUID memberId, UUID captainId) {
        Team team = teamRepository.getById(teamId);
        if (team == null) {
            throw new TeamDoesNotExistException("Team with id " + teamId + " does not exist.");
        }

        if (!team.getCaptainId().equals(captainId)) {
            throw new RuntimeException("Only team captain can remove team members.");
        }

        TeamMember memberToRemove = teamMemberRepository.getMemberByTeamAndUser(teamId, memberId);
        if (memberToRemove == null) {
            throw new RuntimeException("User is not a member of this team.");
        }

        if (memberToRemove.getRole() == TeamRole.CAPTAIN) {
            throw new RuntimeException("Captain cannot remove themselves from the team.");
        }

        teamMemberRepository.removeUserFromTeam(teamId, memberId);
    }

    private boolean hasAvailableSlots(UUID teamId, int maxMembers) {
        List<TeamMember> members = teamMemberRepository.getMembersByTeam(teamId);
        return members.size() < maxMembers;
    }

    private TeamResponse convertTeamToTeamResponse(Team team) {
        List<TeamMember> members = teamMemberRepository.getMembersByTeam(team.getId());
        List<TeamMemberResponse> memberResponses = members.stream()
                .map(this::convertTeamMemberToResponse)
                .collect(Collectors.toList());

        // Get captain name
        Optional<User> captain = userRepository.getById(team.getCaptainId());
        String captainName = captain.map(User::getName).orElse("Unknown");

        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getDescription(),
                team.getSportType(),
                team.getCaptainId(),
                captainName,
                memberResponses,
                team.getMaxMembers(),
                team.getMinAge(),
                team.getMaxAge(),
                team.getCreatedAt(),
                team.getUpdatedAt()
        );
    }

    private TeamMemberResponse convertTeamMemberToResponse(TeamMember member) {
        Optional<User> user = userRepository.getById(member.getUserId());
        String name = user.map(User::getName).orElse("Unknown");
        String email = user.map(User::getEmail).orElse("Unknown");

        return new TeamMemberResponse(
                member.getUserId(),
                name,
                email,
                member.getRole(),
                member.getJoinedAt()
        );
    }
} 