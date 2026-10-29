package com.github.xoaltie.currencyexchange.servlet;

import com.github.xoaltie.currencyexchange.dto.CurrencyResponseDto;
import com.github.xoaltie.currencyexchange.listener.AppContextKeys;
import com.github.xoaltie.currencyexchange.service.CurrencyService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@WebServlet("/currencies")
public class CurrenciesServlet extends BaseServlet {
    private CurrencyService currencyService;

    @Override
    public void init() {
        super.init();
        Object rawCurrencyService = getServletContext().getAttribute(AppContextKeys.CURRENCY_SERVICE);

        Objects.requireNonNull(rawCurrencyService, "%s not found in ServletContext".formatted(AppContextKeys.CURRENCY_SERVICE));
        if (!(rawCurrencyService instanceof CurrencyService)) {
            throw new IllegalStateException("%s must be %s, got %s instead".formatted(AppContextKeys.CURRENCY_SERVICE, CurrencyService.class, rawCurrencyService.getClass().getTypeName()));
        }

        currencyService = (CurrencyService) rawCurrencyService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<CurrencyResponseDto> currencies = currencyService.findAll();
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), currencies);
    }
}
