package ge.join2play.join2playback.model;

import java.util.UUID;

public class FriendResponseNotification {
    private UUID requestId;
    private UUID responderId;
    private String status;

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public UUID getResponderId() {
        return responderId;
    }

    public void setResponderId(UUID responderId) {
        this.responderId = responderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}