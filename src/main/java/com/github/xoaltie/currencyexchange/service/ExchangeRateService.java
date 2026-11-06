package com.github.xoaltie.currencyexchange.service;

import com.github.xoaltie.currencyexchange.dao.ExchangeRateDao;
import com.github.xoaltie.currencyexchange.dto.ExchangeRateResponseDto;
import com.github.xoaltie.currencyexchange.entity.ExchangeRate;
import com.github.xoaltie.currencyexchange.exception.ExchangeRateNotFoundException;
import com.github.xoaltie.currencyexchange.exception.IdenticalCurrencyPairException;
import com.github.xoaltie.currencyexchange.mapper.ExchangeRateMapper;

import java.util.List;
import java.util.Objects;

public final class ExchangeRateService {
    private final ExchangeRateDao exchangeRateDao;

    public ExchangeRateService(ExchangeRateDao exchangeRateDao) {
        Objects.requireNonNull(exchangeRateDao, "exchange rate dao must not be null");
        this.exchangeRateDao = exchangeRateDao;
    }

    public List<ExchangeRateResponseDto> findAll() {
        List<ExchangeRate> exchangeRates = exchangeRateDao.findAll();
        return ExchangeRateMapper.INSTANCE.toDtoList(exchangeRates);
    }

    public ExchangeRateResponseDto findByCodePair(String baseCurrencyCode, String targetCurrencyCode) {
        Objects.requireNonNull(baseCurrencyCode, "base currency code must be not null");
        Objects.requireNonNull(targetCurrencyCode, "target currency code must be not null");

        if (baseCurrencyCode.equals(targetCurrencyCode)) {
            throw new IdenticalCurrencyPairException("base and target currency code pairs must not be the same: %s".formatted(baseCurrencyCode));
        }

        ExchangeRate exchangeRate = exchangeRateDao.findByCodePair(baseCurrencyCode.toUpperCase(), targetCurrencyCode.toUpperCase())
                .orElseThrow(() -> new ExchangeRateNotFoundException("exchange rate %s does not exist".formatted(baseCurrencyCode + targetCurrencyCode)));
        return ExchangeRateMapper.INSTANCE.toDto(exchangeRate);
    }
}
