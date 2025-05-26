package ge.join2play.join2playback.model.errors;

public class SportTypeDoesNotExist extends RuntimeException{
    public SportTypeDoesNotExist(String message) {
        super(message);
    }
}
