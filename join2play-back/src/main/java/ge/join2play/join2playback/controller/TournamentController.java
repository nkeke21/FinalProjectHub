package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.User;
import ge.join2play.join2playback.model.dto.TournamentRequest;
import ge.join2play.join2playback.model.dto.TournamentResponse;
import ge.join2play.join2playback.model.enums.TournamentStatus;
import ge.join2play.join2playback.service.TournamentService;
import jakarta.servlet.http.HttpSession;
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

    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @PostMapping
    public ResponseEntity<TournamentResponse> createTournament(
            @RequestBody TournamentRequest request,
            HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(401).build(); // Unauthorized
            }
            
            TournamentResponse response = tournamentService.createTournament(request, currentUser.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentResponse> getTournamentById(@PathVariable UUID id) {
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
    public ResponseEntity<List<TournamentResponse>> getCurrentUserTournaments(HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(401).build(); // Unauthorized
            }
            
            List<TournamentResponse> response = tournamentService.getTournamentsByHost(currentUser.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TournamentResponse> updateTournament(
            @PathVariable UUID id,
            @RequestBody TournamentRequest request,
            HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(401).build(); // Unauthorized
            }
            
            TournamentResponse response = tournamentService.updateTournament(id, request, currentUser.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(
            @PathVariable UUID id,
            HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(401).build(); // Unauthorized
            }
            
            tournamentService.deleteTournament(id, currentUser.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TournamentResponse> updateTournamentStatus(
            @PathVariable UUID id,
            @RequestParam TournamentStatus status,
            HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(401).build(); // Unauthorized
            }
            
            TournamentResponse response = tournamentService.updateTournamentStatus(id, status, currentUser.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 