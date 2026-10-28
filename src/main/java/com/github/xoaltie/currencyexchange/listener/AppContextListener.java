package com.github.xoaltie.currencyexchange.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import tools.jackson.databind.ObjectMapper;

@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ObjectMapper objectMapper = new ObjectMapper();

        ServletContext context = sce.getServletContext();
        context.setAttribute(AppContextKeys.OBJECT_MAPPER, objectMapper);
    }
}
