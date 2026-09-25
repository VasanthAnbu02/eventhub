package com.eventhub.backend.event;

import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Event business rules. Owns creation: validates the request, assigns the
 * server-managed state (DRAFT status; id and timestamps are generated on
 * persist), and maps the result. No web or persistence details leak out.
 */
@Service
public class EventService {

    private final EventRepository events;

    public EventService(EventRepository events) {
        this.events = events;
    }

    @Transactional
    public EventResponse createEvent(CreateEventRequest request) {
        validate(request);
        Event event = new Event(
                request.name().trim(),
                request.description(),
                request.startTime(),
                request.endTime(),
                request.capacity(),
                EventStatus.DRAFT);
        return EventResponse.from(events.save(event));
    }

    private void validate(CreateEventRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request must be provided");
        }
        if (request.name() == null || request.name().isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        if (request.startTime() == null) {
            throw new IllegalArgumentException("startTime must be provided");
        }
        if (!request.startTime().isAfter(OffsetDateTime.now(request.startTime().getOffset()))) {
            throw new IllegalArgumentException("startTime must be in the future");
        }
        if (request.endTime() == null) {
            throw new IllegalArgumentException("endTime must be provided");
        }
        if (!request.endTime().isAfter(request.startTime())) {
            throw new IllegalArgumentException("endTime must be after startTime");
        }
        if (request.capacity() == null || request.capacity() <= 0) {
            throw new IllegalArgumentException("capacity must be greater than zero");
        }
    }
}
