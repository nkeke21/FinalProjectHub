package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.*;
import ge.join2play.join2playback.service.ApplicationService;
import ge.join2play.join2playback.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {
    private final ApplicationService applicationService;
    private final JwtUtil jwtUtil;

    @Autowired
    public EventController(ApplicationService applicationService, JwtUtil jwtUtil) {
        this.applicationService = applicationService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/setup")
    public SetupResponse getSetupInfo() {
        return applicationService.getSetupInfo();
    }

    @PostMapping
    public EventResponse createEvent(@RequestBody EventRequest eventRequest) {
        return jwtUtil.getCurrentUserId()
                .map(userId -> {
                    eventRequest.setHostId(userId);
                    return applicationService.createEvent(eventRequest);
                })
                .orElseThrow(() -> new RuntimeException("Not authenticated"));
    }

    @GetMapping("/{id}")
    public EventResponse getEvent(@PathVariable UUID id) {
        return applicationService.getEvent(id);
    }

    @PutMapping("/{id}")
    public EventResponse updateEvent(@RequestBody EventRequest eventRequest, @PathVariable UUID id) {
        return jwtUtil.getCurrentUserId()
                .map(userId -> {
                    eventRequest.setHostId(userId);
                    return applicationService.updateEvent(eventRequest, id);
                })
                .orElseThrow(() -> new RuntimeException("Not authenticated"));
    }

    @GetMapping
    public List<EventResponse> getEvents() {
        return applicationService.getAllEvents();
    }

    @PostMapping("/{id}/join")
    public EventResponse joinEvent(@PathVariable UUID id) {
        return jwtUtil.getCurrentUserId()
                .map(userId -> applicationService.joinEvent(id, userId))
                .orElseThrow(() -> new RuntimeException("Not authenticated"));
    }

    @GetMapping("/{id}/participating")
    public boolean isUserParticipating(@PathVariable UUID id) {
        return jwtUtil.getCurrentUserId()
                .map(userId -> applicationService.isUserParticipating(id, userId))
                .orElseThrow(() -> new RuntimeException("Not authenticated"));
    }

    @DeleteMapping("/{eventId}/participants/{participantId}")
    public EventResponse removeParticipant(@PathVariable UUID eventId, @PathVariable UUID participantId) {
        return jwtUtil.getCurrentUserId()
                .map(userId -> applicationService.removeParticipant(eventId, participantId, userId))
                .orElseThrow(() -> new RuntimeException("Not authenticated"));
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable UUID id) {
        jwtUtil.getCurrentUserId()
                .ifPresentOrElse(
                    userId -> applicationService.deleteEvent(id, userId),
                    () -> { throw new RuntimeException("Not authenticated"); }
                );
    }
}