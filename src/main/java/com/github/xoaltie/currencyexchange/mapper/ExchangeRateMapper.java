package com.github.xoaltie.currencyexchange.mapper;

import com.github.xoaltie.currencyexchange.dto.ExchangeRateResponseDto;
import com.github.xoaltie.currencyexchange.entity.ExchangeRate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {CurrencyMapper.class})
public interface ExchangeRateMapper {
    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);

    ExchangeRateResponseDto toDto(ExchangeRate exchangeRate);

    List<ExchangeRateResponseDto> toDtoList(List<ExchangeRate> exchangeRates);
}
