package edu.tbank.hw5.bootstrap;

import edu.tbank.hw5.client.KudaGoClient;
import edu.tbank.hw5.entity.type.EntityType;
import edu.tbank.hw5.repository.CategoryRepository;
import edu.tbank.hw5.repository.LocationRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.tinkoff.measurementloggingstarter.annotation.Timed;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.*;

@Timed
@RequiredArgsConstructor
@Component
@Slf4j
public class DataLoader {
    private final KudaGoClient kudaGoClient;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    @Value("${app.threads}")
    private int threadsNum;

    @Value("${app.scheduler-period}")
    private long schedulerPeriod;

    private ExecutorService mainExecutorService;
    private ScheduledExecutorService schedulingExecutorService;

    @PostConstruct
    public void initScheduler() {
        mainExecutorService = Executors.newFixedThreadPool(threadsNum);
        schedulingExecutorService = Executors.newScheduledThreadPool(threadsNum);

        schedulingExecutorService.scheduleAtFixedRate(
                this::retrieveData,
                0,
                schedulerPeriod,
                TimeUnit.SECONDS);
    }

    public void retrieveData() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<Callable<CompletableFuture<String>>> tasks = List.of(
                () -> fetchData(EntityType.CATEGORY).toFuture(),
                () -> fetchData(EntityType.LOCATION).toFuture()
        );

        try {
            List<Future<CompletableFuture<String>>> results = mainExecutorService.invokeAll(tasks);
            for (Future<CompletableFuture<String>> result : results) {
                result.get().thenAccept(outcome -> log.info("Task outcome: {}", outcome));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Task interrupted", e);
        } catch (ExecutionException e) {
            log.error("Task execution failed: {}", e.getMessage(), e);
        }

        stopWatch.stop();
        log.info("retrieveData method took {} ms to execute", stopWatch.getTotalTimeMillis());
    }

    private Mono<String> fetchData(EntityType type) {
        if (EntityType.CATEGORY.equals(type)) {
            return fetchCategories();
        } else if (EntityType.LOCATION.equals(type)) {
            return fetchLocations();
        } else {
            throw new IllegalArgumentException("Unknown entity type: " + type);
        }
    }

    private Mono<String> fetchLocations() {
        return kudaGoClient.getAllLocations()
                .collectList()
                .doOnNext(locations -> {
                    if (locations.isEmpty()) {
                        log.warn("No locations fetched from API or empty list returned");
                    } else {
                        log.info("Fetched {} locations from API", locations.size());
                        locationRepository.saveAll(locations);
                    }
                })
                .then(Mono.just("retrieved locations"))
                .onErrorResume(e -> {
                    log.error("Error fetching locations: {}", e.getMessage(), e);
                    return Mono.just("failed to retrieve locations");
                });
    }

    private Mono<String> fetchCategories() {
        return kudaGoClient.getAllCategories()
                .collectList()
                .doOnNext(categories -> {
                    if (categories.isEmpty()) {
                        log.warn("No categories fetched from API or empty list returned");
                    } else {
                        log.info("Fetched {} categories from API", categories.size());
                        categoryRepository.saveAll(categories);
                    }
                })
                .then(Mono.just("retrieved categories"))
                .onErrorResume(e -> {
                    log.error("Error fetching categories: {}", e.getMessage(), e);
                    return Mono.just("failed to retrieve categories");
                });
    }

    @PreDestroy
    public void shutdownExecutors() {
        schedulingExecutorService.shutdown();
        mainExecutorService.shutdown();
        try {
            if (!schedulingExecutorService.awaitTermination(5, TimeUnit.SECONDS)) {
                schedulingExecutorService.shutdownNow();
            }
            if (!mainExecutorService.awaitTermination(5, TimeUnit.SECONDS)) {
                mainExecutorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Shutdown interrupted", e);
        }
    }
}