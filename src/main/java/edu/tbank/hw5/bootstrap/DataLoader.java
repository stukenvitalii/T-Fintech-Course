package edu.tbank.hw5.bootstrap;

import edu.tbank.hw5.client.KudaGoClient;
import edu.tbank.hw5.dto.Category;
import edu.tbank.hw5.dto.Location;
import edu.tbank.hw5.repository.CategoryRepository;
import edu.tbank.hw5.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.tinkoff.measurementloggingstarter.annotation.Timed;

import java.util.List;

@Timed
@RequiredArgsConstructor
@Component
@Slf4j
public class DataLoader {
    private final KudaGoClient kudaGoClient;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    @EventListener
    public void handleApplicationStartedEvent(ApplicationStartedEvent event) {
        log.info("Starting data loading process");
        retrieveCategories();
        retrieveLocations();
        log.info("Finished data loading process");
    }

    void retrieveCategories() {
        List<Category> categories = kudaGoClient.getAllCategories();

        if (categories != null) {
            log.info("Fetched {} categories from API", categories.size());
            categoryRepository.saveAll(categories);
        } else {
            log.warn("No categories fetched from API");
        }
    }

    void retrieveLocations() {
        List<Location> locations = kudaGoClient.getAllLocations();
        if (locations != null) {
            log.info("Fetched {} locations from API", locations.size());
            locationRepository.saveAll(locations);
        } else {
            log.warn("No locations fetched from API");
        }
    }
}