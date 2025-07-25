package ge.join2play.join2playback.repository.jpa_interfaces;

import ge.join2play.join2playback.model.FriendRequest;
import ge.join2play.join2playback.model.enums.FriendRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendRequestJPARepository extends JpaRepository<FriendRequest, UUID> {

    @Query("SELECT fr FROM FriendRequest fr WHERE fr.toUserId = :userId AND fr.status = :status")
    List<FriendRequest> findByToUserIdAndStatus(@Param("userId") UUID userId, @Param("status") FriendRequestStatus status);

    @Query("SELECT fr FROM FriendRequest fr WHERE fr.toUserId = :userId")
    List<FriendRequest> findByToUserId(@Param("userId") UUID userId);

    @Query("SELECT CASE WHEN fr.fromUserId = :userId THEN fr.toUserId ELSE fr.fromUserId END " +
            "FROM FriendRequest fr WHERE (fr.fromUserId = :userId OR fr.toUserId = :userId) AND fr.status = :status")
    List<UUID> findAcceptedFriendIds(@Param("userId") UUID userId, @Param("status") FriendRequestStatus status);

    @Modifying
    @Query("DELETE FROM FriendRequest fr WHERE " +
            "(fr.fromUserId = :userId AND fr.toUserId = :friendId AND fr.status = :status) OR " +
            "(fr.fromUserId = :friendId AND fr.toUserId = :userId AND fr.status = :status)")
    void deleteAcceptedFriendship(@Param("userId") UUID userId, @Param("friendId") UUID friendId, @Param("status") FriendRequestStatus status);

    @Query("SELECT fr FROM FriendRequest fr WHERE " +
            "(fr.fromUserId = :user1Id AND fr.toUserId = :user2Id) OR " +
            "(fr.fromUserId = :user2Id AND fr.toUserId = :user1Id)")
    Optional<FriendRequest> findRequestBetweenUsers(@Param("user1Id") UUID user1Id, @Param("user2Id") UUID user2Id);
}
