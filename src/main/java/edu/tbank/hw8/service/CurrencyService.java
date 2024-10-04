package edu.tbank.hw8.service;

import edu.tbank.hw8.dto.ConvertRequest;
import edu.tbank.hw8.dto.ConvertResponse;
import edu.tbank.hw8.dto.ValuteDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CurrencyService {
    List<ValuteDto> getDailyRates();
    ValuteDto getCurrencyRate(String code);
    ConvertResponse convertCurrency(ConvertRequest request);
}
