package edu.tbank.hw5.repository;

import edu.tbank.hw5.client.KudaGoClient;
import edu.tbank.hw5.entity.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EventRepository {
    private final KudaGoClient kudaGoClient;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private long toEpochSecond(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
    }

    private String formatEpochSecond(long epochSecond) {
        return LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneId.systemDefault().getRules().getOffset(LocalDateTime.now())).format(formatter);
    }

    private void logDateRange(long startDateTimestamp, long endDateTimestamp) {
        log.info("Getting events between {} and {}", formatEpochSecond(startDateTimestamp), formatEpochSecond(endDateTimestamp));
    }

    public List<Event> getEventsBetweenDates() {
        LocalDate endDate = LocalDate.now().minusMonths(1);
        LocalDate startDate = LocalDate.now().minusMonths(6);

        long startDateTimestamp = toEpochSecond(startDate);
        long endDateTimestamp = toEpochSecond(endDate);

        logDateRange(startDateTimestamp, endDateTimestamp);

        List<Event> events = kudaGoClient.getEventsBetweenDates(startDateTimestamp, endDateTimestamp);
        log.info("Retrieved {} events", events.size());
        return events;
    }

    public CompletableFuture<List<Event>> getEventsBetweenDates(LocalDate dateFrom, LocalDate dateTo) {
        LocalDate startDate = (dateFrom != null) ? dateFrom : LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1);
        LocalDate endDate = (dateTo != null) ? dateTo : startDate.plusDays(6);

        long startDateTimestamp = toEpochSecond(startDate);
        long endDateTimestamp = toEpochSecond(endDate);

        logDateRange(startDateTimestamp, endDateTimestamp);

        return kudaGoClient.getEventsBetweenDatesAsync(startDateTimestamp, endDateTimestamp)
                .thenApply(events -> {
                    log.info("Retrieved {} events", events.size());
                    return events;
                });
    }
}