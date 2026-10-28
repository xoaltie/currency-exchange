package com.github.xoaltie.currencyexchange.listener;

import com.github.xoaltie.currencyexchange.db.ConnectionPool;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import tools.jackson.databind.ObjectMapper;

@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ConnectionPool.init();
        ObjectMapper objectMapper = new ObjectMapper();

        ServletContext context = sce.getServletContext();
        context.setAttribute(AppContextKeys.OBJECT_MAPPER, objectMapper);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionPool.close();
    }
}
