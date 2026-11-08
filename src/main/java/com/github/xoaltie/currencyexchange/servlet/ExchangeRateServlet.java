package com.github.xoaltie.currencyexchange.servlet;

import com.github.xoaltie.currencyexchange.dto.CurrencyCodePair;
import com.github.xoaltie.currencyexchange.dto.ExchangeRateRequestDto;
import com.github.xoaltie.currencyexchange.dto.ExchangeRateResponseDto;
import com.github.xoaltie.currencyexchange.listener.AppContextKeys;
import com.github.xoaltie.currencyexchange.service.ExchangeRateService;
import com.github.xoaltie.currencyexchange.util.CurrencyCodePairParser;
import com.github.xoaltie.currencyexchange.util.UriParser;
import com.github.xoaltie.currencyexchange.validator.CurrencyValidator;
import com.github.xoaltie.currencyexchange.validator.ExchangeRateValidator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends BaseServlet {
    private ExchangeRateService exchangeRateService;
    private CurrencyValidator currencyValidator;
    private ExchangeRateValidator exchangeRateValidator;

    @Override
    public void init() {
        super.init();
        Object rawExchangeRateService = getServletContext().getAttribute(AppContextKeys.EXCHANGE_RATE_SERVICE);
        Object rawCurrencyValidator = getServletContext().getAttribute(AppContextKeys.CURRENCY_VALIDATOR);
        Object rawExchangeRateValidator = getServletContext().getAttribute(AppContextKeys.EXCHANGE_RATE_VALIDATOR);

        Objects.requireNonNull(rawExchangeRateService, "%s not found in ServletContext".formatted(AppContextKeys.EXCHANGE_RATE_SERVICE));
        if (!(rawExchangeRateService instanceof ExchangeRateService)) {
            throw new IllegalStateException("%s must be %s, got %s instead".formatted(AppContextKeys.EXCHANGE_RATE_SERVICE, ExchangeRateService.class, rawExchangeRateService.getClass().getTypeName()));
        }
        Objects.requireNonNull(rawCurrencyValidator, "%s not found in ServletContext".formatted(AppContextKeys.CURRENCY_VALIDATOR));
        if (!(rawCurrencyValidator instanceof CurrencyValidator)) {
            throw new IllegalStateException("%s must be %s, got %s instead".formatted(AppContextKeys.CURRENCY_VALIDATOR, CurrencyValidator.class, rawCurrencyValidator.getClass().getTypeName()));
        }
        Objects.requireNonNull(rawExchangeRateValidator, "%s not found in ServletContext".formatted(AppContextKeys.EXCHANGE_RATE_VALIDATOR));
        if (!(rawExchangeRateValidator instanceof ExchangeRateValidator)) {
            throw new IllegalStateException("%s must be %s, got %s instead".formatted(AppContextKeys.EXCHANGE_RATE_VALIDATOR, ExchangeRateValidator.class, rawExchangeRateValidator.getClass().getTypeName()));
        }

        exchangeRateService = (ExchangeRateService) rawExchangeRateService;
        currencyValidator = (CurrencyValidator) rawCurrencyValidator;
        exchangeRateValidator = (ExchangeRateValidator) rawExchangeRateValidator;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String codePairRaw = UriParser.parse(req.getPathInfo());
        CurrencyCodePair codePair = CurrencyCodePairParser.parse(codePairRaw);

        currencyValidator.validateCode(codePair.base());
        currencyValidator.validateCode(codePair.target());

        ExchangeRateResponseDto exchangeRate = exchangeRateService.findByCodePair(codePair.base(), codePair.target());
        writeJson(resp, exchangeRate, 200);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String codePairRaw = UriParser.parse(req.getPathInfo());
        CurrencyCodePair codePair = CurrencyCodePairParser.parse(codePairRaw);
        ExchangeRateRequestDto dto = new ExchangeRateRequestDto(
                codePair.base(),
                codePair.target(),
                req.getParameter("rate")
        );

        exchangeRateValidator.validate(dto);

        ExchangeRateResponseDto updatedExchangeRate = exchangeRateService.update(dto.baseCurrencyCode(), dto.targetCurrencyCode(), new BigDecimal(dto.rate()));
        writeJson(resp, updatedExchangeRate, 200);
    }
}
