package edu.tbank.hw5.controller;

import edu.tbank.hw5.dto.EventDto;
import edu.tbank.hw5.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = EventController.class)
class EventControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private EventService eventService;

    @Test
    void getEvents_returnsListOfEvents() {
        EventDto event1 = new EventDto(1L,"Event1", 100L);
        EventDto event2 = new EventDto(2L,"Event2", 200L);
        List<EventDto> eventList = List.of(event1, event2);

        when(eventService.getEventsByBudgetAsync(anyDouble(), anyString(), any(), any()))
                .thenReturn(Mono.just(eventList));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/events")
                        .queryParam("budget", 100)
                        .queryParam("currency", "USD")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EventDto.class)
                .hasSize(2)
                .contains(event1, event2);
    }

    @Test
    void getEvents_SmallBudget_returnsEmptyList() {
        EventDto event1 = new EventDto(1L,"Event1", 100L);
        EventDto event2 = new EventDto(2L,"Event2", 200L);
        List<EventDto> eventList = List.of(event1, event2);

        when(eventService.getEventsByBudgetAsync(anyDouble(), anyString(), any(), any()))
                .thenReturn(Mono.empty());

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/events")
                        .queryParam("budget", 10)
                        .queryParam("currency", "RUB")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EventDto.class)
                .hasSize(0);
    }

    @Test
    void testGetEventsByBudget_MissingParams_ShouldReturnBadRequest() {
        webTestClient.get()
                .uri("/events")
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void testGetEventsByBudget_NullCurrency_ShouldReturnBadRequest() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/events")
                        .queryParam("budget", 100)
                        .build())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testGetEventsByBudget_InvalidBudget_ShouldReturnBadRequest() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/events")
                        .queryParam("budget", "abc")
                        .queryParam("currency", "USD")
                        .build())
                .exchange()
                .expectStatus().isBadRequest();
    }

}
