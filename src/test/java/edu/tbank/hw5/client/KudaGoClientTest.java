package edu.tbank.hw5.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import edu.tbank.hw5.dto.Category;
import edu.tbank.hw5.dto.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestPropertySource(locations = "classpath:application-test.properties")
public class KudaGoClientTest {

    @Container
    static WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:3.6.0");

    @Autowired
    private KudaGoClient kudaGoClient;

    @Value("${app.categories-endpoint}")
    private String categoriesEndpoint;

    @Value("${app.locations-endpoint}")
    private String locationsEndpoint;

    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("app.kuda-go-url", wiremockServer::getBaseUrl);
    }

    @Test
    void testGetAllCategories() {
        // Given
        WireMock.configureFor(wiremockServer.getHost(), wiremockServer.getFirstMappedPort());
        String categoriesJson = "[{\"id\":1,\"name\":\"Category 1\"},{\"id\":2,\"name\":\"Category 2\"}]";
        stubFor(get(urlEqualTo(categoriesEndpoint))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(categoriesJson)));

        // When
        List<Category> categories = kudaGoClient.getAllCategories();

        // Then
        assertThat(categories).hasSize(2);
    }

    @Test
    void testGetAllCategoriesError() {
        // Given
        WireMock.configureFor(wiremockServer.getHost(), wiremockServer.getFirstMappedPort());
        stubFor(get(urlEqualTo(categoriesEndpoint))
                .willReturn(aResponse()
                        .withStatus(500)));

        // When
        List<Category> categories = kudaGoClient.getAllCategories();

        // Then
        assertThat(categories).isEmpty();
    }

    @Test
    void testGetAllLocations() {
        // Given
        WireMock.configureFor(wiremockServer.getHost(), wiremockServer.getFirstMappedPort());
        String locationsJson = "[{\"slug\":\"slug1\",\"name\":\"Location 1\"},{\"slug\":\"slug2\",\"name\":\"Location 2\"}]";
        stubFor(get(urlEqualTo(locationsEndpoint))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(locationsJson)));

        // When
        List<Location> locations = kudaGoClient.getAllLocations();

        // Then
        assertThat(locations).hasSize(2);
    }

    @Test
    void testGetAllLocationsError() {
        // Given
        WireMock.configureFor(wiremockServer.getHost(), wiremockServer.getFirstMappedPort());
        stubFor(get(urlEqualTo(locationsEndpoint))
                .willReturn(aResponse()
                        .withStatus(500)));

        // When
        List<Location> locations = kudaGoClient.getAllLocations();

        // Then
        assertThat(locations).isEmpty();
    }
}