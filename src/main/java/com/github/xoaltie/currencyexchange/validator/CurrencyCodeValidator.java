package com.github.xoaltie.currencyexchange.validator;

import com.github.xoaltie.currencyexchange.exception.ValidationException;

import java.util.Objects;

public final class CurrencyCodeValidator {
    private static final int CODE_LENGTH = 3;

    private CurrencyCodeValidator() {
    }

    public static void validate(String code) {
        Objects.requireNonNull(code, "code must be not null");

        if (code.length() != CODE_LENGTH) {
            throw new ValidationException("code: '%s' must be 3 characters, got %d characters instead".formatted(code, code.length()));
        }

        boolean isOnlyLetters = code.chars()
                .allMatch(c -> (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
        if (!isOnlyLetters) {
            throw new ValidationException("code: '%s' must be only latin letters".formatted(code));
        }
    }
}
