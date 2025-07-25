package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.TeamJoinRequest;
import ge.join2play.join2playback.model.enums.TeamJoinRequestStatus;
import ge.join2play.join2playback.repository.interfaces.TeamJoinRequestRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "memory", matchIfMissing = true)
public class TeamJoinRequestInMemoryRepository implements TeamJoinRequestRepository {
    private final Map<UUID, TeamJoinRequest> requests = new ConcurrentHashMap<>();

    @Override
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

    @Override
    public List<TeamJoinRequest> getPendingRequestsForTeam(UUID teamId) {
        return requests.values().stream()
                .filter(req -> req.getTeamId().equals(teamId) && req.getStatus() == TeamJoinRequestStatus.PENDING)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeamJoinRequest> getAllPendingRequests() {
        return requests.values().stream()
                .filter(req -> req.getStatus() == TeamJoinRequestStatus.PENDING)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeamJoinRequest> getAllRequestsForTeam(UUID teamId) {
        return requests.values().stream()
                .filter(req -> req.getTeamId().equals(teamId))
                .collect(Collectors.toList());
    }

    @Override
    public List<TeamJoinRequest> getAllRequests() {
        return new ArrayList<>(requests.values());
    }

    @Override
    public Optional<TeamJoinRequest> findById(UUID requestId) {
        return Optional.ofNullable(requests.get(requestId));
    }

    @Override
    public void updateStatus(UUID requestId, TeamJoinRequestStatus status) {
        TeamJoinRequest request = requests.get(requestId);
        if (request != null) {
            request.setStatus(status);
        }
    }

    @Override
    public Optional<TeamJoinRequest> findRequestBetweenTeamAndUser(UUID teamId, UUID userId) {
        return requests.values().stream()
                .filter(req -> req.getTeamId().equals(teamId) && req.getFromUserId().equals(userId))
                .findFirst();
    }
} 