package com.github.xoaltie.currencyexchange.validator;

import com.github.xoaltie.currencyexchange.dto.ExchangeRateRequestDto;
import com.github.xoaltie.currencyexchange.exception.ValidationException;

import java.math.BigDecimal;
import java.util.Objects;

public final class ExchangeRateValidator {
    private ExchangeRateValidator() {
    }

    public static void validate(ExchangeRateRequestDto dto) {
        Objects.requireNonNull(dto, "exchange rate request dto must not be null");

        validateMissing(dto.baseCurrencyCode(), "baseCurrencyCode");
        validateMissing(dto.targetCurrencyCode(), "targetCurrencyCode");
        validateMissing(dto.rate(), "rate");

        validateBlank(dto.rate(), "rate");

        CurrencyValidator.validateCode(dto.baseCurrencyCode());
        CurrencyValidator.validateCode(dto.targetCurrencyCode());

        validateRate(dto.rate());
    }

    private static void validateMissing(String value, String fieldName) {
        if (value == null) {
            throw new ValidationException("missing %s field".formatted(fieldName));
        }
    }

    private static void validateBlank(String value, String fieldName) {
        if (value.isBlank()) {
            throw new ValidationException("%s: must be non-empty".formatted(fieldName));
        }
    }

    private static void validateRate(String rate) {
        BigDecimal rateDecimal;
        try {
            rateDecimal = new BigDecimal(rate);
        } catch (NumberFormatException e) {
            throw new ValidationException("invalid rate format: %s".formatted(rate));
        }

        if (rateDecimal.compareTo(BigDecimal.ZERO) < 1) {
            throw new ValidationException("rate must be positive: %s".formatted(rate));
        }
    }
}
