package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.enums.TeamJoinRequestStatus;
import ge.join2play.join2playback.model.TeamJoinRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TeamJoinRequestInMemoryRepository implements TeamJoinRequestRepository {
    private final Map<UUID, TeamJoinRequest> requests = new HashMap<>();

    public TeamJoinRequest saveRequest(UUID teamId, UUID fromUserId) {
        TeamJoinRequest request = new TeamJoinRequest(
                UUID.randomUUID(),
                teamId,
                fromUserId,
                LocalDateTime.now(),
                TeamJoinRequestStatus.PENDING
        );

        requests.put(request.getRequestId(), request);
        return request;
    }

    public List<TeamJoinRequest> getPendingRequestsForTeam(UUID teamId) {
        return requests.values().stream()
                .filter(req -> req.getTeamId().equals(teamId) && req.getStatus() == TeamJoinRequestStatus.PENDING)
                .collect(Collectors.toList());
    }

    public List<TeamJoinRequest> getPendingRequestsForUser(UUID userId) {
        // This method returns all pending requests - filtering by user role (requester vs captain) is done in service layer
        return requests.values().stream()
                .filter(req -> req.getStatus() == TeamJoinRequestStatus.PENDING)
                .collect(Collectors.toList());
    }

    public List<TeamJoinRequest> getPendingRequestsForTeamCaptain(UUID captainId) {
        // This method returns all pending requests - filtering by captain is done in service layer
        return requests.values().stream()
                .filter(req -> req.getStatus() == TeamJoinRequestStatus.PENDING)
                .collect(Collectors.toList());
    }

    public List<TeamJoinRequest> getAllRequestsForTeam(UUID teamId) {
        return requests.values().stream()
                .filter(req -> req.getTeamId().equals(teamId))
                .collect(Collectors.toList());
    }

    public List<TeamJoinRequest> getAllRequestsForUser(UUID userId) {
        // This method returns all requests - filtering by user role (requester vs captain) is done in service layer
        return new ArrayList<>(requests.values());
    }

    public List<TeamJoinRequest> getAllRequestsForTeamCaptain(UUID captainId) {
        // This method returns all requests - filtering by captain is done in service layer
        return new ArrayList<>(requests.values());
    }

    public Optional<TeamJoinRequest> findById(UUID requestId) {
        return Optional.ofNullable(requests.get(requestId));
    }

    public void updateStatus(UUID requestId, TeamJoinRequestStatus status) {
        TeamJoinRequest request = requests.get(requestId);
        if (request != null) {
            request.setStatus(status);
        }
    }

    public Optional<TeamJoinRequest> findRequestBetweenTeamAndUser(UUID teamId, UUID userId) {
        return requests.values().stream()
                .filter(req -> req.getTeamId().equals(teamId) && req.getFromUserId().equals(userId))
                .findFirst();
    }
} 