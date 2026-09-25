package com.eventhub.backend.event;

/**
 * Lifecycle states of an Event.
 *
 * <p>Approved values are exactly DRAFT, PUBLISHED and CANCELLED. Events are
 * never physically deleted (ER-004); retiring an event means moving it to
 * CANCELLED instead of removing its row.
 */
public enum EventStatus {
    DRAFT,
    PUBLISHED,
    CANCELLED
}
