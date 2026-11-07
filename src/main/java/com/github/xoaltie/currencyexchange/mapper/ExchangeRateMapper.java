package com.github.xoaltie.currencyexchange.mapper;

import com.github.xoaltie.currencyexchange.dto.ExchangeRateResponseDto;
import com.github.xoaltie.currencyexchange.entity.Currency;
import com.github.xoaltie.currencyexchange.entity.ExchangeRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

@Mapper(uses = {CurrencyMapper.class})
public interface ExchangeRateMapper {
    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);

    ExchangeRateResponseDto toDto(ExchangeRate exchangeRate);

    List<ExchangeRateResponseDto> toDtoList(List<ExchangeRate> exchangeRates);

    @Mapping(target = "id", ignore = true)
    ExchangeRate toEntity(Currency baseCurrency, Currency targetCurrency, BigDecimal rate);
}
