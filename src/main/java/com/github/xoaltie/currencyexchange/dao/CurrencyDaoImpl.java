package com.github.xoaltie.currencyexchange.dao;

import com.github.xoaltie.currencyexchange.db.ConnectionPool;
import com.github.xoaltie.currencyexchange.entity.Currency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class CurrencyDaoImpl implements CurrencyDao {
    private static final String FIND_ALL_QUERY = """
            SELECT id, code, full_name, sign
            FROM currencies;
            """;
    private static final String FIND_BY_ID_QUERY = """
            SELECT id, code, full_name, sign
            FROM currencies
            WHERE code = ?;
            """;

    @Override
    public List<Currency> findAll() {
        List<Currency> currencies = new ArrayList<>();

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                currencies.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return currencies;
    }

    @Override
    public Optional<Currency> findByCode(String code) {
        Objects.requireNonNull(code, "code must not be null");

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_QUERY);) {
            preparedStatement.setString(1, code);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Currency currency = mapRow(resultSet);
                    return Optional.of(currency);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    private Currency mapRow(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getInt("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign")
        );
    }
}
