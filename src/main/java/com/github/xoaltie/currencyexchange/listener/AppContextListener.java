package com.github.xoaltie.currencyexchange.listener;

import com.github.xoaltie.currencyexchange.dao.CurrencyDao;
import com.github.xoaltie.currencyexchange.dao.CurrencyDaoImpl;
import com.github.xoaltie.currencyexchange.dao.ExchangeRateDao;
import com.github.xoaltie.currencyexchange.dao.ExchangeRateDaoImpl;
import com.github.xoaltie.currencyexchange.db.ConnectionPool;
import com.github.xoaltie.currencyexchange.service.CurrencyService;
import com.github.xoaltie.currencyexchange.service.ExchangeRateService;
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
        CurrencyDao currencyDao = new CurrencyDaoImpl();
        CurrencyService currencyService = new CurrencyService(currencyDao);
        ExchangeRateDao exchangeRateDao = new ExchangeRateDaoImpl();
        ExchangeRateService exchangeRateService = new ExchangeRateService(exchangeRateDao, currencyDao);

        ServletContext context = sce.getServletContext();
        context.setAttribute(AppContextKeys.OBJECT_MAPPER, objectMapper);
        context.setAttribute(AppContextKeys.CURRENCY_SERVICE, currencyService);
        context.setAttribute(AppContextKeys.EXCHANGE_RATE_SERVICE, exchangeRateService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionPool.close();
    }
}
