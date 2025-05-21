package ge.join2play.join2playback.model;

import java.util.UUID;

public class EventResponse {
    private UUID eventId;
    private UUID hostId;
    private String hostName;
    private String ageRange;
    private String description;
    private long eventTime;
    private double latitude;
    private double longitude;
    private String location;
    private int numberOfParticipantsTotal;
    private int numberOfParticipantsRegistered;
    private String sportType;

    public EventResponse(UUID eventId, UUID hostId, String hostName, String ageRange, String description, long eventTime, double latitude,
                         double longitude, String location, int numberOfParticipantsTotal, int numberOfParticipantsRegistered, String sportType) {
        this.eventId = eventId;
        this.hostId = hostId;
        this.hostName = hostName;
        this.ageRange = ageRange;
        this.description = description;
        this.eventTime = eventTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.numberOfParticipantsTotal = numberOfParticipantsTotal;
        this.numberOfParticipantsRegistered = numberOfParticipantsRegistered;
        this.sportType = sportType;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
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

    public int getNumberOfParticipantsRegistered() {
        return numberOfParticipantsRegistered;
    }

    public void setNumberOfParticipantsRegistered(int numberOfParticipantsRegistered) {
        this.numberOfParticipantsRegistered = numberOfParticipantsRegistered;
    }

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
