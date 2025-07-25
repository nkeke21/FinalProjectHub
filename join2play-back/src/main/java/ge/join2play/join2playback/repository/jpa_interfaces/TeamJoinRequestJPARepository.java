package ge.join2play.join2playback.repository.jpa_interfaces;

import ge.join2play.join2playback.model.TeamJoinRequest;
import ge.join2play.join2playback.model.enums.TeamJoinRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamJoinRequestJPARepository extends JpaRepository<TeamJoinRequest, UUID> {

    @Query("SELECT tjr FROM TeamJoinRequest tjr WHERE tjr.teamId = :teamId AND tjr.status = :status")
    List<TeamJoinRequest> findByTeamIdAndStatus(@Param("teamId") UUID teamId, @Param("status") TeamJoinRequestStatus status);

    @Query("SELECT tjr FROM TeamJoinRequest tjr WHERE tjr.status = :status")
    List<TeamJoinRequest> findByStatus(@Param("status") TeamJoinRequestStatus status);

    @Query("SELECT tjr FROM TeamJoinRequest tjr WHERE tjr.teamId = :teamId")
    List<TeamJoinRequest> findByTeamId(@Param("teamId") UUID teamId);

    @Query("SELECT tjr FROM TeamJoinRequest tjr WHERE tjr.teamId = :teamId AND tjr.fromUserId = :userId")
    Optional<TeamJoinRequest> findByTeamIdAndFromUserId(@Param("teamId") UUID teamId, @Param("userId") UUID userId);
}
