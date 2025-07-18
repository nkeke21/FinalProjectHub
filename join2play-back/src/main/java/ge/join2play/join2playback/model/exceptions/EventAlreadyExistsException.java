package ge.join2play.join2playback.model.exceptions;

public class EventAlreadyExistsException extends RuntimeException{
    public EventAlreadyExistsException(String message) {
        super(message);
    }
}