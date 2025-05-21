package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.EventResponse;
import ge.join2play.join2playback.model.UserDetailsResponse;
import ge.join2play.join2playback.model.UserUpdateDTO;
import ge.join2play.join2playback.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/details/{id}")
    public UserUpdateDTO updateUserDetails(@PathVariable UUID id, @RequestBody UserUpdateDTO userUpdateDTO){
        return applicationService.updateUserDetails(id, userUpdateDTO);
    }

    @GetMapping("/events/hosted/{id}")
    public List<EventResponse> getUserHostedEvents(@PathVariable UUID id){
        return applicationService.getUserHostedEvents(id);
    }

    @GetMapping("/events/registered/{id}")
    public List<EventResponse> getUserRegisteredEvents(@PathVariable UUID id){
        return applicationService.getUserRegisteredEvents(id);
    }
}
