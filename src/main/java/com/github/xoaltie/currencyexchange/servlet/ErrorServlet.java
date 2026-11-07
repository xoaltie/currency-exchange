package com.github.xoaltie.currencyexchange.servlet;

import com.github.xoaltie.currencyexchange.dto.ErrorResponse;
import com.github.xoaltie.currencyexchange.exception.CurrencyAlreadyExistsException;
import com.github.xoaltie.currencyexchange.exception.CurrencyNotFoundException;
import com.github.xoaltie.currencyexchange.exception.ExchangeRateNotFoundException;
import com.github.xoaltie.currencyexchange.exception.IdenticalCurrencyPairException;
import com.github.xoaltie.currencyexchange.exception.IncorrectContentTypeException;
import com.github.xoaltie.currencyexchange.exception.ValidationException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/error-handler")
public class ErrorServlet extends BaseServlet {
    @Override
    public void init() {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        processError(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        processError(req, resp);
    }

    private void processError(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Throwable throwable = (Throwable) req
                .getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        if (throwable == null) {
            writeJson(resp, new ErrorResponse("the requested endpoint does not exist"), HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (throwable instanceof ValidationException || throwable instanceof IdenticalCurrencyPairException) {
            writeJson(resp, new ErrorResponse(throwable.getMessage()), HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (throwable instanceof IncorrectContentTypeException) {
            writeJson(resp, new ErrorResponse(throwable.getMessage()), HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            return;
        }

        if (throwable instanceof CurrencyNotFoundException || throwable instanceof ExchangeRateNotFoundException) {
            writeJson(resp, new ErrorResponse(throwable.getMessage()), HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (throwable instanceof CurrencyAlreadyExistsException) {
            writeJson(resp, new ErrorResponse(throwable.getMessage()), HttpServletResponse.SC_CONFLICT);
            return;
        }

        writeJson(resp, new ErrorResponse("internal server error"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}
