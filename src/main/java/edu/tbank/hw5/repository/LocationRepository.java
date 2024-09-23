package edu.tbank.hw5.repository;

import edu.tbank.hw5.dto.Location;
import edu.tbank.hw5.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class LocationRepository {
    private final Storage<String, Location> storage = new Storage<>();

    public List<Location> findAll() {
        log.info("Fetching all locations");
        return storage.getAll();
    }

    public Location findById(String id) {
        log.info("Fetching location with id: {}", id);
        return storage.get(id);
    }

    public void save(Location location) {
        log.info("Saving location with id: {}", location.getSlug());
        storage.put(location.getSlug(), location);
    }

    public void deleteById(String id) {
        log.info("Deleting location with id: {}", id);
        storage.remove(id);
    }

    public void saveAll(List<Location> locations) {
        log.info("Saving all locations");
        for (Location location: locations) {
            storage.put(location.getSlug(), location);
        }
    }

    public void update(Location location, String id) {
        log.info("Updating location with id: {}", id);
        storage.put(id, location);
    }
}