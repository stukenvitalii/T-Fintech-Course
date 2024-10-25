package edu.tbank.hw5.bootstrap;

import edu.tbank.hw5.client.KudaGoClient;
import edu.tbank.hw5.dto.Category;
import edu.tbank.hw5.dto.Location;
import edu.tbank.hw5.repository.CategoryRepository;
import edu.tbank.hw5.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataLoaderTest {

    @Mock
    private KudaGoClient kudaGoClient;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private DataLoader dataLoader;

    @BeforeEach
    void setUp() {
        dataLoader = new DataLoader(kudaGoClient, categoryRepository, locationRepository);
    }

    @Test
    void run_executesDataLoadingProcess() {
        // When
        dataLoader.run();

        // Then
        verify(kudaGoClient, times(1)).getAllCategories();
        verify(kudaGoClient, times(1)).getAllLocations();
        verify(categoryRepository, atLeastOnce()).saveAll(anyList());
        verify(locationRepository, atLeastOnce()).saveAll(anyList());
    }

    @Test
    void retrieveCategories_savesFetchedCategories() {
        List<Category> categories = List.of(new Category(1L, "slug1", "Category 1"));
        when(kudaGoClient.getAllCategories()).thenReturn(categories);

        dataLoader.retrieveCategories();

        verify(categoryRepository).saveAll(categories);
    }

    @Test
    void retrieveCategories_logsWarningWhenNoCategoriesFetched() {
        when(kudaGoClient.getAllCategories()).thenReturn(null);

        dataLoader.retrieveCategories();

        verify(categoryRepository, never()).saveAll(anyList());
    }

    @Test
    void retrieveLocations_savesFetchedLocations() {
        List<Location> locations = List.of(new Location("slug1", "Location 1"));
        when(kudaGoClient.getAllLocations()).thenReturn(locations);

        dataLoader.retrieveLocations();

        verify(locationRepository).saveAll(locations);
    }

    @Test
    void retrieveLocations_logsWarningWhenNoLocationsFetched() {
        when(kudaGoClient.getAllLocations()).thenReturn(null);

        dataLoader.retrieveLocations();

        verify(locationRepository, never()).saveAll(anyList());
    }
}