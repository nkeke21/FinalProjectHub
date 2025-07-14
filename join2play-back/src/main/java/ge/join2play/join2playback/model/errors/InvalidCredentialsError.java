package ge.join2play.join2playback.model.errors;

public class InvalidCredentialsError extends RuntimeException {
    public InvalidCredentialsError(String message) {
        super(message);
    }
} 