package com.github.xoaltie.currencyexchange.dao;

import com.github.xoaltie.currencyexchange.db.ConnectionPool;
import com.github.xoaltie.currencyexchange.entity.Currency;
import com.github.xoaltie.currencyexchange.exception.CurrencyAlreadyExistsException;
import org.sqlite.SQLiteErrorCode;

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
    private static final String INSERT_QUERY = """
            INSERT INTO currencies(code, full_name, sign)
            VALUES (?, ?, ?)
            RETURNING id, code, full_name, sign;
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
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
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

    @Override
    public Currency create(Currency currency) {
        Objects.requireNonNull(currency, "currency must not be null");

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {
            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getFullName());
            preparedStatement.setString(3, currency.getSign());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return mapRow(resultSet);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT.code) {
                throw new CurrencyAlreadyExistsException("currency with code '%s' already exists".formatted(currency.getCode()));
            }
            throw new RuntimeException(e);
        }
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
