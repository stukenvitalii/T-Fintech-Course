package edu.tbank.hw5.repository;

import edu.tbank.hw5.dto.Location;
import edu.tbank.hw5.storage.Storage;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LocationRepository extends BaseRepository<String, Location> {

    public LocationRepository(Storage<String, Location> storage) {
        super(storage);
    }

    public void save(Location location) {
        save(location, location.getSlug());
    }

    public void saveAll(List<Location> locations) {
        List<String> ids = locations.stream().map(Location::getSlug).collect(Collectors.toList());
        saveAll(locations, ids);
    }
}