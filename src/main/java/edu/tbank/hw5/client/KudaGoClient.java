package edu.tbank.hw5.client;

import edu.tbank.hw5.dto.Category;
import edu.tbank.hw5.dto.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class KudaGoClient {
    private final WebClient webClient;

    @Value("${app.categories-endpoint}")
    private String categoriesEndpoint;

    @Value("${app.locations-endpoint}")
    private String locationsEndpoint;

    public Mono<List<Category>> getAllCategories() {
        return webClient.get()
                .uri(categoriesEndpoint)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Category>>() {})
                .onErrorResume(e -> {
                    log.error("Error fetching categories from API: {}", e.getMessage());
                    return Mono.just(Collections.emptyList());
                });
    }

    public Mono<List<Location>> getAllLocations() {
        return webClient.get()
                .uri(locationsEndpoint)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Location>>() {})
                .onErrorResume(e -> {
                    log.error("Error fetching locations from API: {}", e.getMessage());
                    return Mono.just(Collections.emptyList());
                });
    }
}