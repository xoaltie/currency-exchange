package com.github.xoaltie.currencyexchange.util;

import com.github.xoaltie.currencyexchange.exception.ValidationException;

public final class UriParser {
    private UriParser() {
    }

    public static String parse(String uri) {
        if (uri == null) {
            throw new ValidationException("path parameter is missing");
        }

        String[] pathParts = uri.split("/");
        if (pathParts.length != 2) {
            throw new ValidationException("invalid path structure: unexpected segments");
        }

        return pathParts[1];
    }
}
