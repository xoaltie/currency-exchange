package com.github.xoaltie.currencyexchange.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class FormContentRequestWrapper extends HttpServletRequestWrapper {
    private final Map<String, String[]> parameters;

    public FormContentRequestWrapper(HttpServletRequest request, Map<String, String[]> parameters) {
        super(request);
        this.parameters = parameters;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return parameters;
    }

    @Override
    public String[] getParameterValues(String name) {
        return Optional.ofNullable(getParameterMap().get(name))
                .map(values -> Arrays.copyOf(values, values.length))
                .orElse(null);
    }

    @Override
    public String getParameter(String name) {
        return Optional.ofNullable(getParameterValues(name))
                .map(values -> values[0])
                .orElse(null);
    }
}
