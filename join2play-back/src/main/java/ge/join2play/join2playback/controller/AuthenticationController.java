package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.dto.AuthResponse;
import ge.join2play.join2playback.model.dto.SignInRequest;
import ge.join2play.join2playback.model.dto.SignUpRequest;
import ge.join2play.join2playback.model.exceptions.EmailAlreadyExistsException;
import ge.join2play.join2playback.model.exceptions.InvalidCredentialsException;
import ge.join2play.join2playback.model.exceptions.UserAgeNotInRangeException;
import ge.join2play.join2playback.service.AuthenticationService;
import ge.join2play.join2playback.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        try {
            AuthResponse response = authenticationService.signUp(signUpRequest);
            return ResponseEntity.ok(response);
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new AuthResponse(null, null, null, e.getMessage()));
        } catch (UserAgeNotInRangeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(null, null, null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(null, null, null, "Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@RequestBody SignInRequest signInRequest) {
        try {
            AuthResponse response = authenticationService.signIn(signInRequest);
            
            String token = jwtService.generateToken(response.getUserId(), response.getEmail(), response.getName());
            
            AuthResponse responseWithToken = new AuthResponse(
                response.getUserId(),
                response.getName(),
                response.getEmail(),
                response.getMessage(),
                token
            );
            
            return ResponseEntity.ok(responseWithToken);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null, null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(null, null, null, "Login failed: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // With JWT, logout is handled client-side by removing the token
        return ResponseEntity.ok("Logged out successfully");
    }
} 