package com.eventhub.backend.event;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Persistence for {@link Event}. No custom queries yet: creation is the only
 * approved operation, and repositories must not contain business workflows.
 */
public interface EventRepository extends JpaRepository<Event, UUID> {
}
