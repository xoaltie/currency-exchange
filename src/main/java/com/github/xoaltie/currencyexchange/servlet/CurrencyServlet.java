package com.github.xoaltie.currencyexchange.servlet;

import com.github.xoaltie.currencyexchange.dto.Response;
import com.github.xoaltie.currencyexchange.listener.AppContextKeys;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@WebServlet("/test")
public class CurrencyServlet extends HttpServlet {
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        Object rawObjectMapper = getServletContext().getAttribute(AppContextKeys.OBJECT_MAPPER);

        if (rawObjectMapper == null) {
            throw new UnavailableException("%s not found in ServletContext".formatted(AppContextKeys.OBJECT_MAPPER));
        }
        if (!(rawObjectMapper instanceof ObjectMapper)) {
            throw new IllegalStateException("%s must be %s, got %s instead".formatted(AppContextKeys.OBJECT_MAPPER, ObjectMapper.class, rawObjectMapper.getClass().getTypeName()));
        }

        objectMapper = (ObjectMapper) rawObjectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Response response = new Response("хуй");
        resp.setStatus(200);
        objectMapper.writeValue(resp.getWriter(), response);
    }
}
