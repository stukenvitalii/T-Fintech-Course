package edu.tbank.hw5.service;

import edu.tbank.hw5.dto.EventDto;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface EventService {
    Flux<EventDto> getEventsByBudgetAsync(Double budget, String currency, LocalDate dateFrom, LocalDate dateTo);
}