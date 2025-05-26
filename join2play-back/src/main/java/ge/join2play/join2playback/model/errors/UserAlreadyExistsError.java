package ge.join2play.join2playback.model.errors;

public class UserAlreadyExistsError extends RuntimeException{
    public UserAlreadyExistsError(String message) {
        super(message);
    }
}
