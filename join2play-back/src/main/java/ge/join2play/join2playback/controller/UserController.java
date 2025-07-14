package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.EventResponse;
import ge.join2play.join2playback.model.UserDetailsResponse;
import ge.join2play.join2playback.model.UserResponse;
import ge.join2play.join2playback.model.UserUpdateDTO;
import ge.join2play.join2playback.model.User;
import ge.join2play.join2playback.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final ApplicationService applicationService;

    @Autowired
    public UserController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/details/{id}")
    public UserDetailsResponse getUserDetails(@PathVariable UUID id){
        return applicationService.getUserDetails(id);
    }

    @GetMapping("/details")
    public ResponseEntity<UserDetailsResponse> getCurrentUserDetails(HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(applicationService.getUserDetails(currentUser.getId()));
    }

    @PatchMapping("/details/{id}")
    public UserUpdateDTO updateUserDetails(@PathVariable UUID id, @RequestBody UserUpdateDTO userUpdateDTO){
        return applicationService.updateUserDetails(id, userUpdateDTO);
    }

    @PatchMapping("/details")
    public ResponseEntity<UserUpdateDTO> updateCurrentUserDetails(@RequestBody UserUpdateDTO userUpdateDTO, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(applicationService.updateUserDetails(currentUser.getId(), userUpdateDTO));
    }

    @GetMapping("/events/hosted/{id}")
    public List<EventResponse> getUserHostedEvents(@PathVariable UUID id){
        return applicationService.getUserHostedEvents(id);
    }

    @GetMapping("/events/hosted")
    public ResponseEntity<List<EventResponse>> getCurrentUserHostedEvents(HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(applicationService.getUserHostedEvents(currentUser.getId()));
    }

    @GetMapping("/events/registered/{id}")
    public List<EventResponse> getUserRegisteredEvents(@PathVariable UUID id){
        return applicationService.getUserRegisteredEvents(id);
    }

    @GetMapping("/events/registered")
    public ResponseEntity<List<EventResponse>> getCurrentUserRegisteredEvents(HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(applicationService.getUserRegisteredEvents(currentUser.getId()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsers(@RequestParam("query") String query) {
        List<UserResponse> responses = applicationService.searchUsers(query);
        return ResponseEntity.ok(responses);
    }
}
