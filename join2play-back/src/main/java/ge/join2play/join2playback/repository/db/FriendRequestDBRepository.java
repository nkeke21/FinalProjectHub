package ge.join2play.join2playback.repository.db;

import ge.join2play.join2playback.model.FriendRequest;
import ge.join2play.join2playback.model.enums.FriendRequestStatus;
import ge.join2play.join2playback.repository.interfaces.FriendRequestRepository;
import ge.join2play.join2playback.repository.jpa_interfaces.FriendRequestJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("friendRequestRepositoryJPA")
@ConditionalOnProperty(name = "app.repository.type", havingValue = "jpa")
public class FriendRequestDBRepository implements FriendRequestRepository {

    private final FriendRequestJPARepository friendRequestJPARepository;

    @Autowired
    public FriendRequestDBRepository(FriendRequestJPARepository friendRequestJPARepository) {
        this.friendRequestJPARepository = friendRequestJPARepository;
    }

    @Override
    public FriendRequest saveRequest(UUID fromUserId, UUID toUserId) {
        FriendRequest request = new FriendRequest(
                UUID.randomUUID(),
                fromUserId,
                toUserId,
                LocalDateTime.now(),
                FriendRequestStatus.PENDING
        );
        return friendRequestJPARepository.save(request);
    }

    @Override
    public List<FriendRequest> getPendingRequestsForUser(UUID userId) {
        return friendRequestJPARepository.findByToUserIdAndStatus(userId, FriendRequestStatus.PENDING);
    }

    @Override
    public List<FriendRequest> getAllRequestsForUser(UUID userId) {
        return friendRequestJPARepository.findByToUserId(userId);
    }

    @Override
    public Optional<FriendRequest> findById(UUID requestId) {
        return friendRequestJPARepository.findById(requestId);
    }

    @Override
    public void updateStatus(UUID requestId, FriendRequestStatus status) {
        Optional<FriendRequest> request = friendRequestJPARepository.findById(requestId);
        if (request.isPresent()) {
            FriendRequest req = request.get();
            req.setStatus(status);
            friendRequestJPARepository.save(req);
        }
    }

    @Override
    public List<UUID> getAllAcceptedFriendIds(UUID userId) {
        return friendRequestJPARepository.findAcceptedFriendIds(userId, FriendRequestStatus.ACCEPTED);
    }

    @Override
    @Transactional
    public void deleteFriend(UUID userId, UUID friendId) {
        friendRequestJPARepository.deleteAcceptedFriendship(userId, friendId, FriendRequestStatus.ACCEPTED);
    }

    @Override
    public Optional<FriendRequest> findRequestBetweenUsers(UUID user1Id, UUID user2Id) {
        return friendRequestJPARepository.findRequestBetweenUsers(user1Id, user2Id);
    }
}
