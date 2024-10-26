package edu.tbank.hw5.service;

import edu.tbank.hw5.dto.Location;
import edu.tbank.hw5.memento.LocationMemento;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationService {
    List<Location> findAll();
    Location getLocationById(String id);
    void addLocation(Location location);
    void updateLocationById(Location location, String id);
    void deleteLocationById(String id);
    List<LocationMemento> getLocationHistoryBySlug(String locationSlug);
}
