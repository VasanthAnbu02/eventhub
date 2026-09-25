package com.eventhub.backend.event;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Response body for event creation. Built from the persisted entity — the JPA
 * entity itself is never exposed over REST.
 */
public record EventResponse(
        UUID id,
        String name,
        String description,
        OffsetDateTime startTime,
        OffsetDateTime endTime,
        Integer capacity,
        EventStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {

    static EventResponse from(Event event) {
        return new EventResponse(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getStartTime(),
                event.getEndTime(),
                event.getCapacity(),
                event.getStatus(),
                event.getCreatedAt(),
                event.getUpdatedAt());
    }
}
