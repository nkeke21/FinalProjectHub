package ge.join2play.join2playback.repository.jpa_interfaces;

import ge.join2play.join2playback.model.Team;
import ge.join2play.join2playback.model.enums.SportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TeamJPARepository extends JpaRepository<Team, UUID> {

    @Query("SELECT t FROM Team t WHERE t.captainId = :captainId")
    List<Team> findByCaptainId(@Param("captainId") UUID captainId);

    @Query("SELECT t FROM Team t WHERE t.sportType = :sportType")
    List<Team> findBySportType(@Param("sportType") SportType sportType);
}
