package edu.tbank.hw5.bootstrap;

import edu.tbank.hw5.client.KudaGoClient;
import edu.tbank.hw5.dto.Category;
import edu.tbank.hw5.dto.Location;
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

        List<Callable<String>> tasks = List.of(
                () -> fetchData("categories"),
                () -> fetchData("locations")
        );
        try {
            mainExecutorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Task interrupted", e);
        }

        stopWatch.stop();

        log.info("retrieveData method took {} ms to execute", stopWatch.getTotalTimeMillis());
    }

    private String fetchData(String type) {
        if ("categories".equals(type)) {
            List<Category> categories = kudaGoClient.getAllCategories();
            if (categories != null) {
                log.info("Fetched {} categories from API", categories.size());
                categoryRepository.saveAll(categories);
            } else {
                log.warn("No categories fetched from API");
            }
        } else if ("locations".equals(type)) {
            List<Location> locations = kudaGoClient.getAllLocations();
            if (locations != null) {
                log.info("Fetched {} locations from API", locations.size());
                locationRepository.saveAll(locations);
            } else {
                log.warn("No locations fetched from API");
            }
        }
        return "retrieved " + type;
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