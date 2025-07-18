package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.enums.FriendRequestStatus;
import ge.join2play.join2playback.model.FriendRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FriendRequestInMemoryRepository implements FriendRequestRepository {
    private final Map<UUID, FriendRequest> requests = new HashMap<>();

    public FriendRequest saveRequest(UUID fromUserId, UUID toUserId) {
        FriendRequest request = new FriendRequest(
                UUID.randomUUID(),
                fromUserId,
                toUserId,
                LocalDateTime.now(),
                FriendRequestStatus.PENDING
        );

        requests.put(request.getRequestId(), request);
        return request;
    }

    public List<FriendRequest> getPendingRequestsForUser(UUID userId) {
        return requests.values().stream()
                .filter(req -> req.getToUserId().equals(userId) && req.getStatus() == FriendRequestStatus.PENDING)
                .collect(Collectors.toList());
    }

    public Optional<FriendRequest> findById(UUID requestId) {
        return Optional.ofNullable(requests.get(requestId));
    }

    public void updateStatus(UUID requestId, FriendRequestStatus status) {
        FriendRequest request = requests.get(requestId);
        if (request != null) {
            request.setStatus(status);
        }
    }

    @Override
    public List<UUID> getAllAcceptedFriendIds(UUID userId) {
        return requests.values().stream()
                .filter(req -> (req.getFromUserId().equals(userId) || req.getToUserId().equals(userId)) && req.getStatus() == FriendRequestStatus.ACCEPTED)
                .map(req -> req.getFromUserId().equals(userId) ? req.getToUserId() : req.getFromUserId())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public void deleteFriend(UUID userId, UUID friendId) {
        requests.values().removeIf(req ->
            (req.getFromUserId().equals(userId) && req.getToUserId().equals(friendId) && req.getStatus() == FriendRequestStatus.ACCEPTED)
            || (req.getFromUserId().equals(friendId) && req.getToUserId().equals(userId) && req.getStatus() == FriendRequestStatus.ACCEPTED)
        );
    }
}