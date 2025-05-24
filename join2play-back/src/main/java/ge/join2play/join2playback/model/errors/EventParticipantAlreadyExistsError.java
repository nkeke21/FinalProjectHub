package ge.join2play.join2playback.model.errors;

public class EventParticipantAlreadyExistsError extends RuntimeException{
    public EventParticipantAlreadyExistsError(String message) {
        super(message);
    }
}
