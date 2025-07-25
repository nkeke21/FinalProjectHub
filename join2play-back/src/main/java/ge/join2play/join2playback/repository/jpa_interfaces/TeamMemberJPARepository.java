package ge.join2play.join2playback.repository.jpa_interfaces;

import ge.join2play.join2playback.model.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamMemberJPARepository extends JpaRepository<TeamMember, UUID> {

    @Query("SELECT tm FROM TeamMember tm WHERE tm.teamId = :teamId")
    List<TeamMember> findByTeamId(@Param("teamId") UUID teamId);

    @Query("SELECT tm FROM TeamMember tm WHERE tm.userId = :userId")
    List<TeamMember> findByUserId(@Param("userId") UUID userId);

    @Query("SELECT tm FROM TeamMember tm WHERE tm.teamId = :teamId AND tm.userId = :userId")
    Optional<TeamMember> findByTeamIdAndUserId(@Param("teamId") UUID teamId, @Param("userId") UUID userId);

    @Modifying
    @Query("DELETE FROM TeamMember tm WHERE tm.teamId = :teamId AND tm.userId = :userId")
    void deleteByTeamIdAndUserId(@Param("teamId") UUID teamId, @Param("userId") UUID userId);
}
