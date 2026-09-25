package com.eventhub.backend.event;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.OffsetDateTime;

/**
 * Request body for POST /api/events.
 *
 * <p>Carries only client-supplied fields: id, status, createdAt and updatedAt
 * are server-managed and therefore absent by construction. Field-level
 * constraints are request-boundary checks; business rules are enforced in
 * {@link EventService}.
 */
public record CreateEventRequest(
        @NotBlank(message = "name must not be blank") String name,
        String description,
        @NotNull(message = "startTime must be provided") @Future(message = "startTime must be in the future") OffsetDateTime startTime,
        @NotNull(message = "endTime must be provided") OffsetDateTime endTime,
        @NotNull(message = "capacity must be provided") @Positive(message = "capacity must be greater than zero") Integer capacity) {
}
