package com.github.xoaltie.currencyexchange.validator;

import com.github.xoaltie.currencyexchange.exception.ValidationException;

public abstract class Validator {
    protected void validateMissing(String value, String fieldName) {
        if (value == null) {
            throw new ValidationException("missing %s field".formatted(fieldName));
        }
    }

    protected void validateBlank(String value, String fieldName) {
        if (value.isBlank()) {
            throw new ValidationException("%s: must be non-empty".formatted(fieldName));
        }
    }
}
