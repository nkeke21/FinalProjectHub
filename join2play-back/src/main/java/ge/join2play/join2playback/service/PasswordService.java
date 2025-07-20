package ge.join2play.join2playback.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    
    private final PasswordEncoder passwordEncoder;
    
    public PasswordService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    public String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }
    
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }
    
    public boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpperCase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowerCase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        
        return hasUpperCase && hasLowerCase && hasDigit;
    }
} 