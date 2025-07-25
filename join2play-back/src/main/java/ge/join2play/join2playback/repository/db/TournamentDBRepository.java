package ge.join2play.join2playback.repository.db;

import ge.join2play.join2playback.model.Tournament;
import ge.join2play.join2playback.repository.interfaces.TournamentRepository;
import ge.join2play.join2playback.repository.jpa_interfaces.TournamentJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("tournamentRepositoryJPA")
@ConditionalOnProperty(name = "app.repository.type", havingValue = "jpa")
public class TournamentDBRepository implements TournamentRepository {

    private final TournamentJPARepository tournamentJPARepository;

    @Autowired
    public TournamentDBRepository(TournamentJPARepository tournamentJPARepository) {
        this.tournamentJPARepository = tournamentJPARepository;
    }

    @Override
    public Tournament save(Tournament tournament) {
        return tournamentJPARepository.save(tournament);
    }

    @Override
    public Tournament getById(UUID id) {
        return tournamentJPARepository.findById(id).orElse(null);
    }

    @Override
    public List<Tournament> getAllTournaments() {
        return tournamentJPARepository.findAll();
    }

    @Override
    public List<Tournament> getTournamentsByHost(UUID hostId) {
        return tournamentJPARepository.findByHostId(hostId);
    }

    @Override
    public Tournament update(Tournament tournament) {
        if (tournament.getId() != null && tournamentJPARepository.existsById(tournament.getId())) {
            return tournamentJPARepository.save(tournament);
        }
        return null;
    }

    @Override
    public void delete(UUID id) {
        tournamentJPARepository.deleteById(id);
    }
}
