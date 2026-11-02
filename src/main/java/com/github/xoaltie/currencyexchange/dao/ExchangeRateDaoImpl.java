package com.github.xoaltie.currencyexchange.dao;

import com.github.xoaltie.currencyexchange.db.ConnectionPool;
import com.github.xoaltie.currencyexchange.entity.Currency;
import com.github.xoaltie.currencyexchange.entity.ExchangeRate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
