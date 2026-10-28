package com.github.xoaltie.currencyexchange.entity;

import lombok.Data;

@Data
public final class Currency {
    private final int id;
    private final String code;
    private final String fullName;
    private final String sign;

    public Currency(int id, String code, String fullName, String sign) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }
}
