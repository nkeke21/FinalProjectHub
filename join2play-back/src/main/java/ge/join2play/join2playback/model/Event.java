package ge.join2play.join2playback.model;

import ge.join2play.join2playback.model.enums.SportType;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "events")
public class Event {
    @Id
    private UUID id;

    @Column(name = "host_id", nullable = false)
    private UUID hostId;

    @Column(name = "host_email", nullable = false, length = 320)
    private String hostEmail;

    @Column(name = "host_phone", length = 20)
    private String hostPhone;

    @Column(name = "min_age", nullable = false)
    private int minAge;

    @Column(name = "max_age", nullable = false)
    private int maxAge;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "event_time", nullable = false)
    private Instant eventTime;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false, length = 500)
    private String location;

    @Column(name = "number_of_participants_total", nullable = false)
    private int numberOfParticipantsTotal;

    @Column(name = "number_of_participants_registered", nullable = false)
    private int numberOfParticipantsRegistered;

    @Enumerated(EnumType.STRING)
    @Column(name = "sport_type", nullable = false, length = 50)
    private SportType sportType;

    // Default constructor for JPA
    public Event() {}

    public Event(UUID id, UUID hostId, String hostEmail, String hostPhone, int minAge, int maxAge, String description, Instant eventTime,
                 double latitude, double longitude, String location, int numberOfParticipantsTotal, int numberOfParticipantsRegistered, SportType sportType) {
        this.id = id;
        this.hostId = hostId;
        this.hostEmail = hostEmail;
        this.hostPhone = hostPhone;
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

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }

    public String getHostEmail() {
        return hostEmail;
    }

    public void setHostEmail(String hostEmail) {
        this.hostEmail = hostEmail;
    }

    public String getHostPhone() {
        return hostPhone;
    }

    public void setHostPhone(String hostPhone) {
        this.hostPhone = hostPhone;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNumberOfParticipantsTotal() {
        return numberOfParticipantsTotal;
    }

    public void setNumberOfParticipantsTotal(int numberOfParticipantsTotal) {
        this.numberOfParticipantsTotal = numberOfParticipantsTotal;
    }

    public int getNumberOfParticipantsRegistered() {
        return numberOfParticipantsRegistered;
    }

    public void setNumberOfParticipantsRegistered(int numberOfParticipantsRegistered) {
        this.numberOfParticipantsRegistered = numberOfParticipantsRegistered;
    }

    public SportType getSportType() {
        return sportType;
    }

    public void setSportType(SportType sportType) {
        this.sportType = sportType;
    }
}