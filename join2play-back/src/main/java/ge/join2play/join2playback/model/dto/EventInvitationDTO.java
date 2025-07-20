package ge.join2play.join2playback.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EventInvitationDTO {
    @NotBlank(message = "Event ID is required")
    private String eventId;

    @NotBlank(message = "User ID is required")
    private String toUserId;

    @Size(max = 500, message = "Message cannot exceed 500 characters")
    private String message;

    public EventInvitationDTO() {}

    public EventInvitationDTO(String eventId, String toUserId, String message) {
        this.eventId = eventId;
        this.toUserId = toUserId;
        this.message = message;
    }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getToUserId() { return toUserId; }
    public void setToUserId(String toUserId) { this.toUserId = toUserId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}