package com.eventhub.backend.event;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Business rules of event creation. The repository is mocked: these tests run
 * without Spring and without a database.
 */
@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository events;

    private EventService service;

    @BeforeEach
    void setUp() {
        service = new EventService(events);
    }

    private CreateEventRequest validRequest() {
        OffsetDateTime start = OffsetDateTime.now(ZoneOffset.UTC).plusDays(7);
        return new CreateEventRequest("Spring Concert", "An evening of music",
                start, start.plusHours(3), 500);
    }

    @Test
    void successfulCreationPersistsDraftEvent() {
        when(events.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));
        EventResponse response = service.createEvent(validRequest());

        ArgumentCaptor<Event> saved = ArgumentCaptor.forClass(Event.class);
        verify(events).save(saved.capture());
        assertNull(saved.getValue().getId(), "id is generated on persist, not by the service");
        assertEquals(EventStatus.DRAFT, saved.getValue().getStatus());
        assertEquals(EventStatus.DRAFT, response.status());
        assertEquals("Spring Concert", response.name());
        assertEquals(500, response.capacity());
    }

    @Test
    void blankNameIsRejected() {
        OffsetDateTime start = OffsetDateTime.now(ZoneOffset.UTC).plusDays(7);
        CreateEventRequest request = new CreateEventRequest("  ", null, start, start.plusHours(1), 10);
        assertThrows(IllegalArgumentException.class, () -> service.createEvent(request));
    }

    @Test
    void pastStartTimeIsRejected() {
        OffsetDateTime past = OffsetDateTime.now(ZoneOffset.UTC).minusDays(1);
        CreateEventRequest request = new CreateEventRequest("Past", null, past, past.plusHours(3), 10);
        assertThrows(IllegalArgumentException.class, () -> service.createEvent(request));
    }

    @Test
    void endTimeBeforeStartTimeIsRejected() {
        OffsetDateTime start = OffsetDateTime.now(ZoneOffset.UTC).plusDays(7);
        CreateEventRequest request = new CreateEventRequest("Bad range", null, start, start.minusHours(1), 10);
        assertThrows(IllegalArgumentException.class, () -> service.createEvent(request));
    }

    @Test
    void zeroCapacityIsRejected() {
        OffsetDateTime start = OffsetDateTime.now(ZoneOffset.UTC).plusDays(7);
        CreateEventRequest request = new CreateEventRequest("No seats", null, start, start.plusHours(1), 0);
        assertThrows(IllegalArgumentException.class, () -> service.createEvent(request));
    }

    @Test
    void negativeCapacityIsRejected() {
        OffsetDateTime start = OffsetDateTime.now(ZoneOffset.UTC).plusDays(7);
        CreateEventRequest request = new CreateEventRequest("Negative seats", null, start, start.plusHours(1), -5);
        assertThrows(IllegalArgumentException.class, () -> service.createEvent(request));
    }
}
