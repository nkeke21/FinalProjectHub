package ge.join2play.join2playback.repository.jpa_interfaces;

import ge.join2play.join2playback.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TournamentJPARepository extends JpaRepository<Tournament, UUID> {

    @Query("SELECT t FROM Tournament t WHERE t.hostId = :hostId")
    List<Tournament> findByHostId(@Param("hostId") UUID hostId);
}
