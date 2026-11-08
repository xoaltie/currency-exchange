package com.github.xoaltie.currencyexchange.validator;

import com.github.xoaltie.currencyexchange.dto.ExchangeRateRequestDto;
import com.github.xoaltie.currencyexchange.exception.ValidationException;

import java.math.BigDecimal;
import java.util.Objects;

public final class ExchangeRateValidator extends Validator {
    private final CurrencyValidator currencyValidator;

    public ExchangeRateValidator(CurrencyValidator currencyValidator) {
        Objects.requireNonNull(currencyValidator, "currency validator must not be null");
        this.currencyValidator = currencyValidator;
    }

    private void validateRate(String rate) {
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

    public void validate(ExchangeRateRequestDto dto) {
        Objects.requireNonNull(dto, "exchange rate request dto must not be null");

        validateMissing(dto.baseCurrencyCode(), "baseCurrencyCode");
        validateMissing(dto.targetCurrencyCode(), "targetCurrencyCode");
        validateMissing(dto.rate(), "rate");

        validateBlank(dto.rate(), "rate");

        currencyValidator.validateCode(dto.baseCurrencyCode());
        currencyValidator.validateCode(dto.targetCurrencyCode());

        validateRate(dto.rate());
    }
}
