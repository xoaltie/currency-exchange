package com.github.xoaltie.currencyexchange.filter;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum MediaType {
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded"),
    APPLICATION_JSON("application/json");

    private final String value;

    MediaType(String type) {
        this.value = type;
    }

    public boolean is(String type) {
        Objects.requireNonNull(type, "type must not be null");
        return value.equalsIgnoreCase(type);
    }
}
