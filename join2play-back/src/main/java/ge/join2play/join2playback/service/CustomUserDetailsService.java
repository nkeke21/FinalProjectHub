package ge.join2play.join2playback.service;

import ge.join2play.join2playback.model.User;
import ge.join2play.join2playback.repository.interfaces.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // Try to parse as UUID first (for JWT tokens)
            UUID userId = UUID.fromString(username);
            User user = userRepository.getById(userId).orElse(null);
            
            if (user == null) {
                throw new UsernameNotFoundException("User not found with id: " + username);
            }
            
            return createUserDetails(user);
        } catch (IllegalArgumentException e) {
            // If not a UUID, try to find by email or name
            User user = userRepository.findByEmailOrName(username);
            
            if (user == null) {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
            
            return createUserDetails(user);
        }
    }

    private UserDetails createUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getId().toString())
                .password(user.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("USER")))
                .build();
    }
} 