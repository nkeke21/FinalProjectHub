package ge.join2play.join2playback.model.dto;

import java.util.UUID;

public class TournamentRegistrationRequest {
    private UUID tournamentId;
    private String registrationType;
    private UUID teamId;
    private ParticipantInfo participantInfo;

    public TournamentRegistrationRequest() {
    }

    public TournamentRegistrationRequest(UUID tournamentId, String registrationType, UUID teamId, ParticipantInfo participantInfo) {
        this.tournamentId = tournamentId;
        this.registrationType = registrationType;
        this.teamId = teamId;
        this.participantInfo = participantInfo;
    }

    public UUID getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(UUID tournamentId) {
        this.tournamentId = tournamentId;
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

    public ParticipantInfo getParticipantInfo() {
        return participantInfo;
    }

    public void setParticipantInfo(ParticipantInfo participantInfo) {
        this.participantInfo = participantInfo;
    }

    public static class ParticipantInfo {
        private String fullName;
        private Integer age;
        private String email;
        private String phoneNumber;
        private String address;
        private EmergencyContact emergencyContact;
        private String previousExperience;
        private String skillLevel;
        private String previousAchievements;

        public ParticipantInfo() {
            // Default constructor for Spring
        }

        public ParticipantInfo(String fullName, Integer age, String email, String phoneNumber, String address,
                              EmergencyContact emergencyContact, String previousExperience, String skillLevel, String previousAchievements) {
            this.fullName = fullName;
            this.age = age;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.address = address;
            this.emergencyContact = emergencyContact;
            this.previousExperience = previousExperience;
            this.skillLevel = skillLevel;
            this.previousAchievements = previousAchievements;
        }

        // Getters and Setters
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

        public EmergencyContact getEmergencyContact() {
            return emergencyContact;
        }

        public void setEmergencyContact(EmergencyContact emergencyContact) {
            this.emergencyContact = emergencyContact;
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

    public static class EmergencyContact {
        private String name;
        private String relationship;
        private String phone;
        private String email;

        public EmergencyContact() {
            // Default constructor for Spring
        }

        public EmergencyContact(String name, String relationship, String phone, String email) {
            this.name = name;
            this.relationship = relationship;
            this.phone = phone;
            this.email = email;
        }

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRelationship() {
            return relationship;
        }

        public void setRelationship(String relationship) {
            this.relationship = relationship;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
} 