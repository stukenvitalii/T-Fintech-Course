package edu.tbank.hw5.service.impl;

import edu.tbank.hw5.dto.Location;
import edu.tbank.hw5.memento.LocationMemento;
import edu.tbank.hw5.repository.LocationRepository;
import edu.tbank.hw5.repository.history.LocationHistoryRepository;
import edu.tbank.hw5.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final LocationHistoryRepository locationHistoryRepository;

    @Override
    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    @Override
    public Location getLocationById(String id) {
        return locationRepository.findById(id);
    }

    @Override
    public void addLocation(Location location) {
        locationRepository.save(location);
    }

    @Override
    public void updateLocationById(Location location, String id) {
        Location existingLocation = locationRepository.findById(id);
        if (existingLocation != null) {
            saveMemento(existingLocation);
            locationRepository.update(location, id);
        }
    }

    @Override
    public void deleteLocationById(String id) {
        Location existingLocation = locationRepository.findById(id);
        if (existingLocation != null) {
            saveMemento(existingLocation);
            locationRepository.deleteById(id);
        }
    }

    private void saveMemento(Location location) {
        String locationSlug = location.getSlug();
        log.info("Saving memento with id {}", locationSlug);
        LocationMemento snapshot = new LocationMemento(location.getSlug(), location.getName());
        locationHistoryRepository.save(locationSlug, snapshot);
    }

    public List<LocationMemento> getLocationHistoryBySlug(String locationSlug) {
        return locationHistoryRepository.findByLocationSlug(locationSlug);
    }
}