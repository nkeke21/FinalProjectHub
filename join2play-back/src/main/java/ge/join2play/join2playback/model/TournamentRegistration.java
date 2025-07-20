package ge.join2play.join2playback.model;

import ge.join2play.join2playback.model.enums.RegistrationStatus;

import java.time.Instant;
import java.util.UUID;

public class TournamentRegistration {
    private UUID id;
    private UUID tournamentId;
    private UUID userId;
    private String registrationType;
    private UUID teamId;
    private RegistrationStatus status;
    private Instant registeredAt;
    private Instant updatedAt;
    
    private String fullName;
    private Integer age;
    private String email;
    private String phoneNumber;
    private String address;
    private String emergencyContactName;
    private String emergencyContactRelationship;
    private String emergencyContactPhone;
    private String emergencyContactEmail;
    private String previousExperience;
    private String skillLevel;
    private String previousAchievements;

    public TournamentRegistration() {
    }

    public TournamentRegistration(UUID id, UUID tournamentId, UUID userId, String registrationType, 
                                UUID teamId, RegistrationStatus status, Instant registeredAt, Instant updatedAt,
                                String fullName, Integer age, String email, String phoneNumber, String address,
                                String emergencyContactName, String emergencyContactRelationship, 
                                String emergencyContactPhone, String emergencyContactEmail,
                                String previousExperience, String skillLevel, String previousAchievements) {
        this.id = id;
        this.tournamentId = tournamentId;
        this.userId = userId;
        this.registrationType = registrationType;
        this.teamId = teamId;
        this.status = status;
        this.registeredAt = registeredAt;
        this.updatedAt = updatedAt;
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactRelationship = emergencyContactRelationship;
        this.emergencyContactPhone = emergencyContactPhone;
        this.emergencyContactEmail = emergencyContactEmail;
        this.previousExperience = previousExperience;
        this.skillLevel = skillLevel;
        this.previousAchievements = previousAchievements;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(UUID tournamentId) {
        this.tournamentId = tournamentId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public void setTeamId(UUID teamId) {
        this.teamId = teamId;
    }

    public RegistrationStatus getStatus() {
        return status;
    }

    public void setStatus(RegistrationStatus status) {
        this.status = status;
    }

    public Instant getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Instant registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactRelationship() {
        return emergencyContactRelationship;
    }

    public void setEmergencyContactRelationship(String emergencyContactRelationship) {
        this.emergencyContactRelationship = emergencyContactRelationship;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public String getEmergencyContactEmail() {
        return emergencyContactEmail;
    }

    public void setEmergencyContactEmail(String emergencyContactEmail) {
        this.emergencyContactEmail = emergencyContactEmail;
    }

    public String getPreviousExperience() {
        return previousExperience;
    }

    public void setPreviousExperience(String previousExperience) {
        this.previousExperience = previousExperience;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    public String getPreviousAchievements() {
        return previousAchievements;
    }

    public void setPreviousAchievements(String previousAchievements) {
        this.previousAchievements = previousAchievements;
    }
} 