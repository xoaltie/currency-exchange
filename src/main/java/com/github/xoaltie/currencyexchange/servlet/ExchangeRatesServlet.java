package com.github.xoaltie.currencyexchange.servlet;

import com.github.xoaltie.currencyexchange.dto.ExchangeRateRequestDto;
import com.github.xoaltie.currencyexchange.dto.ExchangeRateResponseDto;
import com.github.xoaltie.currencyexchange.listener.AppContextKeys;
import com.github.xoaltie.currencyexchange.service.ExchangeRateService;
import com.github.xoaltie.currencyexchange.validator.ExchangeRateValidator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends BaseServlet {
    private ExchangeRateService exchangeRateService;
    private ExchangeRateValidator exchangeRateValidator;

    @Override
    public void init() {
        super.init();
        Object rawExchangeRateService = getServletContext().getAttribute(AppContextKeys.EXCHANGE_RATE_SERVICE);
        Object rawExchangeRateValidator = getServletContext().getAttribute(AppContextKeys.EXCHANGE_RATE_VALIDATOR);

        Objects.requireNonNull(rawExchangeRateService, "%s not found in ServletContext".formatted(AppContextKeys.EXCHANGE_RATE_SERVICE));
        if (!(rawExchangeRateService instanceof ExchangeRateService)) {
            throw new IllegalStateException("%s must be %s, got %s instead".formatted(AppContextKeys.EXCHANGE_RATE_SERVICE, ExchangeRateService.class, rawExchangeRateService.getClass().getTypeName()));
        }
        Objects.requireNonNull(rawExchangeRateValidator, "%s not found in ServletContext".formatted(AppContextKeys.EXCHANGE_RATE_VALIDATOR));
        if (!(rawExchangeRateValidator instanceof ExchangeRateValidator)) {
            throw new IllegalStateException("%s must be %s, got %s instead".formatted(AppContextKeys.EXCHANGE_RATE_VALIDATOR, ExchangeRateValidator.class, rawExchangeRateValidator.getClass().getTypeName()));
        }

        exchangeRateService = (ExchangeRateService) rawExchangeRateService;
        exchangeRateValidator = (ExchangeRateValidator) rawExchangeRateValidator;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<ExchangeRateResponseDto> exchangeRates = exchangeRateService.findAll();
        writeJson(resp, exchangeRates, HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ExchangeRateRequestDto dto = new ExchangeRateRequestDto(
                req.getParameter("baseCurrencyCode"),
                req.getParameter("targetCurrencyCode"),
                req.getParameter("rate")
        );
        exchangeRateValidator.validate(dto);

        ExchangeRateResponseDto newExchangeRate = exchangeRateService.create(dto.baseCurrencyCode(), dto.targetCurrencyCode(), new BigDecimal(dto.rate()));
        writeJson(resp, newExchangeRate, HttpServletResponse.SC_CREATED);
    }
}
