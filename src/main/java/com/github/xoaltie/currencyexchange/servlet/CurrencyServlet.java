package com.github.xoaltie.currencyexchange.servlet;

import com.github.xoaltie.currencyexchange.dto.CurrencyResponseDto;
import com.github.xoaltie.currencyexchange.dto.ErrorResponse;
import com.github.xoaltie.currencyexchange.exception.CurrencyNotFoundException;
import com.github.xoaltie.currencyexchange.listener.AppContextKeys;
import com.github.xoaltie.currencyexchange.service.CurrencyService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

@WebServlet("/currency/*")
public class CurrencyServlet extends BaseServlet {
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
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            resp.setStatus(400);
            objectMapper.writeValue(resp.getWriter(), new ErrorResponse("invalid uri. must be /currency/{code}"));
            return;
        }

        String[] pathParts = pathInfo.split("/");
        if (pathParts.length != 2) {
            resp.setStatus(400);
            objectMapper.writeValue(resp.getWriter(), new ErrorResponse("invalid uri. must be /currency/{code}"));
            return;
        }

        String code = pathParts[1];

        boolean isOnlyLetters = code.chars()
                .allMatch(Character::isLetter);
        if (!isOnlyLetters) {
            resp.setStatus(400);
            objectMapper.writeValue(resp.getWriter(), new ErrorResponse("code: '%s' must be only letters".formatted(code)));
            return;
        }

        if (code.length() != 3) {
            resp.setStatus(400);
            objectMapper.writeValue(resp.getWriter(), new ErrorResponse("code: '%s' must be 3 letters, got %d instead".formatted(code, code.length())));
            return;
        }

        try {
            CurrencyResponseDto currency = currencyService.findByCode(code);
            resp.setStatus(200);
            objectMapper.writeValue(resp.getWriter(), currency);
        } catch (CurrencyNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            objectMapper.writeValue(resp.getWriter(), new ErrorResponse(e.getMessage()));
        }
    }
}
