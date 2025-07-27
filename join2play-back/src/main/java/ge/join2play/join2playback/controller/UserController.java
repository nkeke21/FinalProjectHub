package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.EventResponse;
import ge.join2play.join2playback.model.UserDetailsResponse;
import ge.join2play.join2playback.model.UserResponse;
import ge.join2play.join2playback.model.UserUpdateDTO;
import ge.join2play.join2playback.service.ApplicationService;
import ge.join2play.join2playback.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    private final ApplicationService applicationService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(ApplicationService applicationService, JwtUtil jwtUtil) {
        this.applicationService = applicationService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/details/{id}")
    public UserDetailsResponse getUserDetails(@PathVariable UUID id){
        return applicationService.getUserDetails(id);
    }

    @GetMapping("/details")
    public ResponseEntity<UserDetailsResponse> getCurrentUserDetails() {
        return jwtUtil.getCurrentUserId()
                .map(userId -> ResponseEntity.ok(applicationService.getUserDetails(userId)))
                .orElse(ResponseEntity.status(401).build());
    }

    @PatchMapping("/details/{id}")
    public UserUpdateDTO updateUserDetails(@PathVariable UUID id, @RequestBody UserUpdateDTO userUpdateDTO){
        return applicationService.updateUserDetails(id, userUpdateDTO);
    }

    @PostMapping("/details")
    public ResponseEntity<UserUpdateDTO> updateCurrentUserDetails(@RequestBody UserUpdateDTO userUpdateDTO) {
        return jwtUtil.getCurrentUserId()
                .map(userId -> ResponseEntity.ok(applicationService.updateUserDetails(userId, userUpdateDTO)))
                .orElse(ResponseEntity.status(401).build());
    }

    @GetMapping("/events/hosted/{id}")
    public List<EventResponse> getUserHostedEvents(@PathVariable UUID id){
        return applicationService.getUserHostedEvents(id);
    }

    @GetMapping("/events/hosted")
    public ResponseEntity<List<EventResponse>> getCurrentUserHostedEvents() {
        return jwtUtil.getCurrentUserId()
                .map(userId -> ResponseEntity.ok(applicationService.getUserHostedEvents(userId)))
                .orElse(ResponseEntity.status(401).build());
    }

    @GetMapping("/events/registered/{id}")
    public List<EventResponse> getUserRegisteredEvents(@PathVariable UUID id){
        return applicationService.getUserRegisteredEvents(id);
    }

    @GetMapping("/events/registered")
    public ResponseEntity<List<EventResponse>> getCurrentUserRegisteredEvents() {
        return jwtUtil.getCurrentUserId()
                .map(userId -> ResponseEntity.ok(applicationService.getUserRegisteredEvents(userId)))
                .orElse(ResponseEntity.status(401).build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsers(@RequestParam("query") String query) {
        List<UserResponse> responses = applicationService.searchUsers(query);
        return ResponseEntity.ok(responses);
    }
}
