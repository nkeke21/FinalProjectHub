package ge.join2play.join2playback.repository.inmemory;

import ge.join2play.join2playback.model.FriendRequest;
import ge.join2play.join2playback.model.enums.FriendRequestStatus;
import ge.join2play.join2playback.repository.interfaces.FriendRequestRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "memory", matchIfMissing = true)
public class FriendRequestInMemoryRepository implements FriendRequestRepository {
    private final Map<UUID, FriendRequest> requests = new ConcurrentHashMap<>();

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

    public List<FriendRequest> getAllRequestsForUser(UUID userId) {
        return requests.values().stream()
                .filter(req -> req.getToUserId().equals(userId))
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

    @Override
    public Optional<FriendRequest> findRequestBetweenUsers(UUID user1Id, UUID user2Id) {
        return requests.values().stream()
                .filter(req ->
                        (req.getFromUserId().equals(user1Id) && req.getToUserId().equals(user2Id)) ||
                                (req.getFromUserId().equals(user2Id) && req.getToUserId().equals(user1Id))
                )
                .findFirst();
    }
}