package com.github.xoaltie.currencyexchange.dto;

public record CurrencyRequestDto(String code, String name, String sign) {
    public CurrencyRequestDto {
        code = code == null ? null : code.strip();
        name = name == null ? null : name.strip();
        sign = sign == null ? null : sign.strip();
    }
}
