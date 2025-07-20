package ge.join2play.join2playback.model.dto;

public class PermissionRequest {
    private String grantedBy;
    private String reason;

    public PermissionRequest() {
        // Default constructor for Spring
    }

    public PermissionRequest(String grantedBy, String reason) {
        this.grantedBy = grantedBy;
        this.reason = reason;
    }

    // Getters and Setters
    public String getGrantedBy() {
        return grantedBy;
    }

    public void setGrantedBy(String grantedBy) {
        this.grantedBy = grantedBy;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
} 