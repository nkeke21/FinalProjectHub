package ge.join2play.join2playback.repository.jpa_interfaces;

import ge.join2play.join2playback.model.TournamentRegistration;
import ge.join2play.join2playback.model.enums.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TournamentRegistrationJPARepository extends JpaRepository<TournamentRegistration, UUID> {

    @Query("SELECT tr FROM TournamentRegistration tr WHERE tr.tournamentId = :tournamentId")
    List<TournamentRegistration> findByTournamentId(@Param("tournamentId") UUID tournamentId);

    @Query("SELECT tr FROM TournamentRegistration tr WHERE tr.userId = :userId")
    List<TournamentRegistration> findByUserId(@Param("userId") UUID userId);

    @Query("SELECT tr FROM TournamentRegistration tr WHERE tr.tournamentId = :tournamentId AND tr.userId = :userId")
    Optional<TournamentRegistration> findByTournamentIdAndUserId(@Param("tournamentId") UUID tournamentId, @Param("userId") UUID userId);

    @Query("SELECT COUNT(tr) FROM TournamentRegistration tr WHERE tr.tournamentId = :tournamentId AND tr.status = :status")
    int countByTournamentIdAndStatus(@Param("tournamentId") UUID tournamentId, @Param("status") RegistrationStatus status);
}
