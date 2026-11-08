package com.github.xoaltie.currencyexchange.service;

import com.github.xoaltie.currencyexchange.dao.CurrencyDao;
import com.github.xoaltie.currencyexchange.dto.CurrencyResponseDto;
import com.github.xoaltie.currencyexchange.entity.Currency;
import com.github.xoaltie.currencyexchange.exception.CurrencyNotFoundException;
import com.github.xoaltie.currencyexchange.mapper.CurrencyMapper;

import java.util.List;
import java.util.Objects;

public final class CurrencyService {
    private final CurrencyDao currencyDao;

    public CurrencyService(CurrencyDao currencyDao) {
        Objects.requireNonNull(currencyDao, "currency dao must not be null");
        this.currencyDao = currencyDao;
    }

    public List<CurrencyResponseDto> findAll() {
        List<Currency> currencies = currencyDao.findAll();
        return CurrencyMapper.INSTANCE.toDtoList(currencies);
    }

    public CurrencyResponseDto findByCode(String code) {
        Objects.requireNonNull(code, "code must be not null");
        Currency currency = currencyDao.findByCode(code)
                .orElseThrow(() -> new CurrencyNotFoundException("currency %s does not exist".formatted(code)));
        return CurrencyMapper.INSTANCE.toDto(currency);
    }

    public CurrencyResponseDto create(String code, String fullName, String sign) {
        Objects.requireNonNull(code, "code must be not null");
        Objects.requireNonNull(fullName, "full name must be not null");
        Objects.requireNonNull(sign, "sign must be not null");

        Currency newCurrency;
        newCurrency = currencyDao.create(CurrencyMapper.INSTANCE.toEntity(code, fullName, sign));
        return CurrencyMapper.INSTANCE.toDto(newCurrency);
    }
}
