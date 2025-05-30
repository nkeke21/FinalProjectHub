package ge.join2play.join2playback.service;

import ge.join2play.join2playback.enums.FriendRequestStatus;
import ge.join2play.join2playback.model.FriendRequest;
import ge.join2play.join2playback.model.FriendRequestNotification;
import ge.join2play.join2playback.model.User;
import ge.join2play.join2playback.repository.FriendRequestRepository;
import ge.join2play.join2playback.repository.UserInMemoryRepository;
import ge.join2play.join2playback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FriendRequestService {

    private final FriendRequestRepository repository;
    private final UserInMemoryRepository userRepository;

    @Autowired
    public FriendRequestService(FriendRequestRepository repository, UserInMemoryRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public FriendRequest sendRequest(UUID fromUserId, UUID toUserId) {
        return repository.sendRequest(fromUserId, toUserId);
    }

    public List<FriendRequest> getIncomingRequests(UUID userId) {
        return repository.getPendingRequestsForUser(userId);
    }

    public FriendRequest respondToRequest(UUID requestId, FriendRequestStatus status) {
        Optional<FriendRequest> optional = repository.findById(requestId);
        if (optional.isPresent()) {
            FriendRequest req = optional.get();
            req.setStatus(status);
            // add as friends if status is true.
            return req;
        }
        throw new RuntimeException("Friend request not found");
    }

    public FriendRequestNotification buildNotification(FriendRequest request) {
        User sender = userRepository.getById(request.getFromUserId());
        if (sender == null) {
            throw new RuntimeException("Sender not found");
        }

        FriendRequestNotification notification = new FriendRequestNotification();
        notification.setRequestId(request.getRequestId());
        notification.setFromUserId(request.getFromUserId());

        String formattedMessage = String.format(
                "You have a new friend request from %s (%s)",
                sender.getName(),
                sender.getEmail()
        );
        notification.setMessage(formattedMessage);

        return notification;
    }
}