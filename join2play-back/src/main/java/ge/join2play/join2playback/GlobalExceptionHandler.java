package ge.join2play.join2playback;

import ge.join2play.join2playback.model.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handleUserDoesNotExist(UserDoesNotExistException ex) {
        logger.warn("User not found: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "USER_NOT_FOUND",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        logger.warn("User already exists: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "USER_ALREADY_EXISTS",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(UserAgeNotInRangeException.class)
    public ResponseEntity<ErrorResponse> handleUserAgeNotInRange(UserAgeNotInRangeException ex) {
        logger.warn("User age not in range: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "USER_AGE_NOT_IN_RANGE",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(UserIsNotFriendException.class)
    public ResponseEntity<ErrorResponse> handleUserIsNotFriend(UserIsNotFriendException ex) {
        logger.warn("User is not friend: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "USER_IS_NOT_FRIEND",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        logger.warn("Email already exists: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "EMAIL_ALREADY_EXISTS",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(EventDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handleEventDoesNotExist(EventDoesNotExistException ex) {
        logger.warn("Event not found: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "EVENT_NOT_FOUND",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EventAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEventAlreadyExists(EventAlreadyExistsException ex) {
        logger.warn("Event already exists: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "EVENT_ALREADY_EXISTS",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(EventInvitationDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handleEventInvitationDoesNotExist(EventInvitationDoesNotExistException ex) {
        logger.warn("Event invitation not found: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "EVENT_INVITATION_NOT_FOUND",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EventInvitationAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEventInvitationAlreadyExists(EventInvitationAlreadyExistsException ex) {
        logger.warn("Event invitation already exists: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "EVENT_INVITATION_ALREADY_EXISTS",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(EventParticipantDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handleEventParticipantDoesNotExist(EventParticipantDoesNotExistException ex) {
        logger.warn("Event participant not found: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "EVENT_PARTICIPANT_NOT_FOUND",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EventParticipantAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEventParticipantAlreadyExists(EventParticipantAlreadyExistsException ex) {
        logger.warn("Event participant already exists: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "EVENT_PARTICIPANT_ALREADY_EXISTS",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(FriendRequestDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handleFriendRequestDoesNotExist(FriendRequestDoesNotExistException ex) {
        logger.warn("Friend request not found: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "FRIEND_REQUEST_NOT_FOUND",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(FriendRequestAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleFriendRequestAlreadyExists(FriendRequestAlreadyExistsException ex) {
        logger.warn("Friend request already exists: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "FRIEND_REQUEST_ALREADY_EXISTS",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(SelfFriendRequestException.class)
    public ResponseEntity<ErrorResponse> handleSelfFriendRequest(SelfFriendRequestException ex) {
        logger.warn("Self friend request attempted: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "SELF_FRIEND_REQUEST",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(TeamDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handleTeamDoesNotExist(TeamDoesNotExistException ex) {
        logger.warn("Team not found: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "TEAM_NOT_FOUND",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(TeamAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleTeamAlreadyExists(TeamAlreadyExistsException ex) {
        logger.warn("Team already exists: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "TEAM_ALREADY_EXISTS",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(TeamMemberDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handleTeamMemberDoesNotExist(TeamMemberDoesNotExistException ex) {
        logger.warn("Team member not found: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "TEAM_MEMBER_NOT_FOUND",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(TeamMemberAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleTeamMemberAlreadyExists(TeamMemberAlreadyExistsException ex) {
        logger.warn("Team member already exists: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "TEAM_MEMBER_ALREADY_EXISTS",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(TeamJoinRequestDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handleTeamJoinRequestDoesNotExist(TeamJoinRequestDoesNotExistException ex) {
        logger.warn("Team join request not found: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "TEAM_JOIN_REQUEST_NOT_FOUND",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(TeamJoinRequestAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleTeamJoinRequestAlreadyExists(TeamJoinRequestAlreadyExistsException ex) {
        logger.warn("Team join request already exists: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "TEAM_JOIN_REQUEST_ALREADY_EXISTS",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(SportTypeDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handleSportTypeDoesNotExist(SportTypeDoesNotExistException ex) {
        logger.warn("Sport type not found: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "SPORT_TYPE_NOT_FOUND",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidEventDateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEventDate(InvalidEventDateException ex) {
        logger.warn("Invalid event date: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "INVALID_EVENT_DATE",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {
        logger.warn("Unauthorized access: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "UNAUTHORIZED",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        logger.warn("Invalid credentials: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "INVALID_CREDENTIALS",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        logger.warn("Invalid argument: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "INVALID_ARGUMENT",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex) {
        logger.warn("Invalid state: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "INVALID_STATE",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        logger.warn("Validation failed: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                "VALIDATION_FAILED",
                "Request validation failed",
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        logger.warn("Constraint violation: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "CONSTRAINT_VIOLATION",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        logger.warn("Type mismatch: {}", ex.getMessage());
        String message = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s",
                ex.getValue(), ex.getName(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName());

        ErrorResponse error = new ErrorResponse(
                "TYPE_MISMATCH",
                message
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.error("Unexpected error occurred", ex);
        ErrorResponse error = new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred. Please try again later."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    public static class ErrorResponse {
        private String code;
        private String message;

        public ErrorResponse(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }

    public static class ValidationErrorResponse extends ErrorResponse {
        private Map<String, String> fieldErrors;

        public ValidationErrorResponse(String code, String message, Map<String, String> fieldErrors) {
            super(code, message);
            this.fieldErrors = fieldErrors;
        }

        public Map<String, String> getFieldErrors() {
            return fieldErrors;
        }

        public void setFieldErrors(Map<String, String> fieldErrors) {
            this.fieldErrors = fieldErrors;
        }
    }
}