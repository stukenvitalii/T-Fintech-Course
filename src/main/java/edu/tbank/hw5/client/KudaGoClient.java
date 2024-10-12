package edu.tbank.hw5.client;

import edu.tbank.hw5.dto.EventApiResponse;
import edu.tbank.hw5.entity.Category;
import edu.tbank.hw5.entity.Event;
import edu.tbank.hw5.entity.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class KudaGoClient {
    private final RestClient restClient;

    @Value("${app.categories-endpoint}")
    private String categoriesEndpoint;

    @Value("${app.locations-endpoint}")
    private String locationsEndpoint;

    @Value("${app.events-endpoint}")
    private String eventsEndpoint;

    public List<Category> getAllCategories() {
        try {
            return restClient
                    .method(HttpMethod.GET)
                    .uri(categoriesEndpoint)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<List<Category>>() {})
                    .getBody();
        } catch (RestClientException e) {
            log.error("Error fetching categories from API: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Location> getAllLocations() {
        try {
            return restClient
                    .method(HttpMethod.GET)
                    .uri(locationsEndpoint)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<List<Location>>() {})
                    .getBody();
        } catch (RestClientException e) {
            log.error("Error fetching locations from API: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Event> getEventsBetweenDates(long startDateTimestamp, long endDateTimestamp) {
        try {
            EventApiResponse response = restClient
                    .method(HttpMethod.GET)
                    .uri(eventsEndpoint +
                                    "?actual_since={startDate}&actual_until={endDate}&fields=id,title,price",
                            startDateTimestamp, endDateTimestamp)
                    .retrieve()
                    .toEntity(EventApiResponse.class)
                    .getBody();
            return response != null ? response.getEvents() : Collections.emptyList();
        } catch (RestClientException e) {
            log.error("Error fetching events from API: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public CompletableFuture<List<Event>> getEventsBetweenDatesAsync(long startDateTimestamp, long endDateTimestamp) {
        return CompletableFuture.supplyAsync(() -> getEventsBetweenDates(startDateTimestamp, endDateTimestamp));
    }
}