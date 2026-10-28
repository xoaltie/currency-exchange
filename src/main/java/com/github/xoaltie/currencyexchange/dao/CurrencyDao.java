package com.github.xoaltie.currencyexchange.dao;

import com.github.xoaltie.currencyexchange.entity.Currency;

import java.util.List;

public interface CurrencyDao {
    List<Currency> findAll();
}
