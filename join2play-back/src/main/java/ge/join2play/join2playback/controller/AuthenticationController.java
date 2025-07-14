package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.dto.AuthResponse;
import ge.join2play.join2playback.model.dto.SignInRequest;
import ge.join2play.join2playback.model.dto.SignUpRequest;
import ge.join2play.join2playback.model.errors.EmailAlreadyExistsError;
import ge.join2play.join2playback.model.errors.InvalidCredentialsError;
import ge.join2play.join2playback.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import ge.join2play.join2playback.model.User;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        try {
            AuthResponse response = authenticationService.signUp(signUpRequest);
            return ResponseEntity.ok(response);
        } catch (EmailAlreadyExistsError e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new AuthResponse(null, null, null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(null, null, null, "Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@RequestBody SignInRequest signInRequest, HttpSession session) {
        try {
            AuthResponse response = authenticationService.signIn(signInRequest);
            User user = authenticationService.getUserByUsernameOrEmail(signInRequest.getUsername());
            session.setAttribute("user", user);
            return ResponseEntity.ok(response);
        } catch (InvalidCredentialsError e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null, null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(null, null, null, "Login failed: " + e.getMessage()));
        }
    }
} 