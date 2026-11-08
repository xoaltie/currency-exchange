package com.github.xoaltie.currencyexchange.service;

import com.github.xoaltie.currencyexchange.dao.ExchangeRateDao;
import com.github.xoaltie.currencyexchange.dto.ExchangeResponseDto;
import com.github.xoaltie.currencyexchange.entity.ExchangeRate;
import com.github.xoaltie.currencyexchange.exception.ExchangeRateNotFoundException;
import com.github.xoaltie.currencyexchange.exception.IdenticalCurrencyPairException;
import com.github.xoaltie.currencyexchange.mapper.ExchangeMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Optional;

public final class ExchangeService {
    private static final int RATE_SCALE = 6;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
    private static final String CROSS_CURRENCY = "USD";
    private final ExchangeRateDao exchangeRateDao;

    public ExchangeService(ExchangeRateDao exchangeRateDao) {
        this.exchangeRateDao = exchangeRateDao;
    }

    public ExchangeResponseDto convert(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        Objects.requireNonNull(baseCurrencyCode, "base currency code must not be null");
        Objects.requireNonNull(targetCurrencyCode, "target currency code must not be null");

        if (baseCurrencyCode.equals(targetCurrencyCode)) {
            throw new IdenticalCurrencyPairException("base and target currency code pairs must not be the same: %s".formatted(baseCurrencyCode));
        }

        Optional<ExchangeRate> direct = exchangeRateDao.findByCodePair(baseCurrencyCode, targetCurrencyCode);
        if (direct.isPresent()) {
            ExchangeRate exchangeRate = direct.get();
            BigDecimal convertedAmount = exchangeRate.getRate()
                    .multiply(amount);
            return ExchangeMapper.INSTANCE.toDto(
                    exchangeRate.getBaseCurrency(),
                    exchangeRate.getTargetCurrency(),
                    exchangeRate.getRate(),
                    amount,
                    convertedAmount
            );
        }

        Optional<ExchangeRate> reverse = exchangeRateDao.findByCodePair(targetCurrencyCode, baseCurrencyCode);
        if (reverse.isPresent()) {
            ExchangeRate exchangeRate = reverse.get();
            BigDecimal rate = BigDecimal.ONE.divide(exchangeRate.getRate(), RATE_SCALE, ROUNDING_MODE);
            BigDecimal convertedAmount = rate.multiply(amount);
            return ExchangeMapper.INSTANCE.toDto(
                    exchangeRate.getTargetCurrency(),
                    exchangeRate.getBaseCurrency(),
                    rate,
                    amount,
                    convertedAmount
            );
        }

        Optional<ExchangeRate> crossBase = exchangeRateDao.findByCodePair(CROSS_CURRENCY, baseCurrencyCode);
        Optional<ExchangeRate> crossTarget = exchangeRateDao.findByCodePair(CROSS_CURRENCY, targetCurrencyCode);
        if (crossBase.isPresent() && crossTarget.isPresent()) {
            ExchangeRate exchangeRateBase = crossBase.get();
            ExchangeRate exchangeRateTarget = crossTarget.get();

            BigDecimal rate = exchangeRateTarget.getRate()
                    .divide(exchangeRateBase.getRate(), RATE_SCALE, ROUNDING_MODE);
            BigDecimal convertedAmount = rate.multiply(amount);
            return ExchangeMapper.INSTANCE.toDto(
                    exchangeRateBase.getTargetCurrency(),
                    exchangeRateTarget.getTargetCurrency(),
                    rate,
                    amount,
                    convertedAmount
            );
        }

        throw new ExchangeRateNotFoundException("exchange rate %s does not exist".formatted(baseCurrencyCode + targetCurrencyCode));
    }
}
