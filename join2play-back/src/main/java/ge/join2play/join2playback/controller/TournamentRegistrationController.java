package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.dto.TournamentRegistrationRequest;
import ge.join2play.join2playback.model.dto.TournamentRegistrationResponse;
import ge.join2play.join2playback.service.TournamentRegistrationService;
import ge.join2play.join2playback.util.JwtUtil;
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
    private final JwtUtil jwtUtil;

    @Autowired
    public TournamentRegistrationController(TournamentRegistrationService registrationService, JwtUtil jwtUtil) {
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<TournamentRegistrationResponse> registerForTournament(
            @RequestBody TournamentRegistrationRequest request) {
        try {
            return jwtUtil.getCurrentUserId()
                    .map(userId -> {
                        try {
                            TournamentRegistrationResponse response = registrationService.registerForTournament(request, userId);
                            return ResponseEntity.ok(response);
                        } catch (Exception e) {
                            return ResponseEntity.<TournamentRegistrationResponse>badRequest().build();
                        }
                    })
                    .orElse(ResponseEntity.<TournamentRegistrationResponse>status(401).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{tournamentId}/withdraw")
    public ResponseEntity<TournamentRegistrationResponse> withdrawFromTournament(
            @PathVariable UUID tournamentId) {
        try {
            return jwtUtil.getCurrentUserId()
                    .map(userId -> {
                        try {
                            TournamentRegistrationResponse response = registrationService.withdrawFromTournament(tournamentId, userId);
                            return ResponseEntity.ok(response);
                        } catch (Exception e) {
                            return ResponseEntity.<TournamentRegistrationResponse>badRequest().build();
                        }
                    })
                    .orElse(ResponseEntity.<TournamentRegistrationResponse>status(401).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<TournamentRegistrationResponse> getUserRegistration(
            @PathVariable UUID tournamentId) {
        try {
            return jwtUtil.getCurrentUserId()
                    .map(userId -> {
                        try {
                            TournamentRegistrationResponse response = registrationService.getUserRegistration(tournamentId, userId);
                            if (response == null) {
                                return ResponseEntity.<TournamentRegistrationResponse>notFound().build();
                            }
                            return ResponseEntity.ok(response);
                        } catch (Exception e) {
                            return ResponseEntity.<TournamentRegistrationResponse>badRequest().build();
                        }
                    })
                    .orElse(ResponseEntity.<TournamentRegistrationResponse>status(401).build());
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
        try {
            return jwtUtil.getCurrentUserId()
                    .map(userId -> {
                        try {
                            List<TournamentRegistrationResponse> response = registrationService.getUserRegistrations(userId);
                            return ResponseEntity.ok(response);
                        } catch (Exception e) {
                            return ResponseEntity.<List<TournamentRegistrationResponse>>badRequest().build();
                        }
                    })
                    .orElse(ResponseEntity.<List<TournamentRegistrationResponse>>status(401).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{registrationId}")
    public ResponseEntity<TournamentRegistrationResponse> getRegistrationById(
            @PathVariable UUID registrationId) {
        try {
            return jwtUtil.getCurrentUserId()
                    .map(userId -> {
                        try {
                            TournamentRegistrationResponse response = registrationService.getRegistrationById(registrationId);
                            if (response == null) {
                                return ResponseEntity.<TournamentRegistrationResponse>notFound().build();
                            }
                            return ResponseEntity.ok(response);
                        } catch (Exception e) {
                            return ResponseEntity.<TournamentRegistrationResponse>badRequest().build();
                        }
                    })
                    .orElse(ResponseEntity.<TournamentRegistrationResponse>status(401).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{registrationId}/approve")
    public ResponseEntity<TournamentRegistrationResponse> approveRegistration(
            @PathVariable UUID registrationId) {
        try {
            return jwtUtil.getCurrentUserId()
                    .map(userId -> {
                        try {
                            TournamentRegistrationResponse response = registrationService.approveRegistration(registrationId, userId);
                            return ResponseEntity.ok(response);
                        } catch (Exception e) {
                            return ResponseEntity.<TournamentRegistrationResponse>badRequest().build();
                        }
                    })
                    .orElse(ResponseEntity.<TournamentRegistrationResponse>status(401).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{registrationId}/reject")
    public ResponseEntity<TournamentRegistrationResponse> rejectRegistration(
            @PathVariable UUID registrationId) {
        try {
            return jwtUtil.getCurrentUserId()
                    .map(userId -> {
                        try {
                            TournamentRegistrationResponse response = registrationService.rejectRegistration(registrationId, userId);
                            return ResponseEntity.ok(response);
                        } catch (Exception e) {
                            return ResponseEntity.<TournamentRegistrationResponse>badRequest().build();
                        }
                    })
                    .orElse(ResponseEntity.<TournamentRegistrationResponse>status(401).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/tournament/{tournamentId}/can-register")
    public ResponseEntity<Boolean> canRegisterForTournament(
            @PathVariable UUID tournamentId) {
        try {
            return jwtUtil.getCurrentUserId()
                    .map(userId -> {
                        try {
                            boolean canRegister = registrationService.canRegisterForTournament(tournamentId, userId);
                            return ResponseEntity.ok(canRegister);
                        } catch (Exception e) {
                            return ResponseEntity.<Boolean>badRequest().build();
                        }
                    })
                    .orElse(ResponseEntity.<Boolean>status(401).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 