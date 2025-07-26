package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.dto.TournamentRegistrationRequest;
import ge.join2play.join2playback.model.dto.TournamentRegistrationResponse;
import ge.join2play.join2playback.service.TournamentRegistrationService;
import ge.join2play.join2playback.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/tournament-registrations")
@CrossOrigin(origins = "*")
public class TournamentRegistrationController {
    private final TournamentRegistrationService registrationService;
    private final JwtUtil jwtUtil;

    @Autowired
    public TournamentRegistrationController(TournamentRegistrationService registrationService, JwtUtil jwtUtil) {
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<TournamentRegistrationResponse> registerForTournament(
            @RequestBody TournamentRegistrationRequest request) {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            TournamentRegistrationResponse response = registrationService.registerForTournament(request, currentUserId.get());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{tournamentId}/withdraw")
    public ResponseEntity<TournamentRegistrationResponse> withdrawFromTournament(
            @PathVariable UUID tournamentId) {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            TournamentRegistrationResponse response = registrationService.withdrawFromTournament(tournamentId, currentUserId.get());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<TournamentRegistrationResponse> getUserRegistration(
            @PathVariable UUID tournamentId) {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            TournamentRegistrationResponse response = registrationService.getUserRegistration(tournamentId, currentUserId.get());
            if (response == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/tournament/{tournamentId}/all")
    public ResponseEntity<List<TournamentRegistrationResponse>> getTournamentRegistrations(
            @PathVariable UUID tournamentId) {
        try {
            List<TournamentRegistrationResponse> response = registrationService.getTournamentRegistrations(tournamentId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<TournamentRegistrationResponse>> getUserRegistrations() {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            List<TournamentRegistrationResponse> response = registrationService.getUserRegistrations(currentUserId.get());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{registrationId}")
    public ResponseEntity<TournamentRegistrationResponse> getRegistrationById(
            @PathVariable UUID registrationId) {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            TournamentRegistrationResponse response = registrationService.getRegistrationById(registrationId);
            if (response == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{registrationId}/approve")
    public ResponseEntity<TournamentRegistrationResponse> approveRegistration(
            @PathVariable UUID registrationId) {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            TournamentRegistrationResponse response = registrationService.approveRegistration(registrationId, currentUserId.get());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{registrationId}/reject")
    public ResponseEntity<TournamentRegistrationResponse> rejectRegistration(
            @PathVariable UUID registrationId) {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            TournamentRegistrationResponse response = registrationService.rejectRegistration(registrationId, currentUserId.get());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/tournament/{tournamentId}/can-register")
    public ResponseEntity<Boolean> canRegisterForTournament(
            @PathVariable UUID tournamentId) {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            boolean canRegister = registrationService.canRegisterForTournament(tournamentId, currentUserId.get());
            return ResponseEntity.ok(canRegister);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 