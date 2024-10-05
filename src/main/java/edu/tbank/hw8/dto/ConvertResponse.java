package edu.tbank.hw8.dto;

public record ConvertResponse(
        String fromCurrency,
        String toCurrency,
        Double convertedAmount) {
}
