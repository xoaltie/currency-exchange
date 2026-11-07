package com.github.xoaltie.currencyexchange.filter;

import com.github.xoaltie.currencyexchange.exception.IncorrectContentTypeException;
import com.github.xoaltie.currencyexchange.exception.ValidationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

@WebFilter(
        urlPatterns = {
                "/currencies",
                "/exchangeRates",
                "/exchangeRate/*"
        }
)
public class ContentTypeValidationFilter extends HttpFilter {
    private static final Set<String> METHODS_WITH_BODY = Set.of("POST", "PATCH");

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String action = req.getMethod();
        if (!METHODS_WITH_BODY.contains(action)) {
            chain.doFilter(req, res);
            return;
        }

        String contentTypeRaw = req.getContentType();
        if (contentTypeRaw == null) {
            throw new ValidationException("missing content-type");
        }

        // TODO: вытащить парсинг в util класс или что то подобное. если обратная связь по ошибке придет в пользу 415 статуса, добавить новый exception
        String contentType = contentTypeRaw.split(";")[0].trim();
        if (!MediaType.APPLICATION_FORM_URLENCODED.is(contentType)) {
            throw new IncorrectContentTypeException("incorrect content-type. content-type must be %s, got %s instead".formatted(
                    MediaType.APPLICATION_FORM_URLENCODED.getValue(),
                    contentType)
            );
        }
        chain.doFilter(req, res);
    }
}
