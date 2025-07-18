package ge.join2play.join2playback.service;

import ge.join2play.join2playback.model.enums.FriendRequestStatus;
import ge.join2play.join2playback.model.FriendRequest;
import ge.join2play.join2playback.model.FriendRequestNotification;
import ge.join2play.join2playback.model.User;
import ge.join2play.join2playback.model.exceptions.*;
import ge.join2play.join2playback.repository.FriendRequestRepository;
import ge.join2play.join2playback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FriendRequestService {

    private final FriendRequestRepository repository;
    private final UserRepository userRepository;

    @Autowired
    public FriendRequestService(FriendRequestRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public FriendRequest saveRequest(UUID fromUserId, UUID toUserId) {
        Optional<User> sender = userRepository.getById(fromUserId);
        if (sender.isEmpty()) {
            throw new UserDoesNotExistException("Sender not found with id: " + fromUserId);
        }

        Optional<User> receiver = userRepository.getById(toUserId);
        if (receiver.isEmpty()) {
            throw new UserDoesNotExistException("Receiver not found with id: " + toUserId);
        }

        if( fromUserId.equals(toUserId)) {
            throw new SelfFriendRequestException("Cannot send a friend request to yourself.");
        }

        if (repository.getPendingRequestsForUser(toUserId).stream()
                .anyMatch(req -> req.getFromUserId().equals(fromUserId) && req.getStatus() != FriendRequestStatus.REJECTED)) {
            throw new FriendRequestAlreadyExistsException("Friend request already exists from " + fromUserId + " to " + toUserId);
        }

        return repository.saveRequest(fromUserId, toUserId);
    }

    public List<FriendRequest> getPendingRequests(UUID userId) {
        Optional<User> optional = userRepository.getById(userId);
        if (optional.isEmpty()) {
            throw new UserDoesNotExistException("Sender not found with id: " + userId);
        }

        return repository.getPendingRequestsForUser(userId);
    }

    public FriendRequest respondToRequest(UUID requestId, FriendRequestStatus status) {
        Optional<FriendRequest> optional = repository.findById(requestId);
        if (optional.isPresent()) {
            FriendRequest req = optional.get();
            req.setStatus(status);
            repository.updateStatus(requestId, status);
            return req;
        }
        throw new FriendRequestDoesNotExistException("Friend request not found for id: " + requestId);
    }

    public FriendRequestNotification buildNotification(FriendRequest request) {
        Optional<User> sender = userRepository.getById(request.getFromUserId());
        if (sender.isEmpty()) {
            throw new UserDoesNotExistException("Sender not found with id: " + request.getFromUserId());
        }

        FriendRequestNotification notification = new FriendRequestNotification();
        notification.setRequestId(request.getRequestId());
        notification.setFromUserId(request.getFromUserId());

        String formattedMessage = String.format(
                "You have a new friend request from %s (%s)",
                sender.get().getName(),
                sender.get().getEmail()
        );
        notification.setMessage(formattedMessage);

        return notification;
    }

    public List<UUID> getFriends(UUID id) {
        Optional<User> sender = userRepository.getById(id);
        if (sender.isEmpty()) {
            throw new UserDoesNotExistException("User not found with id: " + id);
        }

        return repository.getAllAcceptedFriendIds(id);
    }

    public void deleteFriend(UUID userId, UUID friendId) {
        Optional<User> user = userRepository.getById(userId);
        if (user.isEmpty()) {
            throw new UserDoesNotExistException("User not found with id: " + userId);
        }

        Optional<User> friend = userRepository.getById(friendId);
        if (friend.isEmpty()) {
            throw new UserDoesNotExistException("User not found with id: " + friendId);
        }

        if (!repository.getAllAcceptedFriendIds(userId).contains(friendId)) {
            throw new UserIsNotFriendException("Friend not found with id: " + friendId);
        }

         repository.deleteFriend(userId, friendId);
    }

}