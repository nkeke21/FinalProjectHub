package ge.join2play.join2playback.model;

import java.util.UUID;

public class EventRequest {
    private UUID hostId;
    private int minAge;
    private int maxAge;
    private String description;
    private long eventTime;
    private double latitude;
    private double longitude;
    private String location;
    private int numberOfParticipantsTotal;
    private int numberOfParticipantsRegistered;
    private String sportType;

    public EventRequest(UUID hostId, int minAge, int maxAge, String description, long eventTime, double latitude,
                        double longitude, String location, int numberOfParticipantsTotal, int numberOfParticipantsRegistered, String sportType) {
        this.hostId = hostId;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.description = description;
        this.eventTime = eventTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.numberOfParticipantsTotal = numberOfParticipantsTotal;
        this.numberOfParticipantsRegistered = numberOfParticipantsRegistered;
        this.sportType = sportType;
    }

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
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

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
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

    public int getNumberOfParticipantsTotal() {
        return numberOfParticipantsTotal;
    }

    public void setNumberOfParticipantsTotal(int numberOfParticipantsTotal) {
        this.numberOfParticipantsTotal = numberOfParticipantsTotal;
    }

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNumberOfParticipantsRegistered() {
        return numberOfParticipantsRegistered;
    }

    public void setNumberOfParticipantsRegistered(int numberOfParticipantsRegistered) {
        this.numberOfParticipantsRegistered = numberOfParticipantsRegistered;
    }
}
