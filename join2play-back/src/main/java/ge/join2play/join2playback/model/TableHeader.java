package ge.join2play.join2playback.model;

public class TableHeader {
    private String title;
    private String key;

    public TableHeader(String title, String key) {
        this.title = title;
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
