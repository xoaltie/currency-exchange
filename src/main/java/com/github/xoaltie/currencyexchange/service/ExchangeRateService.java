package com.github.xoaltie.currencyexchange.service;

import com.github.xoaltie.currencyexchange.dao.ExchangeRateDao;
import com.github.xoaltie.currencyexchange.dto.ExchangeRateResponseDto;
import com.github.xoaltie.currencyexchange.entity.ExchangeRate;
import com.github.xoaltie.currencyexchange.mapper.ExchangeRateMapper;

import java.util.List;
import java.util.Objects;

public final class ExchangeRateService {
    private final ExchangeRateDao exchangeRateDao;

    public ExchangeRateService(ExchangeRateDao exchangeRateDao) {
        Objects.requireNonNull(exchangeRateDao, "exchange rate dao must not be null");
        this.exchangeRateDao = exchangeRateDao;
    }

    public List<ExchangeRateResponseDto> findAll() {
        List<ExchangeRate> exchangeRates = exchangeRateDao.findAll();
        return ExchangeRateMapper.INSTANCE.toDtoList(exchangeRates);
    }
}
