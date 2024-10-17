package edu.tbank.hw5.service.impl;

import edu.tbank.hw5.dto.EventDto;
import edu.tbank.hw5.dto.EventMapper;
import edu.tbank.hw5.entity.Event;
import edu.tbank.hw5.repository.EventRepository;
import edu.tbank.hw5.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    public Flux<EventDto> getEventsByBudget() {
        return eventRepository.getEventsByBudget()
                .map(eventMapper::toDto)
                .doOnNext(eventDto -> log.info("Found event: {}", eventDto));
    }

    @Override
    public Mono<List<EventDto>> getEventsByBudgetAsync(double budget, String currency, LocalDate dateFrom, LocalDate dateTo) {
        Mono<Flux<Event>> eventsMono = eventRepository.getEventsByBudget(dateFrom, dateTo);
        Mono<Double> budgetInRublesMono = convertBudgetToRublesAsync(budget, currency);

        return Mono.zip(eventsMono, budgetInRublesMono)
                .flatMap(tuple -> {
                    Flux<Event> eventsFlux = tuple.getT1();
                    Double budgetInRubles = tuple.getT2();
                    log.info("Calculated budget in rubles: {}", budgetInRubles);

                    return eventsFlux
                            .map(eventMapper::toDto)
                            .filter(eventDto -> eventDto.getPrice() <= budgetInRubles)
                            .collectList();
                });
    }


    private Mono<Double> convertBudgetToRublesAsync(double budget, String currency) {
        double conversionRate = getConversionRate(currency);
        return Mono.just(budget * conversionRate);
    }

    private double getConversionRate(String currency) {
        return switch (currency) {
            case "USD" -> 96.0;
            case "EUR" -> 105.0;
            default -> 1.0;
        };
    }
}
