package edu.tbank.hw8.service.impl;

import edu.tbank.hw8.client.CentralBankApiClient;
import edu.tbank.hw8.dto.ConvertRequest;
import edu.tbank.hw8.dto.ConvertResponse;
import edu.tbank.hw8.dto.ValuteDto;
import edu.tbank.hw8.dto.mapper.ValuteMapper;
import edu.tbank.hw8.entity.Valute;
import edu.tbank.hw8.exception.CurrencyNotFoundException;
import edu.tbank.hw8.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private final CentralBankApiClient centralBankApiClient;
    private final ValuteMapper valuteMapper;

    @Override
    public List<ValuteDto> getDailyRates() {
        List<Valute> response = centralBankApiClient.getDailyRates();
        List<ValuteDto> valuteDtos = response.stream().map(valuteMapper::toDto).toList();

        if (response.isEmpty()) {
            log.error("Api is unavailable!");
            throw new HttpServerErrorException(HttpStatusCode.valueOf(503),"Api is unavailable");
        }

        log.info("Getting daily rates from api (maybe cached)");
        return valuteDtos;
    }

    @Override
    public ValuteDto getCurrencyRate(String code) {
        if (isInvalidCurrency(code)) {
            log.info("Invalid currency code: {}", code);
            throw new IllegalArgumentException("Invalid currency code: " + code);
        }

        return getDailyRates().stream()
                .filter(valuteDto -> code.equals(valuteDto.charCode()))
                .findFirst()
                .orElseThrow(() -> new CurrencyNotFoundException("Currency " + code + " is not available in the response from the Central Bank"));
    }

    @Override
public ConvertResponse convertCurrency(ConvertRequest request) {
    if (request.amount() <= 0) {
        log.info("Amount must be greater than 0");
        throw new IllegalArgumentException("Amount must be greater than 0");
    }

    if (isInvalidCurrency(request.fromCurrency()) || isInvalidCurrency(request.toCurrency())) {
        log.info("Invalid currency code");
        throw new IllegalArgumentException("Invalid currency code");
    }

    BigDecimal fromRate = getRateOrDefault(request.fromCurrency());
    BigDecimal toRate = getRateOrDefault(request.toCurrency());

    if (fromRate.compareTo(BigDecimal.ZERO) < 0 || toRate.compareTo(BigDecimal.ZERO) < 0) {
        log.debug("Currency not found");
        throw new CurrencyNotFoundException("Currency not found");
    }
    BigDecimal amount = BigDecimal.valueOf(request.amount());

    BigDecimal convertedAmount = amount.multiply(fromRate).divide(toRate, 5, RoundingMode.HALF_DOWN);

    log.debug("From currency with rate: {} and code: {}", fromRate, request.fromCurrency());
    log.debug("To currency with rate: {} and code: {}", toRate, request.toCurrency());
    log.debug("Converted amount: {}", convertedAmount);

    return new ConvertResponse(
            request.fromCurrency(),
            request.toCurrency(),
            convertedAmount.doubleValue()
    );
}

private BigDecimal getRateOrDefault(String currencyCode) {
    return currencyCode.equals("RUB") ? BigDecimal.ONE : getRate(currencyCode);
}

    private BigDecimal getRate(String currencyCode) {
    ValuteDto currency = getCurrencyRate(currencyCode);
    if (currency == null) {
        return BigDecimal.valueOf(-1);
    }
    return new BigDecimal(currency.vunitRate().replace(',', '.'));
}

    public boolean isInvalidCurrency(String code) {
        try {
            Currency.getInstance(code);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }
}