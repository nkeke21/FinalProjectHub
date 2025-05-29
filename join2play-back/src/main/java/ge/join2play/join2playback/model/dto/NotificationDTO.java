package ge.join2play.join2playback.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificationDTO {

    @JsonProperty("message")
    private String message;

    @JsonProperty("eventId")
    private String eventId;

    @JsonProperty("senderUserId")
    private String senderUserId;

    public NotificationDTO() {}

    public NotificationDTO(String message, String eventId, String senderUserId) {
        this.message = message;
        this.eventId = eventId;
        this.senderUserId = senderUserId;
    }

    public String getMessage() {
        return message;
    }

    public String getEventId() {
        return eventId;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }
}