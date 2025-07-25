package ge.join2play.join2playback.service;

import ge.join2play.join2playback.model.Team;
import ge.join2play.join2playback.model.TeamJoinRequest;
import ge.join2play.join2playback.model.User;
import ge.join2play.join2playback.model.enums.TeamJoinRequestStatus;
import ge.join2play.join2playback.model.enums.TeamRole;
import ge.join2play.join2playback.model.exceptions.TeamJoinRequestAlreadyExistsException;
import ge.join2play.join2playback.model.exceptions.UserAgeNotInRangeException;
import ge.join2play.join2playback.repository.inmemory.TeamJoinRequestInMemoryRepository;
import ge.join2play.join2playback.repository.inmemory.TeamMemberInMemoryRepository;
import ge.join2play.join2playback.repository.inmemory.TeamInMemoryRepository;
import ge.join2play.join2playback.repository.inmemory.UserInMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TeamJoinRequestServiceTests {

    private TeamJoinRequestService service;
    private TeamJoinRequestInMemoryRepository requestRepository;
    private TeamInMemoryRepository teamRepository;
    private UserInMemoryRepository userRepository;
    private TeamMemberInMemoryRepository teamMemberRepository;

    private UUID teamId;
    private UUID captainId;
    private UUID userId;
    private Team team;
    private User captain;
    private User user;

    @BeforeEach
    void setUp() {
        requestRepository = new TeamJoinRequestInMemoryRepository();
        teamRepository = new TeamInMemoryRepository();
        userRepository = new UserInMemoryRepository();
        teamMemberRepository = new TeamMemberInMemoryRepository();

        service = new TeamJoinRequestService(requestRepository, teamRepository, userRepository, teamMemberRepository);

        // Create test data
        teamId = UUID.randomUUID();
        captainId = UUID.randomUUID();
        userId = UUID.randomUUID();

        // Create captain (25 years old)
        long captainBirthDate = LocalDate.now().minusYears(25).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        captain = new User(captainId, "Captain", "captain@test.com", "123456789", captainBirthDate, "Test captain", "password");
        userRepository.save(captain);

        // Create user (22 years old)
        long userBirthDate = LocalDate.now().minusYears(22).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        user = new User(userId, "User", "user@test.com", "987654321", userBirthDate, "Test user", "password");
        userRepository.save(user);

        // Create team (age range 18-30)
        team = new Team(teamId, "Test Team", "Test Description", ge.join2play.join2playback.model.enums.SportType.FOOTBALL, captainId, 10, 18, 30, Instant.now(), Instant.now());
        teamRepository.save(team);

        // Add captain as team member
        teamMemberRepository.save(new ge.join2play.join2playback.model.TeamMember(
                UUID.randomUUID(), teamId, captainId, TeamRole.CAPTAIN, Instant.now()
        ));
    }

    @Test
    void testSendJoinRequest_Success() {
        // Act
        TeamJoinRequest request = service.sendJoinRequest(teamId, userId);

        // Assert
        assertNotNull(request);
        assertEquals(teamId, request.getTeamId());
        assertEquals(userId, request.getFromUserId());
        assertEquals(TeamJoinRequestStatus.PENDING, request.getStatus());
    }

    @Test
    void testSendJoinRequest_UserAgeNotInRange() {
        // Create user outside age range (15 years old)
        UUID youngUserId = UUID.randomUUID();
        long youngUserBirthDate = LocalDate.now().minusYears(15).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        User youngUser = new User(youngUserId, "Young User", "young@test.com", "111111111", youngUserBirthDate, "Young user", "password");
        userRepository.save(youngUser);

        // Act & Assert
        assertThrows(UserAgeNotInRangeException.class, () -> {
            service.sendJoinRequest(teamId, youngUserId);
        });
    }

    @Test
    void testSendJoinRequest_AlreadyPending() {
        // Arrange - send first request
        service.sendJoinRequest(teamId, userId);

        // Act & Assert - try to send another request
        assertThrows(TeamJoinRequestAlreadyExistsException.class, () -> {
            service.sendJoinRequest(teamId, userId);
        });
    }

    @Test
    void testRespondToRequest_Approve() {
        // Arrange
        TeamJoinRequest request = service.sendJoinRequest(teamId, userId);

        // Act
        TeamJoinRequest response = service.respondToRequest(request.getRequestId(), TeamJoinRequestStatus.APPROVED, captainId);

        // Assert
        assertEquals(TeamJoinRequestStatus.APPROVED, response.getStatus());
        
        // Check that user was added to team
        List<ge.join2play.join2playback.model.TeamMember> members = teamMemberRepository.getMembersByTeam(teamId);
        assertTrue(members.stream().anyMatch(member -> member.getUserId().equals(userId)));
    }

    @Test
    void testRespondToRequest_Reject() {
        // Arrange
        TeamJoinRequest request = service.sendJoinRequest(teamId, userId);

        // Act
        TeamJoinRequest response = service.respondToRequest(request.getRequestId(), TeamJoinRequestStatus.REJECTED, captainId);

        // Assert
        assertEquals(TeamJoinRequestStatus.REJECTED, response.getStatus());
        
        // Check that user was NOT added to team
        List<ge.join2play.join2playback.model.TeamMember> members = teamMemberRepository.getMembersByTeam(teamId);
        assertFalse(members.stream().anyMatch(member -> member.getUserId().equals(userId)));
    }

    @Test
    void testGetPendingRequestsForTeam() {
        // Arrange
        service.sendJoinRequest(teamId, userId);

        // Act
        List<TeamJoinRequest> requests = service.getPendingRequestsForTeam(teamId);

        // Assert
        assertEquals(1, requests.size());
        assertEquals(userId, requests.get(0).getFromUserId());
    }

    @Test
    void testGetPendingRequestsForUser() {
        // Arrange
        service.sendJoinRequest(teamId, userId);

        // Act
        List<TeamJoinRequest> requests = service.getPendingRequestsForUser(userId);

        // Assert
        assertEquals(1, requests.size());
        assertEquals(teamId, requests.get(0).getTeamId());
    }
} 