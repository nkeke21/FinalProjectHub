package ge.join2play.join2playback.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class EventInvitationResponseDTO {
    @NotBlank(message = "Invitation ID is required")
    private String invitationId;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "ACCEPTED|REJECTED", message = "Status must be ACCEPTED or REJECTED")
    private String status;

    public EventInvitationResponseDTO() {}

    public EventInvitationResponseDTO(String invitationId, String status) {
        this.invitationId = invitationId;
        this.status = status;
    }

    public String getInvitationId() { return invitationId; }
    public void setInvitationId(String invitationId) { this.invitationId = invitationId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
