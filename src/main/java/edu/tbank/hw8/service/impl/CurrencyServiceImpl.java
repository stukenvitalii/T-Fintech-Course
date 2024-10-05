package edu.tbank.hw8.service.impl;

import edu.tbank.hw8.dto.ConvertRequest;
import edu.tbank.hw8.dto.ConvertResponse;
import edu.tbank.hw8.dto.ValuteDto;
import edu.tbank.hw8.exception.CurrencyNotFoundException;
import edu.tbank.hw8.service.CurrencyCacheService;
import edu.tbank.hw8.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Currency;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyCacheService currencyCacheService;

    @Override
    public List<ValuteDto> getDailyRates() {
        List<ValuteDto> response = currencyCacheService.getDailyRates();
        if (response.isEmpty()) {
            log.error("Api is unavailable!");
            throw new HttpServerErrorException(HttpStatusCode.valueOf(503),"Api is unavailable");
        }

        log.info("Getting daily rates from api (maybe cached)");
        return currencyCacheService.getDailyRates();
    }

    @Override
    public ValuteDto getCurrencyRate(String code) {
        if (isInvalidCurrency(code)) {
            log.info("Invalid currency code: {}", code);
            throw new IllegalArgumentException("Invalid currency code: " + code);
        }

        return getDailyRates().stream()
                .filter(valuteDto -> valuteDto.charCode().equals(code))
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

        double fromRate = request.fromCurrency().equals("RUB") ? 1.0 : getRate(request.fromCurrency());
        double toRate = request.toCurrency().equals("RUB") ? 1.0 : getRate(request.toCurrency());

        if (fromRate == -1 || toRate == -1) {
            log.debug("Currency not found");
            throw new CurrencyNotFoundException("Currency not found");
        }

        double convertedAmount = request.amount() * (fromRate / toRate);

        log.debug("From currency with rate: {} and code: {}", fromRate, request.fromCurrency());
        log.debug("To currency with rate: {} and code: {}", toRate, request.toCurrency());
        log.debug("Converted amount: {}", convertedAmount);

        return new ConvertResponse(
                request.fromCurrency(),
                request.toCurrency(),
                convertedAmount
        );
    }

    private double getRate(String currencyCode) {
        ValuteDto currency = getCurrencyRate(currencyCode);
        if (currency == null) {
            return -1;
        }
        return Double.parseDouble(currency.vunitRate().replace(',', '.'));
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