package ge.join2play.join2playback.model;

import java.util.UUID;

public class ParticipantInfo {
    private UUID userId;
    private String name;
    private int age;

    public ParticipantInfo(UUID userId, String name, int age) {
        this.userId = userId;
        this.name = name;
        this.age = age;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
} 