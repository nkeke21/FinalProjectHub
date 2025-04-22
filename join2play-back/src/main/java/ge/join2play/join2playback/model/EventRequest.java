package ge.join2play.join2playback.model;

public class EventRequest {
    private String ageRange;
    private String description;
    private long eventTime;
    private double latitude;
    private double longitude;
    private int numberOfParticipants;
    private String sportType;

    public EventRequest(String ageRange, String description, long eventTime, double latitude,
                        double longitude, int numberOfParticipants, String sportType) {
        this.ageRange = ageRange;
        this.description = description;
        this.eventTime = eventTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.numberOfParticipants = numberOfParticipants;
        this.sportType = sportType;
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
