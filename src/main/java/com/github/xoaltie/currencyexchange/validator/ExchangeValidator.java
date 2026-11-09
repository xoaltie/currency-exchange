package com.github.xoaltie.currencyexchange.validator;

import com.github.xoaltie.currencyexchange.dto.ExchangeRequestDto;
import com.github.xoaltie.currencyexchange.exception.ValidationException;

import java.math.BigDecimal;
import java.util.Objects;

public final class ExchangeValidator extends Validator {
    private final CurrencyValidator currencyValidator;

    public ExchangeValidator(CurrencyValidator currencyValidator) {
        Objects.requireNonNull(currencyValidator, "currency validator must not be null");
        this.currencyValidator = currencyValidator;
    }

    public void validate(ExchangeRequestDto dto) {
        Objects.requireNonNull(dto, "exchange request dto must not be null");

        validateMissing(dto.baseCurrencyCode(), "baseCurrencyCode");
        validateMissing(dto.targetCurrencyCode(), "targetCurrencyCode");
        validateMissing(dto.amount(), "amount");

        validateBlank(dto.amount(), "amount");

        currencyValidator.validateCode(dto.baseCurrencyCode());
        currencyValidator.validateCode(dto.targetCurrencyCode());

        validateAmount(dto.amount());
    }


    private void validateAmount(String amountRaw) {
        BigDecimal amount;
        try {
            amount = new BigDecimal(amountRaw);
        } catch (NumberFormatException e) {
            throw new ValidationException("invalid amount format: %s".formatted(amountRaw));
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("amount must be non-negative: %s".formatted(amount));
        }
    }
}
