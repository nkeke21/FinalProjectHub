package ge.join2play.join2playback.service;

import ge.join2play.join2playback.model.User;
import ge.join2play.join2playback.model.dto.AuthResponse;
import ge.join2play.join2playback.model.dto.SignInRequest;
import ge.join2play.join2playback.model.dto.SignUpRequest;
import ge.join2play.join2playback.model.exceptions.EmailAlreadyExistsException;
import ge.join2play.join2playback.model.exceptions.InvalidCredentialsException;
import ge.join2play.join2playback.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthResponse signUp(SignUpRequest signUpRequest) {
        User existingUser = userRepository.findByEmail(signUpRequest.getEmail());
        if (existingUser != null) {
            throw new EmailAlreadyExistsException("Email " + signUpRequest.getEmail() + " is already registered.");
        }

        User newUser = new User(
                UUID.randomUUID(),
                signUpRequest.getName(),
                signUpRequest.getEmail(),
                signUpRequest.getPhoneNumber(),
                signUpRequest.getBirthDate(),
                signUpRequest.getDescription(),
                signUpRequest.getPassword()
        );

        User savedUser = userRepository.save(newUser);

        return new AuthResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                "User registered successfully"
        );
    }

    public AuthResponse signIn(SignInRequest signInRequest) {
        User user = userRepository.findByEmailOrName(signInRequest.getUsername());
        
        if (user == null) {
            throw new InvalidCredentialsException("Invalid username or password.");
        }

        if (!user.getPassword().equals(signInRequest.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password.");
        }

        return new AuthResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                "Login successful"
        );
    }

    public User getUserByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByEmailOrName(usernameOrEmail);
    }
}