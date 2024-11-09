package edu.tbank.hw5.service.impl;

import edu.tbank.hw5.entity.Location;
import edu.tbank.hw5.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocationServiceImplTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationServiceImpl locationService;

    @Test
    public void testFindAll() {
        // Given
        Location location1 = new Location(1L,"slug-1", "Location 1",null);
        Location location2 = new Location(1L,"slug-2", "Location 2",null);
        List<Location> locations = Arrays.asList(location1, location2);

        when(locationRepository.findAll()).thenReturn(locations);

        // When
        List<Location> result = locationService.findAll();

        // Then
        assertEquals(2, result.size());
        verify(locationRepository, times(1)).findAll();
    }

    @Test
    public void testGetLocationBySlug() {
        // Given
        Location location = new Location(1L,"slug-1", "Location 1",null);
        when(locationRepository.findById("slug-1")).thenReturn(location);

        // When
        Location result = locationService.getLocationById("slug-1");

        // Then
        assertEquals("Location 1", result.getName());
        verify(locationRepository, times(1)).findById("slug-1");
    }

    @Test
    public void testAddLocation() {
        // Given
        Location location = new Location(1L,"slug-1", "New Location",null);

        // When
        locationService.addLocation(location);

        // Then
        verify(locationRepository, times(1)).save(location);
    }

    @Test
    public void testUpdateLocationBySlug() {
        // Given
        Location updatedLocation = new Location(1L,"slug-1", "Updated Location",null);

        // When
        locationService.updateLocationById(updatedLocation, "slug-1");

        // Then
        verify(locationRepository, times(1)).update(updatedLocation, "slug-1");
    }

    @Test
    public void testDeleteLocationBySlug() {
        // When
        locationService.deleteLocationById("slug-1");

        // Then
        verify(locationRepository, times(1)).deleteById("slug-1");
    }
}
