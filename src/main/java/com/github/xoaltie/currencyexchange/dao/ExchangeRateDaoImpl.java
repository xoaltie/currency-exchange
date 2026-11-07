package com.github.xoaltie.currencyexchange.dao;

import com.github.xoaltie.currencyexchange.db.ConnectionPool;
import com.github.xoaltie.currencyexchange.entity.Currency;
import com.github.xoaltie.currencyexchange.entity.ExchangeRate;
import com.github.xoaltie.currencyexchange.exception.ExchangeRateAlreadyExistsException;
import org.sqlite.SQLiteErrorCode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class ExchangeRateDaoImpl implements ExchangeRateDao {
    private static final String FIND_ALL_QUERY = """
            SELECT er.id       AS id,
                   b.id        AS base_currency_id,
                   b.code      AS base_currency_code,
                   b.full_name AS base_currency_full_name,
                   b.sign      AS base_currency_sign,
                   t.id        AS target_currency_id,
                   t.code      AS target_currency_code,
                   t.full_name AS target_currency_full_name,
                   t.sign      AS target_currency_sign,
                   er.rate     AS rate
            FROM exchange_rates AS er
                     JOIN currencies AS b
                          ON er.base_currency_id = b.id
                     JOIN currencies AS t
                          ON er.target_currency_id = t.id;
            """;
    private static final String FIND_BY_CODE_PAIR_QUERY = """
            SELECT er.id       AS id,
                   b.id        AS base_currency_id,
                   b.code      AS base_currency_code,
                   b.full_name AS base_currency_full_name,
                   b.sign      AS base_currency_sign,
                   t.id        AS target_currency_id,
                   t.code      AS target_currency_code,
                   t.full_name AS target_currency_full_name,
                   t.sign      AS target_currency_sign,
                   er.rate     AS rate
            FROM exchange_rates AS er
                     JOIN currencies AS b
                          ON er.base_currency_id = b.id AND b.code = ?
                     JOIN currencies AS t
                          ON er.target_currency_id = t.id AND t.code = ?;
            """;
    private static final String INSERT_QUERY = """
            INSERT INTO exchange_rates(base_currency_id, target_currency_id, rate)
            VALUES (?, ?, ?)
            RETURNING id;
            """;

    @Override
    public List<ExchangeRate> findAll() {
        List<ExchangeRate> exchangeRates = new ArrayList<>();

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                exchangeRates.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return exchangeRates;
    }

    @Override
    public Optional<ExchangeRate> findByCodePair(String baseCurrencyCode, String targetCurrencyCode) {
        Objects.requireNonNull(baseCurrencyCode, "base currency code must not be null");
        Objects.requireNonNull(targetCurrencyCode, "target currency code must not be null");

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODE_PAIR_QUERY)) {
            preparedStatement.setString(1, baseCurrencyCode);
            preparedStatement.setString(2, targetCurrencyCode);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    ExchangeRate exchangeRate = mapRow(resultSet);
                    return Optional.of(exchangeRate);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public ExchangeRate create(ExchangeRate exchangeRate) {
        Objects.requireNonNull(exchangeRate, "exchange rate must not be null");

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {
            preparedStatement.setInt(1, exchangeRate.getBaseCurrency().getId());
            preparedStatement.setInt(2, exchangeRate.getTargetCurrency().getId());
            preparedStatement.setBigDecimal(3, exchangeRate.getRate());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return new ExchangeRate(
                        resultSet.getInt("id"),
                        exchangeRate.getBaseCurrency(),
                        exchangeRate.getTargetCurrency(),
                        exchangeRate.getRate()
                );
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT.code) {
                throw new ExchangeRateAlreadyExistsException(
                        "exchange rate with codes '%s' and '%s' already exists".formatted(exchangeRate.getBaseCurrency().getCode(), exchangeRate.getTargetCurrency().getCode())
                );
            }
            throw new RuntimeException(e);
        }
    }

    private ExchangeRate mapRow(ResultSet resultSet) throws SQLException {
        Currency baseCurrency = new Currency(
                resultSet.getInt("base_currency_id"),
                resultSet.getString("base_currency_code"),
                resultSet.getString("base_currency_full_name"),
                resultSet.getString("base_currency_sign")
        );
        Currency targetCurrency = new Currency(
                resultSet.getInt("target_currency_id"),
                resultSet.getString("target_currency_code"),
                resultSet.getString("target_currency_full_name"),
                resultSet.getString("target_currency_sign")
        );

        return new ExchangeRate(
                resultSet.getInt("id"),
                baseCurrency,
                targetCurrency,
                resultSet.getBigDecimal("rate")
        );
    }
}
