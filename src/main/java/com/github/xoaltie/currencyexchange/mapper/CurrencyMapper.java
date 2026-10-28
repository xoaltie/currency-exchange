package com.github.xoaltie.currencyexchange.mapper;

import com.github.xoaltie.currencyexchange.dto.CurrencyResponseDto;
import com.github.xoaltie.currencyexchange.entity.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CurrencyMapper {
    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    @Mapping(source = "fullName", target = "name")
    CurrencyResponseDto toDto(Currency currency);

    List<CurrencyResponseDto> toDtoList(List<Currency> currencies);
}
