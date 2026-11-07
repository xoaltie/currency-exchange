package com.github.xoaltie.currencyexchange.dto;

public record ExchangeRateRequestDto(String baseCurrencyCode, String targetCurrencyCode, String rate) {
    public ExchangeRateRequestDto(String baseCurrencyCode, String targetCurrencyCode, String rate) {
        this.baseCurrencyCode = baseCurrencyCode == null ? null : baseCurrencyCode.strip();
        this.targetCurrencyCode = targetCurrencyCode == null ? null : targetCurrencyCode.strip();
        this.rate = rate == null ? null : rate.strip();
    }
}
