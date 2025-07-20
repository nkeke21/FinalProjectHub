package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.TournamentRegistrationNotification;

import java.util.List;
import java.util.UUID;

public interface TournamentRegistrationNotificationRepository {
    TournamentRegistrationNotification save(TournamentRegistrationNotification notification);
    TournamentRegistrationNotification getById(UUID id);
    List<TournamentRegistrationNotification> getByHostId(UUID hostId);
    List<TournamentRegistrationNotification> getByRequesterId(UUID requesterId);
    TournamentRegistrationNotification getByRegistrationId(UUID registrationId);
    void delete(UUID id);
    void markAsRead(UUID id);
    int getUnreadCountByHostId(UUID hostId);
} 