package ge.join2play.join2playback.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import ge.join2play.join2playback.model.Option;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum SportType {
    FOOTBALL("Football"),
    BASKETBALL("Basketball"),
    RUNNING("Running"),
    TENNIS("Tennis"),
    CYCLING("Cycling"),
    VOLLEYBALL("Volleyball"),
    SWIMMING("Swimming");

    private final String displayName;

    SportType(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    @Override
    public String toString() {
        return displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static SportType fromString(String displayName) {
        for (SportType sportType : SportType.values()) {
            if (sportType.displayName.equalsIgnoreCase(displayName)) {
                return sportType;
            }
        }
        throw new IllegalArgumentException("Unknown sport type: " + displayName);
    }

    public static List<Option> getSportTypeOptions() {
        return Stream.of(SportType.values())
                .map(sport -> new Option(sport.toString(), sport.name()))
                .collect(Collectors.toList());
    }
}
