package edu.tbank.hw8.controller;

import edu.tbank.hw8.dto.ConvertRequest;
import edu.tbank.hw8.dto.ConvertResponse;
import edu.tbank.hw8.dto.ValuteDto;
import edu.tbank.hw8.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currencies")
@RequiredArgsConstructor
public class CurrencyController implements CurrencyApi {

    private final CurrencyService currencyService;

    @Override
    public ValuteDto getCurrencyRate(String code) {
        return currencyService.getCurrencyRate(code);
    }

    @Override
    public ConvertResponse convertCurrency(ConvertRequest request) {
        return currencyService.convertCurrency(request);
    }
}