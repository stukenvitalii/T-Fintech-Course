package edu.tbank.hw8.repository;

import edu.tbank.hw8.client.BankClient;
import edu.tbank.hw8.entity.Valute;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CurrencyRepository {
    private final BankClient bankClient;

    public List<Valute> getDailyRates() {
        return bankClient.getDailyRates();
    }
}
