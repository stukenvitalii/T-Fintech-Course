package edu.tbank.hw5.client;

import edu.tbank.hw5.dto.EventApiResponse;
import edu.tbank.hw5.entity.Category;
import edu.tbank.hw5.entity.Event;
import edu.tbank.hw5.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class KudaGoClient {
    private final RestClient restClient;
    private final WebClient webClient;

    @Value("${app.categories-endpoint}")
    private String categoriesEndpoint;

    @Value("${app.locations-endpoint}")
    private String locationsEndpoint;

    @Value("${app.events-endpoint}")
    private String eventsEndpoint;

    public Flux<Category> getAllCategories() {
        return webClient
                .method(HttpMethod.GET)
                .uri(categoriesEndpoint)
                .retrieve()
                .bodyToFlux(Category.class)
                .onErrorResume(e -> {
                    log.error("Error fetching categories from API: {}", e.getMessage());
                    return Flux.empty();
                });
    }

    public Flux<Location> getAllLocations() {
        return webClient
                .method(HttpMethod.GET)
                .uri(locationsEndpoint)
                .retrieve()
                .bodyToFlux(Location.class)
                .onErrorResume(e -> {
                    log.error("Error fetching locations from API: {}", e.getMessage());
                    return Flux.empty();
                });
    }

    public Flux<Event> getEventsBetweenDates(long startDateTimestamp, long endDateTimestamp) {
        return webClient
                .method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder
                        .path(eventsEndpoint)
                        .queryParam("actual_since", startDateTimestamp)
                        .queryParam("actual_until", endDateTimestamp)
                        .queryParam("fields", "id,title,price")
                        .build())
                .retrieve()
                .bodyToMono(EventApiResponse.class)
                .flatMapMany(response -> response != null ? Flux.fromIterable(response.getEvents()) : Flux.empty())
                .onErrorResume(e -> {
                    log.error("Error fetching events from API: {}", e.getMessage());
                    return Flux.empty();
                });
    }


    private Mono<Event> processEvent(Event event) {
        return Mono.just(event);
    }

    @Getter
    @AllArgsConstructor
    public static class CombinedResponse {
        private final List<Category> categories;
        private final List<Location> locations;
    }
}