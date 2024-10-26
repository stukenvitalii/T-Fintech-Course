package edu.tbank.hw5.controller;

import edu.tbank.hw5.dto.Location;
import edu.tbank.hw5.memento.LocationMemento;
import edu.tbank.hw5.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinkoff.measurementloggingstarter.annotation.Timed;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/locations")
@Timed
public class LocationController {
    private final LocationService locationService;

    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.findAll();
    }

    @GetMapping("/{id}")
    public Location getLocationById(@PathVariable String id) {
        return locationService.getLocationById(id);
    }

    @PostMapping("/add")
    public void addLocation(@RequestBody Location location) {
        locationService.addLocation(location);
    }

    @PutMapping("/update/{id}")
    public void updateLocationById(@RequestBody Location location, @PathVariable String id) {
        locationService.updateLocationById(location, id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteLocationById(@PathVariable String id) {
        locationService.deleteLocationById(id);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<LocationMemento>> getLocationHistoryBySlug(@PathVariable String id) {
        List<LocationMemento> locationMementos = locationService.getLocationHistoryBySlug(id);
        return ResponseEntity.ok(locationMementos);
    }
}
