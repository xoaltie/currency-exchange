package com.github.xoaltie.currencyexchange.dto;

public record ExchangeRateRequestDto(String baseCurrencyCode, String targetCurrencyCode, String rate) {
    public ExchangeRateRequestDto {
        baseCurrencyCode = baseCurrencyCode == null ? null : baseCurrencyCode.strip();
        targetCurrencyCode = targetCurrencyCode == null ? null : targetCurrencyCode.strip();
        rate = rate == null ? null : rate.strip();
    }
}
