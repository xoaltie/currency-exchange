package com.github.xoaltie.currencyexchange.dao;

import com.github.xoaltie.currencyexchange.db.ConnectionPool;
import com.github.xoaltie.currencyexchange.entity.Currency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class CurrencyDaoImpl implements CurrencyDao {
    private static final String FIND_ALL_CURRENCIES_QUERY = """
            SELECT id, code, full_name, sign
            FROM currencies;
            """;

    @Override
    public List<Currency> findAll() {
        List<Currency> currencies = new ArrayList<>();

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_CURRENCIES_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Currency currency = new Currency(
                        resultSet.getInt("id"),
                        resultSet.getString("code"),
                        resultSet.getString("full_name"),
                        resultSet.getString("sign")
                );
                currencies.add(currency);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return currencies;
    }
}
