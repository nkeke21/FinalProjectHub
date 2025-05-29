package ge.join2play.join2playback.model.dto;

public class NotificationDTO {
    private String message;
    private String eventId;
    private String senderUserId;

    public NotificationDTO() {}

    public NotificationDTO(String message, String eventId, String senderUserId) {
        this.message = message;
        this.eventId = eventId;
        this.senderUserId = senderUserId;
    }
}
