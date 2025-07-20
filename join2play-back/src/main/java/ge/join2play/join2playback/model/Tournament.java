package ge.join2play.join2playback.model;

import ge.join2play.join2playback.model.enums.SportType;
import ge.join2play.join2playback.model.enums.TournamentFormat;
import ge.join2play.join2playback.model.enums.TournamentStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Tournament {
    private UUID id;
    private String name;
    private String description;
    private SportType sportType;
    private TournamentFormat format;
    private String tournamentType; // "individual" or "team"
    private TournamentStatus status;
    private UUID hostId;
    private String hostName;
    private String location;
    private double latitude;
    private double longitude;
    private Instant startDate;
    private Instant endDate;
    private Instant registrationDeadline;
    private int maxParticipants;
    private int currentParticipants;
    private double entryFee;
    private double prizePool;
    private int minAge;
    private int maxAge;
    private List<String> rules;
    private Instant createdAt;
    private Instant updatedAt;

    public Tournament() {
        // Default constructor for Spring
    }

    public Tournament(UUID id, String name, String description, SportType sportType, 
                     TournamentFormat format, String tournamentType,
                     UUID hostId, String hostName, String location, double latitude, double longitude,
                     Instant startDate, Instant endDate, Instant registrationDeadline,
                     int maxParticipants, int currentParticipants, double entryFee, double prizePool,
                     int minAge, int maxAge, List<String> rules, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sportType = sportType;
        this.format = format;
        this.tournamentType = tournamentType;
        this.status = status;
        this.hostId = hostId;
        this.hostName = hostName;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startDate = startDate;
        this.endDate = endDate;
        this.registrationDeadline = registrationDeadline;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = currentParticipants;
        this.entryFee = entryFee;
        this.prizePool = prizePool;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.rules = rules;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SportType getSportType() {
        return sportType;
    }

    public void setSportType(SportType sportType) {
        this.sportType = sportType;
    }

    public TournamentFormat getFormat() {
        return format;
    }

    public void setFormat(TournamentFormat format) {
        this.format = format;
    }

    public String getTournamentType() {
        return tournamentType;
    }

    public void setTournamentType(String tournamentType) {
        this.tournamentType = tournamentType;
    }

    public TournamentStatus getStatus() {
        return status;
    }

    public void setStatus(TournamentStatus status) {
        this.status = status;
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

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Instant getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(Instant registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public int getCurrentParticipants() {
        return currentParticipants;
    }

    public void setCurrentParticipants(int currentParticipants) {
        this.currentParticipants = currentParticipants;
    }

    public double getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(double entryFee) {
        this.entryFee = entryFee;
    }

    public double getPrizePool() {
        return prizePool;
    }

    public void setPrizePool(double prizePool) {
        this.prizePool = prizePool;
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

    public List<String> getRules() {
        return rules;
    }

    public void setRules(List<String> rules) {
        this.rules = rules;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
} 