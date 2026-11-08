package com.github.xoaltie.currencyexchange.filter;

import com.github.xoaltie.currencyexchange.exception.ValidationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebFilter("/*")
public class FormContentFilter extends HttpFilter {
    private static final Set<String> METHODS_WITHOUT_PARSED_BODY = Set.of("PATCH", "PUT");

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String action = req.getMethod();
        if (!METHODS_WITHOUT_PARSED_BODY.contains(action)) {
            chain.doFilter(req, res);
            return;
        }

        String contentTypeRaw = req.getContentType();
        if (contentTypeRaw == null) {
            throw new ValidationException("missing content-type");
        }

        String contentType = contentTypeRaw.split(";")[0].strip();
        if (!MediaType.APPLICATION_FORM_URLENCODED.is(contentType)) {
            chain.doFilter(req, res);
            return;
        }

        FormContentRequestWrapper reqWrapper = new FormContentRequestWrapper(req, parseFormBody(req));

        chain.doFilter(reqWrapper, res);
    }

    private Map<String, String[]> parseFormBody(HttpServletRequest req) throws IOException {
        Map<String, List<String>> parameterListMap = new HashMap<>();
        Map<String, String[]> parameterMap = new HashMap<>();
        String body = new String(req.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        if (body.isBlank()) {
            return parameterMap;
        }

        String[] rawParameters = body.split("&");

        for (String parameter : rawParameters) {
            String[] keyValue = parameter.split("=", 2);

            String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
            String value = keyValue.length > 1
                    ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8)
                    : "";

            parameterListMap.computeIfAbsent(key, _ -> new ArrayList<>()).add(value);
        }

        for (Map.Entry<String, List<String>> entry : parameterListMap.entrySet()) {
            parameterMap.put(entry.getKey(), entry.getValue().toArray(String[]::new));
        }
        return parameterMap;
    }
}
