package edu.tbank.hw5.service;

import edu.tbank.hw5.dto.EventDto;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface EventService {
    List<EventDto> getEventsByBudget();
    CompletableFuture<List<EventDto>> getEventsByBudgetAsync(double budget, String currency, LocalDate dateFrom, LocalDate dateTo);
}