package edu.tbank.hw8.controller;

import edu.tbank.hw8.dto.ConvertRequest;
import edu.tbank.hw8.dto.ConvertResponse;
import edu.tbank.hw8.dto.ValuteDto;
import edu.tbank.hw8.exception.CurrencyNotFoundException;
import edu.tbank.hw8.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link CurrencyController}
 */
@WebMvcTest({CurrencyController.class})
public class CurrencyControllerTest {

    @MockBean
    private CurrencyService currencyService;

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @ValueSource(strings = {"USD", "EUR", "CNY", "AUD"})
    public void getCurrencyRate_ValidValuteCode_ReturnsOk(String valuteCode) throws Exception {
        when(currencyService.getCurrencyRate(valuteCode))
                .thenReturn(new ValuteDto(valuteCode, "95"));

        mockMvc.perform(get("/currencies/rates/{valuteCode}", valuteCode))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getCurrencyRate_InvalidValuteCode_ReturnsBadRequest() throws Exception {
        when(currencyService.getCurrencyRate("AAA"))
                .thenThrow(new IllegalArgumentException("Invalid currency code: AAA"));

        mockMvc.perform(get("/currencies/rates/{0}", "AAA"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void getCurrencyRate_NotFoundButValidCurrency_ReturnsNotFound() throws Exception {
        when(currencyService.getCurrencyRate("XAF"))
                .thenThrow(new CurrencyNotFoundException("Currency not found: XAF"));

        mockMvc.perform(get("/currencies/rates/{0}", "XAF"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void convertCurrency_ValidRequest_ReturnsOk() throws Exception {
        String request = """
            {
                "fromCurrency": "USD",
                "toCurrency": "RUB",
                "amount": 100
            }""";

        when(currencyService.convertCurrency(
                new ConvertRequest("USD", "RUB", 100.0)))
                .thenReturn(
                        new ConvertResponse("USD", "RUB", 10000.0));

        mockMvc.perform(get("/currencies/convert")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void convertCurrency_InvalidRequest_ReturnsBadRequest() throws Exception {
        String request = """
            {
                "fromCurrency": "INVALID",
                "toCurrency": "RUB",
                "amount": 100
            }""";

        when(currencyService.convertCurrency(new ConvertRequest("INVALID", "RUB", 100.0)))
                .thenThrow(new IllegalArgumentException("Invalid currency code: INVALID"));

        mockMvc.perform(get("/currencies/convert")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void convertCurrency_NotFoundButValidCurrency_ReturnsNotFound() throws Exception {
        String request = """
            {
                "fromCurrency": "XAF",
                "toCurrency": "RUB",
                "amount": 100
            }""";

        when(currencyService.convertCurrency(new ConvertRequest("XAF", "RUB", 100.0)))
                .thenThrow(new CurrencyNotFoundException("Currency not found: XAF"));

        mockMvc.perform(get("/currencies/convert")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void convertCurrency_NonPositiveAmount_ReturnsBadRequest() throws Exception {
        String request = """
            {
                "fromCurrency": "USD",
                "toCurrency": "RUB",
                "amount": -100
            }""";

        when(currencyService.convertCurrency(new ConvertRequest("USD", "RUB", -100.0)))
                .thenThrow(new IllegalArgumentException("Amount should be positive!"));

        mockMvc.perform(get("/currencies/convert")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
