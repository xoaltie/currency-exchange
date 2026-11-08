package com.github.xoaltie.currencyexchange.dto;

public record ExchangeRequestDto(String baseCurrencyCode, String targetCurrencyCode, String amount) {
    public ExchangeRequestDto {
        baseCurrencyCode = baseCurrencyCode == null ? null : baseCurrencyCode.strip();
        targetCurrencyCode = targetCurrencyCode == null ? null : targetCurrencyCode.strip();
        amount = amount == null ? null : amount.strip();
    }
}
