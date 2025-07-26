package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.dto.TeamRequest;
import ge.join2play.join2playback.model.dto.TeamResponse;
import ge.join2play.join2playback.service.TeamService;
import ge.join2play.join2playback.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = "*")
public class TeamController {
    private final TeamService teamService;
    private final JwtUtil jwtUtil;

    @Autowired
    public TeamController(TeamService teamService, JwtUtil jwtUtil) {
        this.teamService = teamService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@RequestBody TeamRequest teamRequest) {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            TeamResponse response = teamService.createTeam(teamRequest, currentUserId.get());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getTeam(@PathVariable UUID id) {
        try {
            TeamResponse response = teamService.getTeam(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/my")
    public ResponseEntity<List<TeamResponse>> getCurrentUserTeams() {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            List<TeamResponse> teams = teamService.getUserTeams(currentUserId.get());
            return ResponseEntity.ok(teams);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<TeamResponse>> getAvailableTeams() {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            List<TeamResponse> teams = teamService.getAvailableTeams(currentUserId.get());
            return ResponseEntity.ok(teams);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/captain")
    public ResponseEntity<List<TeamResponse>> getTeamsByCaptain() {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            List<TeamResponse> teams = teamService.getTeamsByCaptain(currentUserId.get());
            return ResponseEntity.ok(teams);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<String> joinTeam(@PathVariable UUID id) {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        return ResponseEntity.badRequest().body("Please use /api/team-requests/send endpoint to request joining a team");
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamResponse> updateTeam(@PathVariable UUID id, @RequestBody TeamRequest teamRequest) {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            TeamResponse response = teamService.updateTeam(id, teamRequest, currentUserId.get());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}/leave")
    public ResponseEntity<Void> leaveTeam(@PathVariable UUID id) {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            teamService.leaveTeam(id, currentUserId.get());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{teamId}/members/{memberId}")
    public ResponseEntity<Void> removeTeamMember(@PathVariable UUID teamId, @PathVariable UUID memberId) {
        Optional<UUID> currentUserId = jwtUtil.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            teamService.removeTeamMember(teamId, memberId, currentUserId.get());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 