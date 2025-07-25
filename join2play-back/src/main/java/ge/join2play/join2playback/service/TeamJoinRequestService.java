package ge.join2play.join2playback.service;

import ge.join2play.join2playback.model.Team;
import ge.join2play.join2playback.model.TeamJoinRequest;
import ge.join2play.join2playback.model.TeamJoinRequestNotification;
import ge.join2play.join2playback.model.TeamMember;
import ge.join2play.join2playback.model.User;
import ge.join2play.join2playback.model.enums.TeamJoinRequestStatus;
import ge.join2play.join2playback.model.enums.TeamRole;
import ge.join2play.join2playback.model.exceptions.*;
import ge.join2play.join2playback.repository.interfaces.TeamJoinRequestRepository;
import ge.join2play.join2playback.repository.interfaces.TeamMemberRepository;
import ge.join2play.join2playback.repository.interfaces.TeamRepository;
import ge.join2play.join2playback.repository.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TeamJoinRequestService {

    private final TeamJoinRequestRepository repository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Autowired
    public TeamJoinRequestService(TeamJoinRequestRepository repository, TeamRepository teamRepository, 
                                 UserRepository userRepository, TeamMemberRepository teamMemberRepository) {
        this.repository = repository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.teamMemberRepository = teamMemberRepository;
    }

    public TeamJoinRequest sendJoinRequest(UUID teamId, UUID fromUserId) {
        // Validate team exists
        Team team = teamRepository.getById(teamId);
        if (team == null) {
            throw new TeamDoesNotExistException("Team with id " + teamId + " does not exist.");
        }

        // Validate user exists
        Optional<User> user = userRepository.getById(fromUserId);
        if (user.isEmpty()) {
            throw new UserDoesNotExistException("User with id " + fromUserId + " does not exist.");
        }

        // Check if user is already a member
        TeamMember existingMember = teamMemberRepository.getMemberByTeamAndUser(teamId, fromUserId);
        if (existingMember != null) {
            throw new TeamMemberAlreadyExistsException("User is already a member of this team.");
        }

        // Check if there's already a pending request
        Optional<TeamJoinRequest> existingRequest = repository.findRequestBetweenTeamAndUser(teamId, fromUserId);
        if (existingRequest.isPresent() && existingRequest.get().getStatus() == TeamJoinRequestStatus.PENDING) {
            throw new TeamJoinRequestAlreadyExistsException("A join request is already pending for this team.");
        }

        // Validate user age
        int userAge = calculateAge(user.get().getBirthDate());
        if (userAge < team.getMinAge() || userAge > team.getMaxAge()) {
            throw new UserAgeNotInRangeException(
                String.format("User age %d is outside the team's age range (%d-%d).", 
                    userAge, team.getMinAge(), team.getMaxAge()));
        }

        // Check if team has available slots
        List<TeamMember> members = teamMemberRepository.getMembersByTeam(teamId);
        if (members.size() >= team.getMaxMembers()) {
            throw new RuntimeException("Team is full.");
        }

        return repository.saveRequest(teamId, fromUserId);
    }

    public List<TeamJoinRequest> getPendingRequestsForTeam(UUID teamId) {
        Team team = teamRepository.getById(teamId);
        if (team == null) {
            throw new TeamDoesNotExistException("Team with id " + teamId + " does not exist.");
        }
        return repository.getPendingRequestsForTeam(teamId);
    }

    public List<TeamJoinRequest> getPendingRequestsForUser(UUID userId) {
        Optional<User> user = userRepository.getById(userId);
        if (user.isEmpty()) {
            throw new UserDoesNotExistException("User with id " + userId + " does not exist.");
        }
        
        // Get all teams where this user is the captain
        List<Team> teamsWhereCaptain = teamRepository.getAllTeams().stream()
                .filter(team -> team.getCaptainId().equals(userId))
                .toList();
        
        // Get all pending requests for these teams
        return teamsWhereCaptain.stream()
                .flatMap(team -> repository.getPendingRequestsForTeam(team.getId()).stream())
                .collect(Collectors.toList());
    }

    public List<TeamJoinRequest> getPendingRequestsForTeamCaptain(UUID captainId) {
        Optional<User> user = userRepository.getById(captainId);
        if (user.isEmpty()) {
            throw new UserDoesNotExistException("User with id " + captainId + " does not exist.");
        }
        
        // Get all teams where this user is the captain
        List<Team> teamsWhereCaptain = teamRepository.getAllTeams().stream()
                .filter(team -> team.getCaptainId().equals(captainId))
                .toList();
        
        // Get all pending requests for these teams
        return teamsWhereCaptain.stream()
                .flatMap(team -> repository.getPendingRequestsForTeam(team.getId()).stream())
                .collect(Collectors.toList());
    }

    public List<TeamJoinRequest> getAllRequestsForTeam(UUID teamId) {
        Team team = teamRepository.getById(teamId);
        if (team == null) {
            throw new TeamDoesNotExistException("Team with id " + teamId + " does not exist.");
        }
        return repository.getAllRequestsForTeam(teamId);
    }

    public List<TeamJoinRequest> getAllRequestsForUser(UUID userId) {
        Optional<User> user = userRepository.getById(userId);
        if (user.isEmpty()) {
            throw new UserDoesNotExistException("User with id " + userId + " does not exist.");
        }
        
        // Get all teams where this user is the captain
        List<Team> teamsWhereCaptain = teamRepository.getAllTeams().stream()
                .filter(team -> team.getCaptainId().equals(userId))
                .toList();
        
        // Get all requests for these teams
        return teamsWhereCaptain.stream()
                .flatMap(team -> repository.getAllRequestsForTeam(team.getId()).stream())
                .collect(Collectors.toList());
    }

    public List<TeamJoinRequest> getRequestsSentByUser(UUID userId) {
        Optional<User> user = userRepository.getById(userId);
        if (user.isEmpty()) {
            throw new UserDoesNotExistException("User with id " + userId + " does not exist.");
        }
        
        // Get all requests where this user is the requester
        return repository.getAllPendingRequests().stream()
                .filter(req -> req.getFromUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<TeamJoinRequest> getPendingRequestsSentByUser(UUID userId) {
        Optional<User> user = userRepository.getById(userId);
        if (user.isEmpty()) {
            throw new UserDoesNotExistException("User with id " + userId + " does not exist.");
        }
        
        // Get all pending requests where this user is the requester
        return repository.getAllPendingRequests().stream()
                .filter(req -> req.getFromUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<TeamJoinRequest> getAllRequestsForTeamCaptain(UUID captainId) {
        Optional<User> user = userRepository.getById(captainId);
        if (user.isEmpty()) {
            throw new UserDoesNotExistException("User with id " + captainId + " does not exist.");
        }
        
        // Get all teams where this user is the captain
        List<Team> teamsWhereCaptain = teamRepository.getAllTeams().stream()
                .filter(team -> team.getCaptainId().equals(captainId))
                .toList();
        
        // Get all requests for these teams
        return teamsWhereCaptain.stream()
                .flatMap(team -> repository.getAllRequestsForTeam(team.getId()).stream())
                .collect(Collectors.toList());
    }

    public TeamJoinRequest respondToRequest(UUID requestId, TeamJoinRequestStatus status, UUID captainId) {
        Optional<TeamJoinRequest> optional = repository.findById(requestId);
        if (optional.isEmpty()) {
            throw new TeamJoinRequestDoesNotExistException("Team join request not found for id: " + requestId);
        }

        TeamJoinRequest request = optional.get();
        Team team = teamRepository.getById(request.getTeamId());
        
        if (team == null) {
            throw new TeamDoesNotExistException("Team not found.");
        }

        // Verify that the responding user is the team captain
        if (!team.getCaptainId().equals(captainId)) {
            throw new RuntimeException("Only team captain can respond to join requests.");
        }

        // Update request status
        repository.updateStatus(requestId, status);
        request.setStatus(status);

        // If approved, add user to team
        if (status == TeamJoinRequestStatus.APPROVED) {
            // Check if team still has available slots
            List<TeamMember> members = teamMemberRepository.getMembersByTeam(request.getTeamId());
            if (members.size() >= team.getMaxMembers()) {
                throw new RuntimeException("Team is now full.");
            }

            // Add user as team member
            TeamMember newMember = new TeamMember(
                    UUID.randomUUID(),
                    request.getTeamId(),
                    request.getFromUserId(),
                    TeamRole.MEMBER,
                    Instant.now()
            );
            teamMemberRepository.save(newMember);
        }

        return request;
    }

    public TeamJoinRequestNotification buildNotification(TeamJoinRequest request) {
        Optional<User> sender = userRepository.getById(request.getFromUserId());
        if (sender.isEmpty()) {
            throw new UserDoesNotExistException("User not found with id: " + request.getFromUserId());
        }

        Team team = teamRepository.getById(request.getTeamId());
        if (team == null) {
            throw new TeamDoesNotExistException("Team not found with id: " + request.getTeamId());
        }

        TeamJoinRequestNotification notification = new TeamJoinRequestNotification();
        notification.setRequestId(request.getRequestId());
        notification.setTeamId(request.getTeamId());
        notification.setFromUserId(request.getFromUserId());

        String formattedMessage = String.format(
                "%s wants to join your team '%s'",
                sender.get().getName(),
                team.getName()
        );
        notification.setMessage(formattedMessage);

        return notification;
    }

    public Optional<TeamJoinRequest> findRequestBetweenTeamAndUser(UUID teamId, UUID userId) {
        Team team = teamRepository.getById(teamId);
        if (team == null) {
            throw new TeamDoesNotExistException("Team with id " + teamId + " does not exist.");
        }

        Optional<User> user = userRepository.getById(userId);
        if (user.isEmpty()) {
            throw new UserDoesNotExistException("User with id " + userId + " does not exist.");
        }

        return repository.findRequestBetweenTeamAndUser(teamId, userId);
    }

    public UUID getTeamCaptainId(UUID teamId) {
        Team team = teamRepository.getById(teamId);
        if (team == null) {
            throw new TeamDoesNotExistException("Team with id " + teamId + " does not exist.");
        }
        return team.getCaptainId();
    }

    private int calculateAge(Long birthDate) {
        if (birthDate == null) {
            return 0;
        }
        LocalDate birth = Instant.ofEpochMilli(birthDate).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();
        return now.getYear() - birth.getYear() - (now.getDayOfYear() < birth.getDayOfYear() ? 1 : 0);
    }
} 