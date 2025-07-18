package ge.join2play.join2playback.model.enums;

import ge.join2play.join2playback.model.Option;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum SportType {
    FOOTBALL("Football"),
    BASKETBALL("Basketball"),
    RUNNING("Running"),
    TENNIS("Tennis"),
    CYCLING("Cycling");

    private final String displayName;

    SportType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static List<Option> getSportTypeOptions() {
        return Stream.of(SportType.values())
                .map(sport -> new Option(sport.toString(), sport.name()))
                .collect(Collectors.toList());
    }
}
