package edu.tbank.hw5.repository;

import edu.tbank.hw5.entity.Location;
import edu.tbank.hw5.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = LocationRepository.class)
public class LocationRepositoryTest {

    @MockBean
    private Storage<String, Location> storage;

    @Autowired
    private LocationRepository locationRepository;

    private Location location1;
    private Location location2;

    @BeforeEach
    public void setup() {
        location1 = new Location(1L,"slug1", "Location 1",null);
        location2 = new Location(2L,"slug2", "Location 2",null);
    }

    @Test
    public void findAll_returnsListOfLocations() {
        List<Location> locations = Arrays.asList(location1, location2);
        when(storage.getAll()).thenReturn(locations);

        List<Location> result = locationRepository.findAll();

        assertEquals(2, result.size());
        assertEquals(locations, result);
        verify(storage).getAll();
    }

    @Test
    public void findById_returnsLocation() {
        when(storage.get("slug1")).thenReturn(location1);

        Location result = locationRepository.findById("slug1");

        assertEquals(location1, result);
        verify(storage).get("slug1");
    }

    @Test
    public void save_savesLocation() {
        locationRepository.save(location1);

        verify(storage).put("slug1", location1);
    }

    @Test
    public void saveAll_savesAllLocations() {
        List<Location> locations = Arrays.asList(location1, location2);

        locationRepository.saveAll(locations);

        verify(storage).put("slug1", location1);
        verify(storage).put("slug2", location2);
    }

    @Test
    public void update_updatesLocation() {
        locationRepository.update(location1, "slug1");

        verify(storage).put("slug1", location1);
    }

    @Test
    public void deleteById_deletesLocation() {
        locationRepository.deleteById("slug1");

        verify(storage).remove("slug1");
    }
}