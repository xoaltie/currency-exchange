package com.github.xoaltie.currencyexchange.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record ExchangeResponseDto(CurrencyResponseDto baseCurrency, CurrencyResponseDto targetCurrency, BigDecimal rate,
                                  BigDecimal amount,
                                  BigDecimal convertedAmount) {
    public ExchangeResponseDto {
        rate = rate.setScale(6, RoundingMode.HALF_EVEN);
        amount = amount.setScale(2, RoundingMode.HALF_EVEN);
        convertedAmount = convertedAmount.setScale(2, RoundingMode.HALF_EVEN);
    }
}
