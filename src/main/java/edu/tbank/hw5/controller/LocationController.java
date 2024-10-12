package edu.tbank.hw5.controller;

import edu.tbank.hw5.entity.Location;
import edu.tbank.hw5.service.LocationService;
import lombok.RequiredArgsConstructor;
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
}
