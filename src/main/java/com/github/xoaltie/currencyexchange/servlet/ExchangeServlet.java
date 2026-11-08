package com.github.xoaltie.currencyexchange.servlet;

import com.github.xoaltie.currencyexchange.dto.ExchangeRequestDto;
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
    private ExchangeValidator exchangeValidator;

    @Override
    public void init() {
        super.init();
        Object rawExchangeService = getServletContext().getAttribute(AppContextKeys.EXCHANGE_SERVICE);
        Object rawExchangeValidator = getServletContext().getAttribute(AppContextKeys.EXCHANGE_VALIDATOR);

        Objects.requireNonNull(rawExchangeService, "%s not found in ServletContext".formatted(AppContextKeys.EXCHANGE_SERVICE));
        if (!(rawExchangeService instanceof ExchangeService)) {
            throw new IllegalStateException("%s must be %s, got %s instead".formatted(AppContextKeys.EXCHANGE_SERVICE, ExchangeService.class, rawExchangeService.getClass().getTypeName()));
        }
        Objects.requireNonNull(rawExchangeValidator, "%s not found in ServletContext".formatted(AppContextKeys.EXCHANGE_VALIDATOR));
        if (!(rawExchangeValidator instanceof ExchangeValidator)) {
            throw new IllegalStateException("%s must be %s, got %s instead".formatted(AppContextKeys.EXCHANGE_VALIDATOR, ExchangeValidator.class, rawExchangeValidator.getClass().getTypeName()));
        }

        exchangeService = (ExchangeService) rawExchangeService;
        exchangeValidator = (ExchangeValidator) rawExchangeValidator;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ExchangeRequestDto dto = new ExchangeRequestDto(
                req.getParameter("from"),
                req.getParameter("to"),
                req.getParameter("amount")
        );

        exchangeValidator.validate(dto);

        ExchangeResponseDto exchange = exchangeService.convert(dto.baseCurrencyCode(), dto.targetCurrencyCode(), new BigDecimal(dto.amount()));
        writeJson(resp, exchange, 200);
    }
}
