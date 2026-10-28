package com.github.xoaltie.currencyexchange.servlet;

import com.github.xoaltie.currencyexchange.dto.CurrencyResponseDto;
import com.github.xoaltie.currencyexchange.listener.AppContextKeys;
import com.github.xoaltie.currencyexchange.service.CurrencyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private CurrencyService currencyService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        Object rawCurrencyService = getServletContext().getAttribute(AppContextKeys.CURRENCY_SERVICE);
        Object rawObjectMapper = getServletContext().getAttribute(AppContextKeys.OBJECT_MAPPER);

        if (rawCurrencyService == null) {
            throw new UnavailableException("%s not found in ServletContext".formatted(AppContextKeys.CURRENCY_SERVICE));
        }
        if (!(rawCurrencyService instanceof CurrencyService)) {
            throw new IllegalStateException("%s must be %s, got %s instead".formatted(AppContextKeys.CURRENCY_SERVICE, CurrencyService.class, rawCurrencyService.getClass().getTypeName()));
        }
        if (rawObjectMapper == null) {
            throw new UnavailableException("%s not found in ServletContext".formatted(AppContextKeys.OBJECT_MAPPER));
        }
        if (!(rawObjectMapper instanceof ObjectMapper)) {
            throw new IllegalStateException("%s must be %s, got %s instead".formatted(AppContextKeys.OBJECT_MAPPER, ObjectMapper.class, rawObjectMapper.getClass().getTypeName()));
        }

        currencyService = (CurrencyService) rawCurrencyService;
        objectMapper = (ObjectMapper) rawObjectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<CurrencyResponseDto> currencies = currencyService.findAll();
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), currencies);
    }
}
