package com.github.xoaltie.currencyexchange.servlet;

import com.github.xoaltie.currencyexchange.dto.CurrencyRequestDto;
import com.github.xoaltie.currencyexchange.dto.CurrencyResponseDto;
import com.github.xoaltie.currencyexchange.dto.ErrorResponse;
import com.github.xoaltie.currencyexchange.exception.CurrencyAlreadyExistsException;
import com.github.xoaltie.currencyexchange.listener.AppContextKeys;
import com.github.xoaltie.currencyexchange.service.CurrencyService;
import com.github.xoaltie.currencyexchange.validator.CurrencyValidator;
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
        writeJson(resp, currencies, HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CurrencyRequestDto dto = new CurrencyRequestDto(
                req.getParameter("code"),
                req.getParameter("name"),
                req.getParameter("sign")
        );
        CurrencyValidator.validate(dto);

        CurrencyResponseDto newCurrency = currencyService.create(dto);
        writeJson(resp, newCurrency, HttpServletResponse.SC_CREATED);
    }
}
