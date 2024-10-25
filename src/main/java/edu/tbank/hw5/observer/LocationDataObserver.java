package edu.tbank.hw5.observer;

import edu.tbank.hw5.dto.Location;
import edu.tbank.hw5.repository.LocationRepository;

import java.util.List;

public class LocationDataObserver implements DataObserver<Location> {
    private final LocationRepository locationRepository;

    public LocationDataObserver(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public void update(List<Location> data) {
        locationRepository.saveAll(data);
    }
}