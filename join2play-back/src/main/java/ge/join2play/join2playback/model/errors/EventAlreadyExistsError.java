package ge.join2play.join2playback.model.errors;

public class EventAlreadyExistsError extends RuntimeException{
    public EventAlreadyExistsError(String message) {
        super(message);
    }
}