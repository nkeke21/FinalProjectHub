package ge.join2play.join2playback.model.errors;

public class EventParticipantDoesNotExistError extends RuntimeException{
    public EventParticipantDoesNotExistError(String message) {
        super(message);
    }
}
