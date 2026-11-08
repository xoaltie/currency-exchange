package com.github.xoaltie.currencyexchange.service;

import com.github.xoaltie.currencyexchange.dao.CurrencyDao;
import com.github.xoaltie.currencyexchange.dao.ExchangeRateDao;
import com.github.xoaltie.currencyexchange.dto.ExchangeRateResponseDto;
import com.github.xoaltie.currencyexchange.entity.Currency;
import com.github.xoaltie.currencyexchange.entity.ExchangeRate;
import com.github.xoaltie.currencyexchange.exception.CurrencyNotFoundException;
import com.github.xoaltie.currencyexchange.exception.ExchangeRateNotFoundException;
import com.github.xoaltie.currencyexchange.exception.IdenticalCurrencyPairException;
import com.github.xoaltie.currencyexchange.mapper.ExchangeRateMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public final class ExchangeRateService {
    private final ExchangeRateDao exchangeRateDao;
    private final CurrencyDao currencyDao;

    public ExchangeRateService(ExchangeRateDao exchangeRateDao, CurrencyDao currencyDao) {
        Objects.requireNonNull(exchangeRateDao, "exchange rate dao must not be null");
        Objects.requireNonNull(currencyDao, "currency dao must not be null");
        this.exchangeRateDao = exchangeRateDao;
        this.currencyDao = currencyDao;
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

        ExchangeRate exchangeRate = exchangeRateDao.findByCodePair(baseCurrencyCode, targetCurrencyCode)
                .orElseThrow(() -> new ExchangeRateNotFoundException("exchange rate %s does not exist".formatted(baseCurrencyCode + targetCurrencyCode)));
        return ExchangeRateMapper.INSTANCE.toDto(exchangeRate);
    }

    public ExchangeRateResponseDto create(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        Objects.requireNonNull(baseCurrencyCode, "base currency code must be not null");
        Objects.requireNonNull(targetCurrencyCode, "target currency code must be not null");
        Objects.requireNonNull(rate, "rate must be not null");

        if (baseCurrencyCode.equals(targetCurrencyCode)) {
            throw new IdenticalCurrencyPairException("base and target currency code pairs must not be the same: %s".formatted(baseCurrencyCode));
        }

        Currency baseCurrency = currencyDao.findByCode(baseCurrencyCode)
                .orElseThrow(() -> new CurrencyNotFoundException("currency %s does not exist".formatted(baseCurrencyCode)));
        Currency targetCurrency = currencyDao.findByCode(targetCurrencyCode)
                .orElseThrow(() -> new CurrencyNotFoundException("currency %s does not exist".formatted(targetCurrencyCode)));

        ExchangeRate newExchangeRate = exchangeRateDao.create(ExchangeRateMapper.INSTANCE.toEntity(baseCurrency, targetCurrency, rate));
        return ExchangeRateMapper.INSTANCE.toDto(newExchangeRate);
    }

    public ExchangeRateResponseDto update(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        Objects.requireNonNull(baseCurrencyCode, "base currency code must be not null");
        Objects.requireNonNull(targetCurrencyCode, "target currency code must be not null");
        Objects.requireNonNull(rate, "rate must be not null");

        if (baseCurrencyCode.equals(targetCurrencyCode)) {
            throw new IdenticalCurrencyPairException("base and target currency code pairs must not be the same: %s".formatted(baseCurrencyCode));
        }

        ExchangeRate exchangeRate = exchangeRateDao.findByCodePair(baseCurrencyCode, targetCurrencyCode)
                .orElseThrow(() -> new ExchangeRateNotFoundException("exchange rate %s does not exist".formatted(baseCurrencyCode + targetCurrencyCode)));

        ExchangeRate updatedExchangeRate = exchangeRateDao.update(exchangeRate, rate);
        return ExchangeRateMapper.INSTANCE.toDto(updatedExchangeRate);
    }
}
