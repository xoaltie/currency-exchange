package com.github.xoaltie.currencyexchange.entity;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public final class ExchangeRate {
    private final int id;
    private final Currency baseCurrency;
    private final Currency targetCurrency;
    private final BigDecimal rate;

    // TODO: подумать о том, чтобы заменить на double (для консистентности с бд, вкорячить ограничение на 6 символов после запятой), операции с числами выполнять уже в BigDecimal
    public ExchangeRate(int id, Currency baseCurrency, Currency targetCurrency, BigDecimal rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }
}
