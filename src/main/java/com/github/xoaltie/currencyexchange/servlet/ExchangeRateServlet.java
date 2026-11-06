package com.github.xoaltie.currencyexchange.servlet;

import com.github.xoaltie.currencyexchange.dto.CurrencyCodePair;
import com.github.xoaltie.currencyexchange.dto.ExchangeRateResponseDto;
import com.github.xoaltie.currencyexchange.listener.AppContextKeys;
import com.github.xoaltie.currencyexchange.service.ExchangeRateService;
import com.github.xoaltie.currencyexchange.util.CurrencyCodePairParser;
import com.github.xoaltie.currencyexchange.util.UriParser;
import com.github.xoaltie.currencyexchange.validator.CurrencyValidator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends BaseServlet {
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
        String codePairRaw = UriParser.parse(req.getPathInfo());
        CurrencyCodePair codePair = CurrencyCodePairParser.parse(codePairRaw);

        CurrencyValidator.validateCode(codePair.base());
        CurrencyValidator.validateCode(codePair.target());

        ExchangeRateResponseDto exchangeRate = exchangeRateService.findByCodePair(codePair.base(), codePair.target());
        writeJson(resp, exchangeRate, 200);
    }
}
