package edu.tbank.hw5.service.impl;

import edu.tbank.hw5.dto.Location;
import edu.tbank.hw5.repository.LocationRepository;
import edu.tbank.hw5.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

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
        locationRepository.update(location, id);
    }

    @Override
    public void deleteLocationById(String id) {
        locationRepository.deleteById(id);
    }
}