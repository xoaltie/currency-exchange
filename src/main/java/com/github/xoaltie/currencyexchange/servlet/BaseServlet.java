package com.github.xoaltie.currencyexchange.servlet;

import com.github.xoaltie.currencyexchange.listener.AppContextKeys;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Objects;

public abstract class BaseServlet extends HttpServlet {
    protected ObjectMapper objectMapper;

    @Override
    public void init() {
        Object rawObjectMapper = getServletContext().getAttribute(AppContextKeys.OBJECT_MAPPER);

        Objects.requireNonNull(rawObjectMapper, "%s not found in ServletContext".formatted(AppContextKeys.OBJECT_MAPPER));
        if (!(rawObjectMapper instanceof ObjectMapper)) {
            throw new IllegalStateException("%s must be %s, got %s instead".formatted(AppContextKeys.OBJECT_MAPPER, ObjectMapper.class, rawObjectMapper.getClass().getTypeName()));
        }

        objectMapper = (ObjectMapper) rawObjectMapper;
    }

    protected void writeJson(HttpServletResponse resp, Object data, int status) throws IOException {
        resp.setStatus(status);
        objectMapper.writeValue(resp.getWriter(), data);
    }
}
