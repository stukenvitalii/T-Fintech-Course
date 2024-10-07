package edu.tbank.hw8.client;

import edu.tbank.hw8.entity.ValCurs;
import edu.tbank.hw8.entity.Valute;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BankClient {
    private final RestClient restClient;

    @CircuitBreaker(name = "bankClient", fallbackMethod = "fallbackDailyRates")
    public List<Valute> getDailyRates() {
        ValCurs valCursResponse = restClient
                .get()
                .uri("/XML_daily.asp/")
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, (response, request) -> {
                    log.error("Error response: {}", response);
                    throw new HttpServerErrorException(HttpStatusCode.valueOf(503));
                })
                .toEntity(ValCurs.class)
                .getBody();
        if (valCursResponse == null) {
            throw new HttpServerErrorException(HttpStatusCode.valueOf(503));
        }
        return valCursResponse.getValutes();
    }

    public List<Valute> fallbackDailyRates(Throwable throwable) {
        log.warn("Api is unavailable! Fallback method fallbackDailyRates triggered due to {} ", throwable.getMessage());
        return Collections.emptyList();
    }
}
