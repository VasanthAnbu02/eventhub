package com.eventhub.backend.event;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Event REST API. Thin by design: validates the request body shape, delegates
 * to {@link EventService}, and maps the outcome to HTTP. No business logic.
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService events;

    public EventController(EventService events) {
        this.events = events;
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody CreateEventRequest request) {
        EventResponse response = events.createEvent(request);
        return ResponseEntity.created(URI.create("/api/events/" + response.id())).body(response);
    }
}
