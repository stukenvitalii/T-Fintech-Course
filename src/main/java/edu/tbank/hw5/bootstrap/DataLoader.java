package edu.tbank.hw5.bootstrap;

import edu.tbank.hw5.client.KudaGoClient;
import edu.tbank.hw5.dto.Category;
import edu.tbank.hw5.dto.Location;
import edu.tbank.hw5.observer.DataObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.tinkoff.measurementloggingstarter.annotation.Timed;

import java.util.List;

@Timed
@RequiredArgsConstructor
@Component
@Slf4j
public class DataLoader implements CommandLineRunner {
    private final KudaGoClient kudaGoClient;
    private final List<DataObserver<Category>> categoryObservers;
    private final List<DataObserver<Location>> locationObservers;

    @Override
    public void run(String... args) {
        log.info("Starting data loading process");
        retrieveCategories();
        retrieveLocations();
        log.info("Finished data loading process");
    }

    void retrieveCategories() {
        List<Category> categories = kudaGoClient.getAllCategories();
        if (categories != null) {
            log.info("Fetched {} categories from API", categories.size());
            notifyObservers(categoryObservers, categories);
        } else {
            log.warn("No categories fetched from API");
        }
    }

    void retrieveLocations() {
        List<Location> locations = kudaGoClient.getAllLocations();
        if (locations != null) {
            log.info("Fetched {} locations from API", locations.size());
            notifyObservers(locationObservers, locations);
        } else {
            log.warn("No locations fetched from API");
        }
    }

    private <T> void notifyObservers(List<DataObserver<T>> observers, List<T> data) {
        observers.forEach(observer -> observer.update(data));
    }
}