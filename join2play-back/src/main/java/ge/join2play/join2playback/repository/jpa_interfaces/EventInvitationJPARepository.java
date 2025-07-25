package ge.join2play.join2playback.repository.jpa_interfaces;

import ge.join2play.join2playback.model.EventInvitation;
import ge.join2play.join2playback.model.enums.EventInvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventInvitationJPARepository extends JpaRepository<EventInvitation, UUID> {

    @Query("SELECT ei FROM EventInvitation ei WHERE ei.toUserId = :userId AND ei.status = :status ORDER BY ei.sentAt DESC")
    List<EventInvitation> findByToUserIdAndStatusOrderBySentAtDesc(@Param("userId") UUID userId, @Param("status") EventInvitationStatus status);

    @Query("SELECT ei FROM EventInvitation ei WHERE ei.toUserId = :userId ORDER BY ei.sentAt DESC")
    List<EventInvitation> findByToUserIdOrderBySentAtDesc(@Param("userId") UUID userId);

    @Query("SELECT ei FROM EventInvitation ei WHERE ei.eventId = :eventId ORDER BY ei.sentAt DESC")
    List<EventInvitation> findByEventIdOrderBySentAtDesc(@Param("eventId") UUID eventId);

    @Query("SELECT ei FROM EventInvitation ei WHERE ei.eventId = :eventId AND ei.toUserId = :userId")
    Optional<EventInvitation> findByEventIdAndToUserId(@Param("eventId") UUID eventId, @Param("userId") UUID userId);

    @Query("SELECT CASE WHEN COUNT(ei) > 0 THEN true ELSE false END FROM EventInvitation ei WHERE ei.eventId = :eventId AND ei.toUserId = :userId AND ei.status = :status")
    boolean existsByEventIdAndToUserIdAndStatus(@Param("eventId") UUID eventId, @Param("userId") UUID userId, @Param("status") EventInvitationStatus status);

    @Query("SELECT ei FROM EventInvitation ei WHERE ei.fromUserId = :userId ORDER BY ei.sentAt DESC")
    List<EventInvitation> findByFromUserIdOrderBySentAtDesc(@Param("userId") UUID userId);
}
