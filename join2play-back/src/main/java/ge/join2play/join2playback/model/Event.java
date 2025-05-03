package ge.join2play.join2playback.model;

import java.util.UUID;
import java.time.Instant;


public class Event {
    private UUID id;
    private int minAge;
    private int maxAge;
    private String description;
    private Instant eventTime;
    private double latitude;
    private double longitude;
    private int numberOfParticipants;
    private String sportType;

    public Event(UUID id, int minAge, int maxAge, String description, Instant eventTime,
                 double latitude, double longitude, int numberOfParticipants, String sportType) {
        this.id = id;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.description = description;
        this.eventTime = eventTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.numberOfParticipants = numberOfParticipants;
        this.sportType = sportType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getEventTime() {
        return eventTime;
    }

    public void setEventTime(Instant eventTime) {
        this.eventTime = eventTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }
}
