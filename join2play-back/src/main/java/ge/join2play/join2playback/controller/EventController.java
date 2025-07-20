package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.*;
import ge.join2play.join2playback.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {
    private final ApplicationService applicationService;

    @Autowired
    public EventController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/setup")
    public SetupResponse getSetupInfo() {
        return applicationService.getSetupInfo();
    }

    @PostMapping
    public EventResponse createEvent(@RequestBody EventRequest eventRequest, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            throw new RuntimeException("Not authenticated");
        }
        eventRequest.setHostId(currentUser.getId());
        return applicationService.createEvent(eventRequest);
    }

    @GetMapping("/{id}")
    public EventResponse getEvent(@PathVariable UUID id) {
        return applicationService.getEvent(id);
    }

    @PutMapping("/{id}")
    public EventResponse updateEvent(@RequestBody EventRequest eventRequest, @PathVariable UUID id, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            throw new RuntimeException("Not authenticated");
        }
        eventRequest.setHostId(currentUser.getId());
        return applicationService.updateEvent(eventRequest, id);
    }

    @GetMapping
    public List<EventResponse> getEvents() {
        return applicationService.getAllEvents();
    }

    @PostMapping("/{id}/join")
    public EventResponse joinEvent(@PathVariable UUID id, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            throw new RuntimeException("Not authenticated");
        }
        return applicationService.joinEvent(id, currentUser.getId());
    }

    @GetMapping("/{id}/participating")
    public boolean isUserParticipating(@PathVariable UUID id, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            throw new RuntimeException("Not authenticated");
        }
        return applicationService.isUserParticipating(id, currentUser.getId());
    }
}