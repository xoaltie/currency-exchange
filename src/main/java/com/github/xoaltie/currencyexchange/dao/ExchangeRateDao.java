package com.github.xoaltie.currencyexchange.dao;

import com.github.xoaltie.currencyexchange.entity.ExchangeRate;

import java.util.List;

public interface ExchangeRateDao {
    List<ExchangeRate> findAll();
}
