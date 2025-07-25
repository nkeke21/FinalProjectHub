package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.TournamentRegistrationNotification;
import ge.join2play.join2playback.repository.interfaces.TournamentRegistrationNotificationRepository;
import ge.join2play.join2playback.repository.jpa_interfaces.TournamentRegistrationNotificationJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("tournamentRegistrationNotificationRepositoryJPA")
@ConditionalOnProperty(name = "app.repository.type", havingValue = "jpa")
public class TournamentRegistrationNotificationDBRepository implements TournamentRegistrationNotificationRepository {

    private final TournamentRegistrationNotificationJPARepository tournamentRegistrationNotificationJPARepository;

    @Autowired
    public TournamentRegistrationNotificationDBRepository(TournamentRegistrationNotificationJPARepository tournamentRegistrationNotificationJPARepository) {
        this.tournamentRegistrationNotificationJPARepository = tournamentRegistrationNotificationJPARepository;
    }

    @Override
    public TournamentRegistrationNotification save(TournamentRegistrationNotification notification) {
        return tournamentRegistrationNotificationJPARepository.save(notification);
    }

    @Override
    public TournamentRegistrationNotification getById(UUID id) {
        return tournamentRegistrationNotificationJPARepository.findById(id).orElse(null);
    }

    @Override
    public List<TournamentRegistrationNotification> getByHostId(UUID hostId) {
        return tournamentRegistrationNotificationJPARepository.findByHostIdOrderByCreatedAtDesc(hostId);
    }

    @Override
    public List<TournamentRegistrationNotification> getByRequesterId(UUID requesterId) {
        return tournamentRegistrationNotificationJPARepository.findByRequesterIdOrderByCreatedAtDesc(requesterId);
    }

    @Override
    public TournamentRegistrationNotification getByRegistrationId(UUID registrationId) {
        return tournamentRegistrationNotificationJPARepository.findByRegistrationId(registrationId).orElse(null);
    }

    @Override
    public void delete(UUID id) {
        tournamentRegistrationNotificationJPARepository.deleteById(id);
    }

    @Override
    public void markAsRead(UUID id) {
        Optional<TournamentRegistrationNotification> notification = tournamentRegistrationNotificationJPARepository.findById(id);
        if (notification.isPresent()) {
            TournamentRegistrationNotification notif = notification.get();
            notif.setRead(true);
            tournamentRegistrationNotificationJPARepository.save(notif);
        }
    }

    @Override
    public int getUnreadCountByHostId(UUID hostId) {
        return tournamentRegistrationNotificationJPARepository.countUnreadByHostId(hostId);
    }
}
