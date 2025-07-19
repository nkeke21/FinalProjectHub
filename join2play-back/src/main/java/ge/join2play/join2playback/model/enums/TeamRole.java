package ge.join2play.join2playback.model.enums;

public enum TeamRole {
    CAPTAIN("Captain"),
    MEMBER("Member"),
    INVITED("Invited");

    private final String displayName;

    TeamRole(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 