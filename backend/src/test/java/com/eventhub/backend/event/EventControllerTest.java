package com.eventhub.backend.event;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * HTTP contract of POST /api/events. The service is mocked: these tests run
 * without a database and assert status codes, bodies, and that unknown
 * client fields (id, status, timestamps) cannot influence creation.
 */
@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private EventService events;

    private EventResponse draftResponse() {
        OffsetDateTime start = OffsetDateTime.now(ZoneOffset.UTC).plusDays(7);
        return new EventResponse(UUID.randomUUID(), "Spring Concert", "An evening of music",
                start, start.plusHours(3), 500, EventStatus.DRAFT, start, start);
    }

    @Test
    void successfulCreationReturns201WithDraftEvent() throws Exception {
        when(events.createEvent(any(CreateEventRequest.class))).thenReturn(draftResponse());

        mvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Spring Concert","description":"An evening of music",
                                "startTime":"2030-05-01T18:00:00Z","endTime":"2030-05-01T21:00:00Z",
                                "capacity":500}"""))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.name").value("Spring Concert"))
                .andExpect(jsonPath("$.capacity").value(500));

        verify(events).createEvent(any(CreateEventRequest.class));
    }

    @Test
    void blankNameReturns400AndNeverReachesService() throws Exception {
        mvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"  ","startTime":"2030-05-01T18:00:00Z",
                                "endTime":"2030-05-01T21:00:00Z","capacity":500}"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());

        verify(events, never()).createEvent(any(CreateEventRequest.class));
    }

    @Test
    void clientSuppliedStatusIsIgnored() throws Exception {
        when(events.createEvent(any(CreateEventRequest.class))).thenReturn(draftResponse());

        mvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Spring Concert","startTime":"2030-05-01T18:00:00Z",
                                "endTime":"2030-05-01T21:00:00Z","capacity":500,
                                "status":"PUBLISHED","id":"00000000-0000-0000-0000-000000000000"}"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("DRAFT"));

        verify(events).createEvent(any(CreateEventRequest.class));
    }
}
