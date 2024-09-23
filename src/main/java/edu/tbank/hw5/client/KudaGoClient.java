package edu.tbank.hw5.client;

import edu.tbank.hw5.dto.Category;
import edu.tbank.hw5.dto.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class KudaGoClient {
    private final RestTemplate restTemplate;

    @Value("${app.categories-endpoint}")
    private String categoriesEndpoint;

    @Value("${app.locations-endpoint}")
    private String locationsEndpoint;

    public List<Category> getAllCategories() {
        try {
            return restTemplate
                    .exchange(
                            categoriesEndpoint,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<List<Category>>() {}
                    ).getBody();
        } catch (RestClientException e) {
            log.error("Error fetching categories from API: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Location> getAllLocations() {
        try {
            return restTemplate
                    .exchange(
                            locationsEndpoint,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<List<Location>>() {}
                    ).getBody();
        } catch (RestClientException e) {
            log.error("Error fetching locations from API: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}