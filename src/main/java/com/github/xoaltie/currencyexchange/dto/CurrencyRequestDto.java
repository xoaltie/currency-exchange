package com.github.xoaltie.currencyexchange.dto;

public record CurrencyRequestDto(String code, String name, String sign) {
    public CurrencyRequestDto(String code, String name, String sign) {
        this.code = code == null ? null : code.trim().toUpperCase();
        this.name = name == null ? null : name.trim();
        this.sign = sign == null ? null : sign.trim();
    }
}
