package edu.tbank.hw5.controller;

import edu.tbank.hw5.dto.Location;
import edu.tbank.hw5.service.LocationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {LocationController.class})
class LocationControllerTest {

    @MockBean
    private LocationService locationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Get all locations returns list of locations")
    void getAllLocations_returnsListOfLocations() throws Exception {
        List<Location> locations = Arrays.asList(new Location(), new Location());
        when(locationService.findAll()).thenReturn(locations);

        mockMvc.perform(get("/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(locations.size()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Get location by ID returns location")
    void getLocationById_returnsLocation() throws Exception {
        Location location = new Location();
        when(locationService.getLocationById("1")).thenReturn(location);

        mockMvc.perform(get("/locations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value(location.getSlug()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Add location creates new location")
    void addLocation_createsNewLocation() throws Exception {
        Location location = new Location();
        String locationJson = "{\"name\":\"New Location\"}";

        mockMvc.perform(post("/locations/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(locationJson))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(locationService, times(1)).addLocation(any(Location.class));
    }

    @Test
    @DisplayName("Update location by ID updates existing location")
    void updateLocationById_updatesExistingLocation() throws Exception {
        Location location = new Location();
        String locationJson = "{\"name\":\"Updated Location\"}";

        mockMvc.perform(put("/locations/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(locationJson))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(locationService, times(1)).updateLocationById(any(Location.class), eq("1"));
    }

    @Test
    @DisplayName("Delete location by ID deletes location")
    void deleteLocationById_deletesLocation() throws Exception {
        mockMvc.perform(delete("/locations/delete/1"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(locationService, times(1)).deleteLocationById("1");
    }
}