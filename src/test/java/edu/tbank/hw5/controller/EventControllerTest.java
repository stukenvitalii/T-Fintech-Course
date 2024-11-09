package edu.tbank.hw5.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tbank.hw5.dto.EventDto;
import edu.tbank.hw5.entity.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class EventControllerTest {

    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("test_db")
                    .withUsername("postgres")
                    .withPassword("postgres");

    static {
        postgresContainer.start();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetList() throws Exception {
        mockMvc.perform(get("/rest/events"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetOne() throws Exception {
        mockMvc.perform(get("/rest/events/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateEvent() throws Exception {
        Location location = new Location(1L,"Testslug", "Testname", null);
        EventDto eventDto = new EventDto("Test Event", 100.0,false, LocalDate.now(), location);
        String eventJson = objectMapper.writeValueAsString(eventDto);

        mockMvc.perform(post("/rest/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Test Event"))
                .andExpect(jsonPath("$.price").value(100));
    }

    @Test
    void testPatchEvent() throws Exception {
        JsonNode patchNode = objectMapper.readTree("{\"title\": \"Updated Event\"}");

        mockMvc.perform(patch("/rest/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchNode.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Event"));
    }

    @Test
    void testDeleteEvent() throws Exception {
        mockMvc.perform(delete("/rest/events/1"))
                .andExpect(status().isOk());
    }
}
