package edu.tbank.hw8.controller;

import edu.tbank.hw8.dto.ConvertRequest;
import edu.tbank.hw8.dto.ConvertResponse;
import edu.tbank.hw8.dto.ValuteDto;
import edu.tbank.hw8.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("/rates/{code}")
    private ValuteDto getCurrencyRate(@PathVariable String code) {
        return currencyService.getCurrencyRate(code);
    }

    @GetMapping("/convert")
    private ConvertResponse convertCurrency(@RequestBody ConvertRequest request) {
        return currencyService.convertCurrency(request);
    }
}