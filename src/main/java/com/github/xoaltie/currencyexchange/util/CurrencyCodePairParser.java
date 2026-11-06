package com.github.xoaltie.currencyexchange.util;

import com.github.xoaltie.currencyexchange.dto.CurrencyCodePair;
import com.github.xoaltie.currencyexchange.exception.ValidationException;

import java.util.Objects;

public final class CurrencyCodePairParser {
    private static final int CODE_LENGTH = 3;
    private static final int CODE_PAIR_LENGTH = 6;

    private CurrencyCodePairParser() {
    }

    public static CurrencyCodePair parse(String codePair) {
        Objects.requireNonNull(codePair, "currency code pair must not be null");

        String normalized = codePair.strip();
        if (normalized.length() != CODE_PAIR_LENGTH) {
            throw new ValidationException("currency code pair must contain exactly 6 characters, got %d instead".formatted(normalized.length()));
        }

        String baseCurrencyCode = normalized.substring(0, CODE_LENGTH);
        String targetCurrencyCode = normalized.substring(CODE_LENGTH);

        return new CurrencyCodePair(baseCurrencyCode, targetCurrencyCode);
    }
}
