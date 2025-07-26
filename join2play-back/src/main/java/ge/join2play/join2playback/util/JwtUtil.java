package ge.join2play.join2playback.util;

import ge.join2play.join2playback.model.User;
import ge.join2play.join2playback.repository.interfaces.UserRepository;
import ge.join2play.join2playback.service.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class JwtUtil {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtUtil(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        try {
            String userId = authentication.getName();
            UUID userUuid = UUID.fromString(userId);
            return userRepository.getById(userUuid);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<UUID> getCurrentUserId() {
        return getCurrentUser().map(User::getId);
    }

    public String getCurrentUserIdString() {
        return getCurrentUserId().map(UUID::toString).orElse(null);
    }
} 