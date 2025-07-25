package ge.join2play.join2playback.repository.db;

import ge.join2play.join2playback.model.TournamentRegistration;
import ge.join2play.join2playback.model.enums.RegistrationStatus;
import ge.join2play.join2playback.repository.interfaces.TournamentRegistrationRepository;
import ge.join2play.join2playback.repository.jpa_interfaces.TournamentRegistrationJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository("tournamentRegistrationRepositoryJPA")
@ConditionalOnProperty(name = "app.repository.type", havingValue = "jpa")
public class TournamentRegistrationDBRepository implements TournamentRegistrationRepository {

    private final TournamentRegistrationJPARepository tournamentRegistrationJPARepository;

    @Autowired
    public TournamentRegistrationDBRepository(TournamentRegistrationJPARepository tournamentRegistrationJPARepository) {
        this.tournamentRegistrationJPARepository = tournamentRegistrationJPARepository;
    }

    @Override
    public TournamentRegistration save(TournamentRegistration registration) {
        if (registration.getId() == null) {
            registration.setId(UUID.randomUUID());
        }
        if (registration.getRegisteredAt() == null) {
            registration.setRegisteredAt(Instant.now());
        }
        registration.setUpdatedAt(Instant.now());

        return tournamentRegistrationJPARepository.save(registration);
    }

    @Override
    public TournamentRegistration getById(UUID id) {
        return tournamentRegistrationJPARepository.findById(id).orElse(null);
    }

    @Override
    public List<TournamentRegistration> getByTournamentId(UUID tournamentId) {
        return tournamentRegistrationJPARepository.findByTournamentId(tournamentId);
    }

    @Override
    public List<TournamentRegistration> getByUserId(UUID userId) {
        return tournamentRegistrationJPARepository.findByUserId(userId);
    }

    @Override
    public TournamentRegistration getByTournamentIdAndUserId(UUID tournamentId, UUID userId) {
        return tournamentRegistrationJPARepository.findByTournamentIdAndUserId(tournamentId, userId).orElse(null);
    }

    @Override
    public TournamentRegistration update(TournamentRegistration registration) {
        if (registration.getId() != null && tournamentRegistrationJPARepository.existsById(registration.getId())) {
            registration.setUpdatedAt(Instant.now());
            return tournamentRegistrationJPARepository.save(registration);
        }
        return null;
    }

    @Override
    public void delete(UUID id) {
        tournamentRegistrationJPARepository.deleteById(id);
    }

    @Override
    public int countByTournamentId(UUID tournamentId) {
        return tournamentRegistrationJPARepository.countByTournamentIdAndStatus(tournamentId, RegistrationStatus.REGISTERED);
    }
}
