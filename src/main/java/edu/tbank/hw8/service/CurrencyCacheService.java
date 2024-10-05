package edu.tbank.hw8.service;

import edu.tbank.hw8.dto.ValuteDto;

import java.util.List;

public interface CurrencyCacheService {
    List<ValuteDto> getDailyRates();
}