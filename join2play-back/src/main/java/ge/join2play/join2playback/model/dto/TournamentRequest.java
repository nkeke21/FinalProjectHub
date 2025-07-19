package ge.join2play.join2playback.model.dto;

import ge.join2play.join2playback.model.enums.SportType;
import ge.join2play.join2playback.model.enums.TournamentFormat;

import java.util.List;

public class TournamentRequest {
    private String name;
    private String description;
    private SportType sportType;
    private TournamentFormat format;
    private String tournamentType; // "individual" or "team"
    private String location;
    private double latitude;
    private double longitude;
    private long startDate;
    private long endDate;
    private long registrationDeadline;
    private int maxParticipants;
    private double entryFee;
    private double prizePool;
    private int minAge;
    private int maxAge;
    private List<String> rules;

    public TournamentRequest() {
        // Default constructor for Spring
    }

    public TournamentRequest(String name, String description, SportType sportType, 
                           TournamentFormat format, String tournamentType, String location,
                           double latitude, double longitude, long startDate, long endDate,
                           long registrationDeadline, int maxParticipants, double entryFee,
                           double prizePool, int minAge, int maxAge, List<String> rules) {
        this.name = name;
        this.description = description;
        this.sportType = sportType;
        this.format = format;
        this.tournamentType = tournamentType;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startDate = startDate;
        this.endDate = endDate;
        this.registrationDeadline = registrationDeadline;
        this.maxParticipants = maxParticipants;
        this.entryFee = entryFee;
        this.prizePool = prizePool;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.rules = rules;
    }

    // Getters and Setters
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

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public long getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(long registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
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
} 