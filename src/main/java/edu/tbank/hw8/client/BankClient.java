package edu.tbank.hw8.client;

import edu.tbank.hw8.entity.ValCurs;
import edu.tbank.hw8.entity.Valute;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BankClient {
    private final RestClient restClient;

    public List<Valute> getDailyRates() {
        return Objects.requireNonNull(restClient
                        .get()
                        .uri("/XML_daily.asp/")
                        .retrieve()
                        .toEntity(ValCurs.class)
                        .getBody())
                .getValutes();
    }
}
