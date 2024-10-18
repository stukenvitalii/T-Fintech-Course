package edu.tbank.hw5.service;

import edu.tbank.hw5.dto.EventDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

public interface EventService {
    Flux<EventDto> getEventsByBudget();
    Mono<List<EventDto>> getEventsByBudgetAsync(Double budget, String currency, LocalDate dateFrom, LocalDate dateTo);
}