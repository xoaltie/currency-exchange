package com.github.xoaltie.currencyexchange.servlet;

import com.github.xoaltie.currencyexchange.dto.ExchangeResponseDto;
import com.github.xoaltie.currencyexchange.listener.AppContextKeys;
import com.github.xoaltie.currencyexchange.service.ExchangeService;
import com.github.xoaltie.currencyexchange.validator.ExchangeValidator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

@WebServlet("/exchange")
public class ExchangeServlet extends BaseServlet {
    private ExchangeService exchangeService;

    @Override
    public void init() {
        super.init();
        Object rawExchangeService = getServletContext().getAttribute(AppContextKeys.EXCHANGE_SERVICE);

        Objects.requireNonNull(rawExchangeService, "%s not found in ServletContext".formatted(AppContextKeys.EXCHANGE_SERVICE));
        if (!(rawExchangeService instanceof ExchangeService)) {
            throw new IllegalStateException("%s must be %s, got %s instead".formatted(AppContextKeys.EXCHANGE_SERVICE, ExchangeService.class, rawExchangeService.getClass().getTypeName()));
        }

        exchangeService = (ExchangeService) rawExchangeService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        String amountRaw = req.getParameter("amount");

        ExchangeValidator.validate(baseCurrencyCode, targetCurrencyCode, amountRaw);

        ExchangeResponseDto exchange = exchangeService.convert(baseCurrencyCode, targetCurrencyCode, new BigDecimal(amountRaw));
        writeJson(resp, exchange, 200);
    }
}
