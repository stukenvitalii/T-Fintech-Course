package edu.tbank.hw8.client;

import edu.tbank.hw8.entity.ValCurs;
import edu.tbank.hw8.entity.Valute;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class BankClient {
    private final RestClient restClient;

    @CircuitBreaker(name = "bankClient", fallbackMethod = "fallbackDailyRates")
    public List<Valute> getDailyRates() {
        return Objects.requireNonNull(restClient
                        .get()
                        .uri("/XML_daily.asp/")
                        .retrieve()
                        .toEntity(ValCurs.class)
                        .getBody())
                .getValutes();
    }

    public List<Valute> fallbackDailyRates(Throwable throwable) {
        log.warn("Api is unavailable! Fallback method '{}' triggered!","fallbackDailyRates");
        return Collections.emptyList();
    }
}
