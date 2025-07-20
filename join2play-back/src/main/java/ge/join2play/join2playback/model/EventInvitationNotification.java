package ge.join2play.join2playback.model;

import java.time.LocalDateTime;

public class EventInvitationNotification {
    private String invitationId;
    private String eventId;
    private String fromUserId;
    private String fromUserName;
    private String eventDesctiption;
    private String eventDate;
    private String eventLocation;
    private String message;
    private LocalDateTime timestamp;

    public EventInvitationNotification() {
        this.timestamp = LocalDateTime.now();
    }

    public EventInvitationNotification(String invitationId, String eventId, String fromUserId, String fromUserName,
                                       String eventDesctiption, String eventDate, String eventLocation, String message) {
        this.invitationId = invitationId;
        this.eventId = eventId;
        this.fromUserId = fromUserId;
        this.fromUserName = fromUserName;
        this.eventDesctiption = eventDesctiption;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getInvitationId() { return invitationId; }
    public void setInvitationId(String invitationId) { this.invitationId = invitationId; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getFromUserId() { return fromUserId; }
    public void setFromUserId(String fromUserId) { this.fromUserId = fromUserId; }

    public String getFromUserName() { return fromUserName; }
    public void setFromUserName(String fromUserName) { this.fromUserName = fromUserName; }

    public String getEventDesctiption() { return eventDesctiption; }
    public void setEventDesctiption(String eventDesctiption) { this.eventDesctiption = eventDesctiption; }

    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }

    public String getEventLocation() { return eventLocation; }
    public void setEventLocation(String eventLocation) { this.eventLocation = eventLocation; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
