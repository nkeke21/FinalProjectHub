package ge.join2play.join2playback.repository.interfaces;

import ge.join2play.join2playback.model.enums.TeamJoinRequestStatus;
import ge.join2play.join2playback.model.TeamJoinRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamJoinRequestRepository {
    TeamJoinRequest saveRequest(UUID teamId, UUID fromUserId);
    List<TeamJoinRequest> getPendingRequestsForTeam(UUID teamId);
    List<TeamJoinRequest> getAllPendingRequests();
    List<TeamJoinRequest> getAllRequestsForTeam(UUID teamId);
    List<TeamJoinRequest> getAllRequests();
    Optional<TeamJoinRequest> findById(UUID requestId);
    void updateStatus(UUID requestId, TeamJoinRequestStatus status);
    Optional<TeamJoinRequest> findRequestBetweenTeamAndUser(UUID teamId, UUID userId);
} 