package ge.join2play.join2playback.controller;

import ge.join2play.join2playback.model.EventRequest;
import ge.join2play.join2playback.model.EventResponse;
import ge.join2play.join2playback.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public EventResponse createEvent(@RequestBody EventRequest eventRequest) {
        return eventService.createEvent(eventRequest);
    }

    @GetMapping("/{id}")
    public EventResponse getEvent(@PathVariable UUID id) {
        return eventService.getEvent(id);
    }

    @PutMapping("/{id}")
    public EventResponse updateEvent(@RequestBody EventRequest eventRequest, @PathVariable UUID id) {
        return eventService.updateEvent(eventRequest, id);
    }

    @GetMapping
    public List<EventResponse> getEvents() {
        return eventService.getAllEvents();
    }
}
