package edu.tbank.hw5.service.impl;

import edu.tbank.hw5.dto.EventDto;
import edu.tbank.hw5.dto.EventMapper;
import edu.tbank.hw5.entity.Event;
import edu.tbank.hw5.repository.EventRepository;
import edu.tbank.hw5.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    public List<EventDto> getEventsByBudget() {
        List<Event> events = eventRepository.getEventsByBudget();
        log.info("Found {} events", events.size());
        return events.stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompletableFuture<List<EventDto>> getEventsByBudgetAsync(double budget, String currency, LocalDate dateFrom, LocalDate dateTo) {
        CompletableFuture<List<Event>> eventsFuture = eventRepository.getEventsByBudget(dateFrom, dateTo);
        CompletableFuture<Double> budgetInRublesFuture = convertBudgetToRublesAsync(budget, currency);

        return eventsFuture.thenCombine(budgetInRublesFuture, (events, budgetInRubles) -> {
            log.info("Calculated budget in rubles: {}", budgetInRubles);
            return events.stream()
                    .map(eventMapper::toDto)
                    .filter(eventDto -> eventDto.getPrice() <= budgetInRubles)
                    .collect(Collectors.toList());
        });
    }

    private CompletableFuture<Double> convertBudgetToRublesAsync(double budget, String currency) {
        double conversionRate = getConversionRate(currency);
        return CompletableFuture.supplyAsync(() -> budget * conversionRate);
    }

    private double getConversionRate(String currency) {
        return switch (currency) {
            case "USD" -> 96.0;
            case "EUR" -> 105.0;
            default -> 1.0;
        };
    }
}
