package ge.join2play.join2playback.repository.jpa_interfaces;

import ge.join2play.join2playback.model.TournamentRegistrationNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TournamentRegistrationNotificationJPARepository extends JpaRepository<TournamentRegistrationNotification, UUID> {

    @Query("SELECT trn FROM TournamentRegistrationNotification trn WHERE trn.hostId = :hostId ORDER BY trn.createdAt DESC")
    List<TournamentRegistrationNotification> findByHostIdOrderByCreatedAtDesc(@Param("hostId") UUID hostId);

    @Query("SELECT trn FROM TournamentRegistrationNotification trn WHERE trn.requesterId = :requesterId ORDER BY trn.createdAt DESC")
    List<TournamentRegistrationNotification> findByRequesterIdOrderByCreatedAtDesc(@Param("requesterId") UUID requesterId);

    @Query("SELECT trn FROM TournamentRegistrationNotification trn WHERE trn.registrationId = :registrationId")
    Optional<TournamentRegistrationNotification> findByRegistrationId(@Param("registrationId") UUID registrationId);

    @Query("SELECT COUNT(trn) FROM TournamentRegistrationNotification trn WHERE trn.hostId = :hostId AND trn.isRead = false")
    int countUnreadByHostId(@Param("hostId") UUID hostId);
}
