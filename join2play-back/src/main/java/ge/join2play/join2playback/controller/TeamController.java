package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.dto.TeamRequest;
import ge.join2play.join2playback.model.dto.TeamResponse;
import ge.join2play.join2playback.service.TeamService;
import ge.join2play.join2playback.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        return jwtUtil.getCurrentUserId()
                .map(userId -> ResponseEntity.ok(teamService.createTeam(teamRequest, userId)))
                .orElse(ResponseEntity.<TeamResponse>status(401).build());
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
        return jwtUtil.getCurrentUserId()
                .map(userId -> ResponseEntity.ok(teamService.getUserTeams(userId)))
                .orElse(ResponseEntity.<List<TeamResponse>>status(401).build());
    }

    @GetMapping("/available")
    public ResponseEntity<List<TeamResponse>> getAvailableTeams() {
        return jwtUtil.getCurrentUserId()
                .map(userId -> ResponseEntity.ok(teamService.getAvailableTeams(userId)))
                .orElse(ResponseEntity.<List<TeamResponse>>status(401).build());
    }

    @GetMapping("/captain")
    public ResponseEntity<List<TeamResponse>> getTeamsByCaptain() {
        return jwtUtil.getCurrentUserId()
                .map(userId -> ResponseEntity.ok(teamService.getTeamsByCaptain(userId)))
                .orElse(ResponseEntity.<List<TeamResponse>>status(401).build());
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<String> joinTeam(@PathVariable UUID id) {
        return jwtUtil.getCurrentUserId()
                .map(userId -> {
                    try {
                        return ResponseEntity.badRequest().body("Please use /api/team-requests/send endpoint to request joining a team");
                    } catch (Exception e) {
                        return ResponseEntity.<String>badRequest().build();
                    }
                })
                .orElse(ResponseEntity.<String>status(401).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamResponse> updateTeam(@PathVariable UUID id, @RequestBody TeamRequest teamRequest) {
        return jwtUtil.getCurrentUserId()
                .map(userId -> {
                    try {
                        TeamResponse response = teamService.updateTeam(id, teamRequest, userId);
                        return ResponseEntity.ok(response);
                    } catch (Exception e) {
                        return ResponseEntity.<TeamResponse>badRequest().build();
                    }
                })
                .orElse(ResponseEntity.<TeamResponse>status(401).build());
    }

    @DeleteMapping("/{id}/leave")
    public ResponseEntity<Void> leaveTeam(@PathVariable UUID id) {
        return jwtUtil.getCurrentUserId()
                .map(userId -> {
                    try {
                        teamService.leaveTeam(id, userId);
                        return ResponseEntity.<Void>ok().build();
                    } catch (Exception e) {
                        return ResponseEntity.<Void>badRequest().build();
                    }
                })
                .orElse(ResponseEntity.<Void>status(401).build());
    }

    @DeleteMapping("/{teamId}/members/{memberId}")
    public ResponseEntity<Void> removeTeamMember(@PathVariable UUID teamId, @PathVariable UUID memberId) {
        return jwtUtil.getCurrentUserId()
                .map(userId -> {
                    try {
                        teamService.removeTeamMember(teamId, memberId, userId);
                        return ResponseEntity.<Void>ok().build();
                    } catch (Exception e) {
                        return ResponseEntity.<Void>badRequest().body(null);
                    }
                })
                .orElse(ResponseEntity.<Void>status(401).build());
    }
} 