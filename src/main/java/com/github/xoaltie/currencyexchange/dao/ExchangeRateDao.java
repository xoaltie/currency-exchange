package com.github.xoaltie.currencyexchange.dao;

import com.github.xoaltie.currencyexchange.entity.ExchangeRate;

import java.util.List;
import java.util.Optional;

public interface ExchangeRateDao {
    List<ExchangeRate> findAll();

    Optional<ExchangeRate> findByCodePair(String baseCurrencyCode, String targetCurrencyCode);
}
