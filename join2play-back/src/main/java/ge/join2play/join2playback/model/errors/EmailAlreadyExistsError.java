package ge.join2play.join2playback.model.errors;

public class EmailAlreadyExistsError extends RuntimeException {
    public EmailAlreadyExistsError(String message) {
        super(message);
    }
} 