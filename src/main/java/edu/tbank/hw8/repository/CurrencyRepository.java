package edu.tbank.hw8.repository;

import edu.tbank.hw8.client.CentralBankApiClient;
import edu.tbank.hw8.entity.Valute;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CurrencyRepository {
    private final CentralBankApiClient centralBankApiClient;

    public List<Valute> getDailyRates() {
        return centralBankApiClient.getDailyRates();
    }
}
