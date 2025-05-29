package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.FriendRequest;

import java.util.List;
import java.util.UUID;

public interface FriendRequestRepository {
    FriendRequest save(FriendRequest request);
    FriendRequest update(FriendRequest request);
    FriendRequest findById(UUID requestId);
    List<FriendRequest> findRequestsByUser(UUID userId);
    List<FriendRequest> getAll();
}
