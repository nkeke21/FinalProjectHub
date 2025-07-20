package ge.join2play.join2playback.service;

import ge.join2play.join2playback.model.TournamentRegistrationNotification;
import ge.join2play.join2playback.model.dto.TournamentRegistrationNotificationDTO;
import ge.join2play.join2playback.repository.TournamentRegistrationNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TournamentRegistrationNotificationService {
    private final TournamentRegistrationNotificationRepository notificationRepository;

    @Autowired
    public TournamentRegistrationNotificationService(TournamentRegistrationNotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<TournamentRegistrationNotificationDTO> getNotificationsByHostId(UUID hostId) {
        List<TournamentRegistrationNotification> notifications = notificationRepository.getByHostId(hostId);
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public int getUnreadCountByHostId(UUID hostId) {
        return notificationRepository.getUnreadCountByHostId(hostId);
    }

    public void markAsRead(UUID notificationId, UUID hostId) {
        TournamentRegistrationNotification notification = notificationRepository.getById(notificationId);
        if (notification != null && notification.getHostId().equals(hostId)) {
            notificationRepository.markAsRead(notificationId);
        }
    }

    private TournamentRegistrationNotificationDTO convertToDTO(TournamentRegistrationNotification notification) {
        return new TournamentRegistrationNotificationDTO(
            notification.getId(),
            notification.getTournamentId(),
            notification.getTournamentName(),
            notification.getRequesterId(),
            notification.getRequesterName(),
            notification.getHostId(),
            notification.getHostName(),
            notification.getRegistrationId(),
            notification.getCreatedAt(),
            notification.isRead()
        );
    }
} 