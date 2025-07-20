package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.User;
import ge.join2play.join2playback.model.dto.TournamentRegistrationRequest;
import ge.join2play.join2playback.model.dto.TournamentRegistrationResponse;
import ge.join2play.join2playback.service.TournamentRegistrationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tournament-registrations")
@CrossOrigin(origins = "*")
public class TournamentRegistrationController {
    private final TournamentRegistrationService registrationService;

    @Autowired
    public TournamentRegistrationController(TournamentRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<TournamentRegistrationResponse> registerForTournament(
            @RequestBody TournamentRegistrationRequest request,
            HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(401).build(); // Unauthorized
            }
            
            TournamentRegistrationResponse response = registrationService.registerForTournament(request, currentUser.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{registrationId}/approve")
    public ResponseEntity<TournamentRegistrationResponse> approveRegistration(
            @PathVariable UUID registrationId,
            HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(401).build(); // Unauthorized
            }
            
            TournamentRegistrationResponse response = registrationService.approveRegistration(registrationId, currentUser.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{registrationId}/reject")
    public ResponseEntity<TournamentRegistrationResponse> rejectRegistration(
            @PathVariable UUID registrationId,
            HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(401).build(); // Unauthorized
            }
            
            TournamentRegistrationResponse response = registrationService.rejectRegistration(registrationId, currentUser.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{tournamentId}/withdraw")
    public ResponseEntity<TournamentRegistrationResponse> withdrawFromTournament(
            @PathVariable UUID tournamentId,
            HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(401).build(); // Unauthorized
            }
            
            TournamentRegistrationResponse response = registrationService.withdrawFromTournament(tournamentId, currentUser.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<TournamentRegistrationResponse> getUserRegistration(
            @PathVariable UUID tournamentId,
            HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(401).build(); // Unauthorized
            }
            
            TournamentRegistrationResponse response = registrationService.getUserRegistration(tournamentId, currentUser.getId());
            if (response == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/tournament/{tournamentId}/all")
    public ResponseEntity<List<TournamentRegistrationResponse>> getTournamentRegistrations(
            @PathVariable UUID tournamentId) {
        try {
            List<TournamentRegistrationResponse> response = registrationService.getTournamentRegistrations(tournamentId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<TournamentRegistrationResponse>> getUserRegistrations(HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(401).build();
            }
            
            List<TournamentRegistrationResponse> response = registrationService.getUserRegistrations(currentUser.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/tournament/{tournamentId}/can-register")
    public ResponseEntity<Boolean> canRegisterForTournament(
            @PathVariable UUID tournamentId,
            HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(401).build(); // Unauthorized
            }
            
            boolean canRegister = registrationService.canRegisterForTournament(tournamentId, currentUser.getId());
            return ResponseEntity.ok(canRegister);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{registrationId}")
    public ResponseEntity<TournamentRegistrationResponse> getRegistrationById(
            @PathVariable UUID registrationId,
            HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(401).build();
            }
            
            TournamentRegistrationResponse response = registrationService.getRegistrationById(registrationId);
            if (response == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
} 