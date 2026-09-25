-- Initial EventHub schema: events table.
--
-- Matches the Event JPA entity field for field (table "events", UUID id
-- generated application-side, varchar-mapped name/status, TEXT description,
-- TIMESTAMPTZ timestamps, INTEGER capacity). No price column (ER-003 lives on
-- a future TicketType), no venue_id, no other tables, no seed data.

CREATE TABLE events (
    id          UUID            PRIMARY KEY,
    name        VARCHAR(255)    NOT NULL,
    description TEXT,
    start_time  TIMESTAMPTZ     NOT NULL,
    end_time    TIMESTAMPTZ     NOT NULL,
    capacity    INTEGER         NOT NULL CONSTRAINT events_capacity_positive CHECK (capacity > 0),
    status      VARCHAR(255)    NOT NULL CONSTRAINT events_status_allowed CHECK (status IN ('DRAFT', 'PUBLISHED', 'CANCELLED')),
    created_at  TIMESTAMPTZ     NOT NULL,
    updated_at  TIMESTAMPTZ     NOT NULL
);

CREATE INDEX idx_events_status_start ON events (status, start_time);
