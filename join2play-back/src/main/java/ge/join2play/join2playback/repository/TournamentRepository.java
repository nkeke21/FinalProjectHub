package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.Tournament;

import java.util.List;
import java.util.UUID;

public interface TournamentRepository {
    Tournament save(Tournament tournament);
    Tournament getById(UUID id);
    List<Tournament> getAllTournaments();
    List<Tournament> getTournamentsByHost(UUID hostId);
    Tournament update(Tournament tournament);
    void delete(UUID id);
} 