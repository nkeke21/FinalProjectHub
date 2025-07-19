package ge.join2play.join2playback.model.dto;

import ge.join2play.join2playback.model.enums.SportType;

public class TeamRequest {
    private String name;
    private String description;
    private SportType sportType;
    private int maxMembers;
    private int minAge;
    private int maxAge;

    public TeamRequest() {
    }

    public TeamRequest(String name, String description, SportType sportType, int maxMembers, 
                      int minAge, int maxAge) {
        this.name = name;
        this.description = description;
        this.sportType = sportType;
        this.maxMembers = maxMembers;
        this.minAge = minAge;
        this.maxAge = maxAge;
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

    public int getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
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
} 