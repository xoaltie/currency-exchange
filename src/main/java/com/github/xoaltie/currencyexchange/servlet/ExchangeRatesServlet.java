package com.github.xoaltie.currencyexchange.servlet;

import com.github.xoaltie.currencyexchange.dto.ExchangeRateResponseDto;
import com.github.xoaltie.currencyexchange.listener.AppContextKeys;
import com.github.xoaltie.currencyexchange.service.ExchangeRateService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends BaseServlet {
    private ExchangeRateService exchangeRateService;

    @Override
    public void init() {
        super.init();
        Object rawExchangeRateService = getServletContext().getAttribute(AppContextKeys.EXCHANGE_RATE_SERVICE);

        Objects.requireNonNull(rawExchangeRateService, "%s not found in ServletContext".formatted(AppContextKeys.EXCHANGE_RATE_SERVICE));
        if (!(rawExchangeRateService instanceof ExchangeRateService)) {
            throw new IllegalStateException("%s must be %s, got %s instead".formatted(AppContextKeys.EXCHANGE_RATE_SERVICE, ExchangeRateService.class, rawExchangeRateService.getClass().getTypeName()));
        }

        exchangeRateService = (ExchangeRateService) rawExchangeRateService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<ExchangeRateResponseDto> exchangeRates = exchangeRateService.findAll();
        writeJson(resp, exchangeRates, HttpServletResponse.SC_OK);
    }
}
