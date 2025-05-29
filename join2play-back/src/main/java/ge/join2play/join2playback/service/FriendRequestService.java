package ge.join2play.join2playback.service;

import ge.join2play.join2playback.enums.FriendRequestStatus;
import ge.join2play.join2playback.model.FriendRequest;
import ge.join2play.join2playback.repository.FriendRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FriendRequestService {

    private final FriendRequestRepository repository;

    @Autowired
    public FriendRequestService(FriendRequestRepository repository) {
        this.repository = repository;
    }

    public FriendRequest sendRequest(UUID fromUserId, UUID toUserId) {
        return repository.sendRequest(fromUserId, toUserId);
    }

    public List<FriendRequest> getIncomingRequests(UUID userId) {
        return repository.getPendingRequestsForUser(userId);
    }

    public boolean respondToRequest(UUID requestId, FriendRequestStatus status) {
        if (repository.findById(requestId).isPresent()) {
            repository.updateStatus(requestId, status);
            return true;
        }
        return false;
    }
}