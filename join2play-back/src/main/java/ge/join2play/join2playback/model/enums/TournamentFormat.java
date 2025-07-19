package ge.join2play.join2playback.model.enums;

public enum TournamentFormat {
    SINGLE_ELIMINATION("Single Elimination"),
    DOUBLE_ELIMINATION("Double Elimination"),
    ROUND_ROBIN("Round Robin"),
    SWISS_SYSTEM("Swiss System");

    private final String displayName;

    TournamentFormat(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 