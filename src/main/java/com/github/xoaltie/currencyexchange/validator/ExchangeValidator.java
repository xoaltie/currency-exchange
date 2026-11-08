package com.github.xoaltie.currencyexchange.validator;

import com.github.xoaltie.currencyexchange.exception.ValidationException;

import java.math.BigDecimal;
import java.util.Objects;

public final class ExchangeValidator {
    private ExchangeValidator() {
    }

    public static void validate(String baseCurrencyCode, String targetCurrencyCode, String amount) {
        Objects.requireNonNull(amount, "amount must not be null");

        validateMissing(baseCurrencyCode, "baseCurrencyCode");
        validateMissing(targetCurrencyCode, "targetCurrencyCode");
        validateMissing(amount, "amount");

        validateBlank(amount, "amount");

        CurrencyValidator.validateCode(baseCurrencyCode);
        CurrencyValidator.validateCode(targetCurrencyCode);

        validateAmount(amount);
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

    private static void validateAmount(String amountRaw) {
        BigDecimal amount;
        try {
            amount = new BigDecimal(amountRaw);
        } catch (NumberFormatException e) {
            throw new ValidationException("invalid amount format: %s".formatted(amountRaw));
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("amount must be positive: %s".formatted(amount));
        }
    }
}
