package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.dto.TournamentRequest;
import ge.join2play.join2playback.model.dto.TournamentResponse;
import ge.join2play.join2playback.service.TournamentService;
import ge.join2play.join2playback.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tournaments")
@CrossOrigin(origins = "*")
public class TournamentController {
    private final TournamentService tournamentService;
    private final JwtUtil jwtUtil;

    @Autowired
    public TournamentController(TournamentService tournamentService, JwtUtil jwtUtil) {
        this.tournamentService = tournamentService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<TournamentResponse> createTournament(
            @RequestBody TournamentRequest request) {
        try {
            return jwtUtil.getCurrentUserId()
                    .map(userId -> {
                        try {
                            TournamentResponse response = tournamentService.createTournament(request, userId);
                            return ResponseEntity.ok(response);
                        } catch (Exception e) {
                            return ResponseEntity.<TournamentResponse>badRequest().build();
                        }
                    })
                    .orElse(ResponseEntity.<TournamentResponse>status(401).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentResponse> getTournament(@PathVariable UUID id) {
        try {
            TournamentResponse response = tournamentService.getTournament(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<TournamentResponse>> getAllTournaments() {
        try {
            List<TournamentResponse> response = tournamentService.getAllTournaments();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/host")
    public ResponseEntity<List<TournamentResponse>> getCurrentUserTournaments() {
        try {
            return jwtUtil.getCurrentUserId()
                    .map(userId -> {
                        try {
                            List<TournamentResponse> response = tournamentService.getTournamentsByHost(userId);
                            return ResponseEntity.ok(response);
                        } catch (Exception e) {
                            return ResponseEntity.<List<TournamentResponse>>internalServerError().build();
                        }
                    })
                    .orElse(ResponseEntity.<List<TournamentResponse>>status(401).build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TournamentResponse> updateTournament(
            @PathVariable UUID id,
            @RequestBody TournamentRequest request) {
        try {
            return jwtUtil.getCurrentUserId()
                    .map(userId -> {
                        try {
                            TournamentResponse response = tournamentService.updateTournament(id, request, userId);
                            return ResponseEntity.ok(response);
                        } catch (Exception e) {
                            return ResponseEntity.<TournamentResponse>badRequest().build();
                        }
                    })
                    .orElse(ResponseEntity.<TournamentResponse>status(401).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(
            @PathVariable UUID id) {
        try {
            return jwtUtil.getCurrentUserId()
                    .map(userId -> {
                        try {
                            tournamentService.deleteTournament(id, userId);
                            return ResponseEntity.<Void>ok().build();
                        } catch (Exception e) {
                            return ResponseEntity.<Void>badRequest().build();
                        }
                    })
                    .orElse(ResponseEntity.<Void>status(401).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 