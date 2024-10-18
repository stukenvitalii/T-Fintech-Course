package edu.tbank.hw5.service.impl;

import edu.tbank.hw5.dto.EventDto;
import edu.tbank.hw5.dto.EventMapper;
import edu.tbank.hw5.entity.Event;
import edu.tbank.hw5.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventServiceImpl eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEventsByBudget() {
        // Arrange
        Event event = new Event();
        EventDto eventDto = new EventDto(1L,"title",100L);
        when(eventRepository.getEventsByBudget()).thenReturn(Flux.just(event));
        when(eventMapper.toDto(event)).thenReturn(eventDto);

        // Act
        Flux<EventDto> result = eventService.getEventsByBudget();

        // Assert
        StepVerifier.create(result)
                .expectNext(eventDto)
                .verifyComplete();

        verify(eventRepository).getEventsByBudget();
        verify(eventMapper).toDto(event);
    }

    @Test
    void testGetEventsByBudgetAsync() {
        // Arrange
        LocalDate dateFrom = LocalDate.now().minusDays(10);
        LocalDate dateTo = LocalDate.now();
        Double budget = 100.0;
        String currency = "USD";
        Event event = new Event();
        event.setPrice("9600");
        EventDto eventDto = new EventDto(1L,"title",9600L);

        when(eventRepository.getEventsByBudget(dateFrom, dateTo)).thenReturn(Mono.just(Flux.just(event)));
        when(eventMapper.toDto(event)).thenReturn(eventDto);

        // Act
        Mono<List<EventDto>> result = eventService.getEventsByBudgetAsync(budget, currency, dateFrom, dateTo);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(events -> events.size() == 1 && events.get(0).getPrice() <= 9600.0)
                .verifyComplete();

        verify(eventRepository).getEventsByBudget(dateFrom, dateTo);
        verify(eventMapper).toDto(event);
    }
}
