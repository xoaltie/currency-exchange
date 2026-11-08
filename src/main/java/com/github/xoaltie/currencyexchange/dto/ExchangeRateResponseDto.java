package com.github.xoaltie.currencyexchange.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record ExchangeRateResponseDto(int id, CurrencyResponseDto baseCurrency, CurrencyResponseDto targetCurrency,
                                      BigDecimal rate) {
    public ExchangeRateResponseDto(int id, CurrencyResponseDto baseCurrency, CurrencyResponseDto targetCurrency, BigDecimal rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate.setScale(6, RoundingMode.HALF_EVEN);
    }
}
