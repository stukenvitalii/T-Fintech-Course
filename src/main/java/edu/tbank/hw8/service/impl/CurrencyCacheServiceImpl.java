package edu.tbank.hw8.service.impl;

import edu.tbank.hw8.dto.ValuteDto;
import edu.tbank.hw8.dto.mapper.ValuteMapper;
import edu.tbank.hw8.repository.CurrencyRepository;
import edu.tbank.hw8.service.CurrencyCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyCacheServiceImpl implements CurrencyCacheService {

    private final CurrencyRepository currencyRepository;
    private final ValuteMapper valuteMapper;

    @Override
    @Cacheable("dailyRates")
    public List<ValuteDto> getDailyRates() {
        return currencyRepository
                .getDailyRates()
                .stream()
                .map(valuteMapper::toDto)
                .toList();
    }
}