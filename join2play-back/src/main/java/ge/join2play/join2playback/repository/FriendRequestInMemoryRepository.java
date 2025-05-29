package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.FriendRequest;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class FriendRequestInMemoryRepository implements FriendRequestRepository {
    private final Map<UUID, FriendRequest> requests = new HashMap<>();

    @Override
    public FriendRequest save(FriendRequest request) {
        requests.put(request.getRequestId(), request);
        return request;
    }

    @Override
    public FriendRequest update(FriendRequest request) {
        requests.put(request.getRequestId(), request);
        return request;
    }

    @Override
    public FriendRequest findById(UUID requestId) {
        return requests.get(requestId);
    }

    @Override
    public List<FriendRequest> findRequestsByUser(UUID userId) {
        List<FriendRequest> result = new ArrayList<>();
        for (FriendRequest req : requests.values()) {
            if (req.getToUserId().equals(userId)) {
                result.add(req);
            }
        }
        return result;
    }

    @Override
    public List<FriendRequest> getAll() {
        return new ArrayList<>(requests.values());
    }
}
