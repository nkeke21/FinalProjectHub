package ge.join2play.join2playback.model.errors;

public class UserDoesNotExistError extends RuntimeException{
    public UserDoesNotExistError(String message) {
        super(message);
    }
}
