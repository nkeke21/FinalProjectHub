package ge.join2play.join2playback.repository.interfaces;

import ge.join2play.join2playback.model.TournamentRegistration;

import java.util.List;
import java.util.UUID;

public interface TournamentRegistrationRepository {
    TournamentRegistration save(TournamentRegistration registration);
    TournamentRegistration getById(UUID id);
    List<TournamentRegistration> getByTournamentId(UUID tournamentId);
    List<TournamentRegistration> getByUserId(UUID userId);
    TournamentRegistration getByTournamentIdAndUserId(UUID tournamentId, UUID userId);
    TournamentRegistration update(TournamentRegistration registration);
    void delete(UUID id);
    int countByTournamentId(UUID tournamentId);
} 