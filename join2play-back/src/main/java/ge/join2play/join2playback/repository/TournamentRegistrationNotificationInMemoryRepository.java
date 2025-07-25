package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.TournamentRegistrationNotification;
import ge.join2play.join2playback.repository.interfaces.TournamentRegistrationNotificationRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "memory", matchIfMissing = true)
public class TournamentRegistrationNotificationInMemoryRepository implements TournamentRegistrationNotificationRepository {
    private final Map<UUID, TournamentRegistrationNotification> notifications = new ConcurrentHashMap<>();

    @Override
    public TournamentRegistrationNotification save(TournamentRegistrationNotification notification) {
        notifications.put(notification.getId(), notification);
        return notification;
    }

    @Override
    public TournamentRegistrationNotification getById(UUID id) {
        return notifications.get(id);
    }

    @Override
    public List<TournamentRegistrationNotification> getByHostId(UUID hostId) {
        return notifications.values().stream()
                .filter(notification -> notification.getHostId().equals(hostId))
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TournamentRegistrationNotification> getByRequesterId(UUID requesterId) {
        return notifications.values().stream()
                .filter(notification -> notification.getRequesterId().equals(requesterId))
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    public TournamentRegistrationNotification getByRegistrationId(UUID registrationId) {
        return notifications.values().stream()
                .filter(notification -> notification.getRegistrationId().equals(registrationId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void delete(UUID id) {
        notifications.remove(id);
    }

    @Override
    public void markAsRead(UUID id) {
        TournamentRegistrationNotification notification = notifications.get(id);
        if (notification != null) {
            notification.setRead(true);
            notifications.put(id, notification);
        }
    }

    @Override
    public int getUnreadCountByHostId(UUID hostId) {
        return (int) notifications.values().stream()
                .filter(notification -> notification.getHostId().equals(hostId) && !notification.isRead())
                .count();
    }
} 