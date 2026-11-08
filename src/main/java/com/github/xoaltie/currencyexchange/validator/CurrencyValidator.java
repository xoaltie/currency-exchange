package com.github.xoaltie.currencyexchange.validator;

import com.github.xoaltie.currencyexchange.dto.CurrencyRequestDto;
import com.github.xoaltie.currencyexchange.exception.ValidationException;

import java.util.Objects;

public final class CurrencyValidator extends Validator {
    private static final int CODE_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 50;
    private static final int MAX_SIGN_LENGTH = 3;

    public void validate(CurrencyRequestDto dto) {
        Objects.requireNonNull(dto, "CurrencyRequestDto must not be null");

        String code = dto.code();
        String name = dto.name();
        String sign = dto.sign();

        validateMissing(code, "code");
        validateMissing(name, "name");
        validateMissing(sign, "sign");

        validateBlank(code, "code");
        validateBlank(name, "name");
        validateBlank(sign, "sign");

        validateCodeFormat(code);
        validateNameFormat(name);
        validateSignFormat(sign);
    }

    public void validateCode(String code) {
        Objects.requireNonNull(code, "code must be not null");
        validateBlank(code, "code");
        validateCodeFormat(code);
    }

    private void validateCodeFormat(String code) {
        if (code.length() != CODE_LENGTH) {
            throw new ValidationException("code: '%s' must be %d characters, got %d characters instead".formatted(code, CODE_LENGTH, code.length()));
        }
        boolean isOnlyLetters = code.chars()
                .allMatch(c -> c >= 'A' && c <= 'Z');
        if (!isOnlyLetters) {
            throw new ValidationException("code: '%s' must be only capital latin letters".formatted(code));
        }
    }

    private void validateNameFormat(String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new ValidationException("name: '%s' must be less than %d".formatted(name, MAX_NAME_LENGTH));
        }
    }

    private void validateSignFormat(String sign) {
        if (sign.length() > MAX_SIGN_LENGTH) {
            throw new ValidationException("sign: '%s' must be less than %d symbol".formatted(sign, MAX_SIGN_LENGTH));
        }
    }
}