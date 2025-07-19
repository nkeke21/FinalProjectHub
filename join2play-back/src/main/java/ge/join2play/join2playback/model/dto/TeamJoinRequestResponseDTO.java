package ge.join2play.join2playback.model.dto;

import java.util.UUID;

public class TeamJoinRequestResponseDTO {
    private UUID requestId;
    private String status;

    public TeamJoinRequestResponseDTO() {
    }

    public TeamJoinRequestResponseDTO(UUID requestId, String status) {
        this.requestId = requestId;
        this.status = status;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 