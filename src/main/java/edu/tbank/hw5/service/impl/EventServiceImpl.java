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

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
@Service
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    public Flux<EventDto> getEventsByBudgetAsync(Double budget, String currency, LocalDate dateFrom, LocalDate dateTo) {
        Flux<Event> eventsFlux = eventRepository.getEventsByBudget(dateFrom, dateTo);
        Mono<BigDecimal> budgetInRublesMono = convertBudgetToRublesAsync(BigDecimal.valueOf(budget), currency);

        return budgetInRublesMono.flatMapMany(budgetInRubles -> {
            log.info("Calculated budget in rubles: {}", budgetInRubles);

            return eventsFlux
                    .map(eventMapper::toDto)
                    .filter(eventDto -> BigDecimal.valueOf(eventDto.getPrice()).compareTo(budgetInRubles) <= 0);
        });
    }

    private Mono<BigDecimal> convertBudgetToRublesAsync(BigDecimal budget, String currency) {
        BigDecimal conversionRate = getConversionRate(currency);
        return Mono.just(budget.multiply(conversionRate));
    }

    private BigDecimal getConversionRate(String currency) {
        return switch (currency) {
            case "USD" -> BigDecimal.valueOf(96.0);
            case "EUR" -> BigDecimal.valueOf(105.0);
            default -> BigDecimal.valueOf(1.0);
        };
    }
}
