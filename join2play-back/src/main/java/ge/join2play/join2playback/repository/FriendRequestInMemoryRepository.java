package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.enums.FriendRequestStatus;
import ge.join2play.join2playback.model.FriendRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FriendRequestInMemoryRepository implements FriendRequestRepository{
    private final Map<UUID, FriendRequest> requests = new HashMap<>();

    public FriendRequest sendRequest(UUID fromUserId, UUID toUserId) {
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
}