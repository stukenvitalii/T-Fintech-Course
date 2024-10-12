package edu.tbank.hw5.controller;

import edu.tbank.hw5.dto.EventDto;
import edu.tbank.hw5.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventDto>> getEventsByBudget(
            @RequestParam double budget,
            @RequestParam String currency,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo) {
        log.info("Received request to get events with budget: {}, currency: {}, dateFrom: {}, dateTo: {}", budget, currency, dateFrom, dateTo);
        CompletableFuture<List<EventDto>> eventsFuture = eventService.getEventsByBudgetAsync(budget, currency, dateFrom, dateTo);
        List<EventDto> events = eventsFuture.join();
        log.info("Returning {} events", events.size());
        return ResponseEntity.ok(events);
    }
}