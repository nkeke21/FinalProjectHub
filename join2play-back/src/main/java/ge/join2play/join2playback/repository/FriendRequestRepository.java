package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.enums.FriendRequestStatus;
import ge.join2play.join2playback.model.FriendRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendRequestRepository {
    FriendRequest saveRequest(UUID fromUserId, UUID toUserId);
    List<FriendRequest> getPendingRequestsForUser(UUID userId);
    List<FriendRequest> getAllRequestsForUser(UUID userId);
    Optional<FriendRequest> findById(UUID requestId);
    void updateStatus(UUID requestId, FriendRequestStatus status);
    List<UUID> getAllAcceptedFriendIds(UUID userId);
    void deleteFriend(UUID userId, UUID friendId);
}
