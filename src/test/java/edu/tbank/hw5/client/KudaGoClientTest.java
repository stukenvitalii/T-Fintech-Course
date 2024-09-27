package edu.tbank.hw5.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.tbank.hw5.dto.Category;
import edu.tbank.hw5.dto.Location;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class KudaGoClientTest {

    private WireMockServer wireMockServer;

    @Autowired
    private KudaGoClient kudaGoClient;

    @Value("${app.categories-endpoint}")
    private String categoriesEndpoint;

    @Value("${app.locations-endpoint}")
    private String locationsEndpoint;

    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("app.kuda-go-url", ()->"http://localhost:7070");
    }

    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer(new WireMockConfiguration().port(7070));
        wireMockServer.start();
        WireMock.configureFor("localhost", 7070);
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testGetAllCategories() {
        // Given
        String categoriesJson = "[{\"id\":1,\"name\":\"Category 1\"},{\"id\":2,\"name\":\"Category 2\"}]";
        wireMockServer.stubFor(get(urlEqualTo(categoriesEndpoint))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(categoriesJson)));

        // When
        List<Category> categories = kudaGoClient.getAllCategories();

        // Then
        assertEquals(2, categories.size());
    }

    @Test
    public void testGetAllCategoriesError() {
        // Given
        wireMockServer.stubFor(get(urlEqualTo(categoriesEndpoint))
                .willReturn(aResponse()
                        .withStatus(500)));

        // When
        List<Category> categories = kudaGoClient.getAllCategories();

        // Then
        assertEquals(Collections.emptyList(), categories);
    }

    @Test
    public void testGetAllLocations() {
        // Given
        String locationsJson = "[{\"id\":1,\"name\":\"Location 1\"},{\"id\":2,\"name\":\"Location 2\"}]";
        wireMockServer.stubFor(get(urlEqualTo(locationsEndpoint))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(locationsJson)));

        // When
        List<Location> locations = kudaGoClient.getAllLocations();

        // Then
        assertEquals(2, locations.size());
    }

    @Test
    public void testGetAllLocationsError() {
        // Given
        wireMockServer.stubFor(get(urlEqualTo(locationsEndpoint))
                .willReturn(aResponse()
                        .withStatus(500)));

        // When
        List<Location> locations = kudaGoClient.getAllLocations();

        // Then
        assertEquals(Collections.emptyList(), locations);
    }
}