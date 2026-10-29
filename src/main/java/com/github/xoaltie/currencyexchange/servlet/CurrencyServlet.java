package com.github.xoaltie.currencyexchange.servlet;

import com.github.xoaltie.currencyexchange.dto.CurrencyResponseDto;
import com.github.xoaltie.currencyexchange.dto.ErrorResponse;
import com.github.xoaltie.currencyexchange.exception.CurrencyNotFoundException;
import com.github.xoaltie.currencyexchange.listener.AppContextKeys;
import com.github.xoaltie.currencyexchange.service.CurrencyService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Objects;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private CurrencyService currencyService;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        Object rawCurrencyService = getServletContext().getAttribute(AppContextKeys.CURRENCY_SERVICE);
        Object rawObjectMapper = getServletContext().getAttribute(AppContextKeys.OBJECT_MAPPER);

        Objects.requireNonNull(rawCurrencyService, "%s not found in ServletContext".formatted(AppContextKeys.CURRENCY_SERVICE));
        Objects.requireNonNull(rawObjectMapper, "%s not found in ServletContext".formatted(AppContextKeys.OBJECT_MAPPER));
        if (!(rawCurrencyService instanceof CurrencyService)) {
            throw new IllegalStateException("%s must be %s, got %s instead".formatted(AppContextKeys.CURRENCY_SERVICE, CurrencyService.class, rawCurrencyService.getClass().getTypeName()));
        }
        if (!(rawObjectMapper instanceof ObjectMapper)) {
            throw new IllegalStateException("%s must be %s, got %s instead".formatted(AppContextKeys.OBJECT_MAPPER, ObjectMapper.class, rawObjectMapper.getClass().getTypeName()));
        }

        currencyService = (CurrencyService) rawCurrencyService;
        objectMapper = (ObjectMapper) rawObjectMapper;
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
