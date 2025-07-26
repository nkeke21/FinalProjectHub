package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.dto.TournamentRequest;
import ge.join2play.join2playback.model.dto.TournamentResponse;
import ge.join2play.join2playback.service.TournamentService;
import ge.join2play.join2playback.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            TournamentResponse response = tournamentService.createTournament(request, currentUserId.get());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentResponse> getTournament(@PathVariable UUID id) {
        try {
            TournamentResponse response = tournamentService.getTournamentById(id);
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
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            List<TournamentResponse> response = tournamentService.getTournamentsByHost(currentUserId.get());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TournamentResponse> updateTournament(
            @PathVariable UUID id,
            @RequestBody TournamentRequest request) {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            TournamentResponse response = tournamentService.updateTournament(id, request, currentUserId.get());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(
            @PathVariable UUID id) {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            tournamentService.deleteTournament(id, currentUserId.get());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 