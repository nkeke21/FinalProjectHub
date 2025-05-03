package ge.join2play.join2playback.model.errors;

public class EventDoesNotExistError extends RuntimeException{
    public EventDoesNotExistError(String message) {
        super(message);
    }
}
