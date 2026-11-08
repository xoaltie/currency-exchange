package com.github.xoaltie.currencyexchange.dao;

import com.github.xoaltie.currencyexchange.entity.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ExchangeRateDao {
    List<ExchangeRate> findAll();

    Optional<ExchangeRate> findByCodePair(String baseCurrencyCode, String targetCurrencyCode);

    ExchangeRate create(ExchangeRate exchangeRate);

    ExchangeRate update(ExchangeRate exchangeRate, BigDecimal rate);
}
