package edu.tbank.hw8.dto;

public record ConvertRequest (
    String fromCurrency,
    String toCurrency,
    Double amount
) {}
