package ge.join2play.join2playback.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RegistrationStatus {
    PENDING("PENDING"),
    REGISTERED("REGISTERED"),
    WITHDRAWN("WITHDRAWN");

    private final String value;

    RegistrationStatus(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return value;
    }

    @JsonCreator
    public static RegistrationStatus fromString(String value) {
        for (RegistrationStatus status : RegistrationStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown registration status: " + value);
    }
} 