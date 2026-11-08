package com.github.xoaltie.currencyexchange.mapper;

import com.github.xoaltie.currencyexchange.dto.ExchangeResponseDto;
import com.github.xoaltie.currencyexchange.entity.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

@Mapper(uses = {CurrencyMapper.class})
public interface ExchangeMapper {
    ExchangeMapper INSTANCE = Mappers.getMapper(ExchangeMapper.class);

    ExchangeResponseDto toDto(Currency baseCurrency, Currency targetCurrency, BigDecimal rate, BigDecimal amount, BigDecimal convertedAmount);
}
