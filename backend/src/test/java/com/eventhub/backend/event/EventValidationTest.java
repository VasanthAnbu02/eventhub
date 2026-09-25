package com.eventhub.backend.event;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Bean Validation rules of the Event domain model.
 *
 * <p>Runs without Spring and without a database: it exercises only the
 * jakarta.validation constraints declared on {@link Event}.
 */
class EventValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private Event validEvent() {
        OffsetDateTime start = OffsetDateTime.now(ZoneOffset.UTC).plusDays(7);
        return new Event("Spring Concert", "An evening of music",
                start, start.plusHours(3), 500, EventStatus.DRAFT);
    }

    @Test
    void validEventPassesValidation() {
        assertTrue(validator.validate(validEvent()).isEmpty());
    }

    @Test
    void pastStartTimeIsRejected() {
        Event event = validEvent();
        event.setStartTime(OffsetDateTime.now(ZoneOffset.UTC).minusDays(1));
        assertViolationPresent(validator.validate(event));
    }

    @Test
    void endTimeBeforeStartTimeIsRejected() {
        Event event = validEvent();
        event.setEndTime(event.getStartTime().minusHours(1));
        assertViolationPresent(validator.validate(event));
    }

    @Test
    void zeroCapacityIsRejected() {
        Event event = validEvent();
        event.setCapacity(0);
        assertViolationPresent(validator.validate(event));
    }

    @Test
    void negativeCapacityIsRejected() {
        Event event = validEvent();
        event.setCapacity(-10);
        assertViolationPresent(validator.validate(event));
    }

    @Test
    void blankNameIsRejected() {
        Event event = validEvent();
        event.setName("  ");
        assertViolationPresent(validator.validate(event));
    }

    @Test
    void nullStatusIsRejected() {
        Event event = validEvent();
        event.setStatus(null);
        assertViolationPresent(validator.validate(event));
    }

    @Test
    void nullDescriptionIsAllowed() {
        Event event = validEvent();
        event.setDescription(null);
        assertTrue(validator.validate(event).isEmpty());
    }

    private void assertViolationPresent(Set<ConstraintViolation<Event>> violations) {
        assertEquals(1, violations.size());
    }
}
