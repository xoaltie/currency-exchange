package com.github.xoaltie.currencyexchange.dao;

import com.github.xoaltie.currencyexchange.entity.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyDao {
    List<Currency> findAll();

    Optional<Currency> findByCode(String code);

    Currency create(Currency currency);
}
