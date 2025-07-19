package ge.join2play.join2playback.model.dto;

import ge.join2play.join2playback.model.enums.SportType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class TeamResponse {
    private UUID id;
    private String name;
    private String description;
    private SportType sportType;
    private UUID captainId;
    private String captainName;
    private List<TeamMemberResponse> members;
    private int maxMembers;
    private boolean isPublic;
    private int minAge;
    private int maxAge;
    private Instant createdAt;
    private Instant updatedAt;

    public TeamResponse() {
    }

    public TeamResponse(UUID id, String name, String description, SportType sportType, UUID captainId, 
                       String captainName, List<TeamMemberResponse> members, int maxMembers, 
                       boolean isPublic, int minAge, int maxAge, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sportType = sportType;
        this.captainId = captainId;
        this.captainName = captainName;
        this.members = members;
        this.maxMembers = maxMembers;
        this.isPublic = isPublic;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

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

    public UUID getCaptainId() {
        return captainId;
    }

    public void setCaptainId(UUID captainId) {
        this.captainId = captainId;
    }

    public String getCaptainName() {
        return captainName;
    }

    public void setCaptainName(String captainName) {
        this.captainName = captainName;
    }

    public List<TeamMemberResponse> getMembers() {
        return members;
    }

    public void setMembers(List<TeamMemberResponse> members) {
        this.members = members;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
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