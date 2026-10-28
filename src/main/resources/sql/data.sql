INSERT INTO currencies (code, full_name, sign)
VALUES ('USD', 'US Dollar', '$'),
       ('EUR', 'Euro', '€'),
       ('RUB', 'Russian Ruble', '₽'),
       ('GBP', 'British Pound Sterling', '£'),
       ('JPY', 'Japanese Yen', '¥'),
       ('CNY', 'Chinese Yuan', '¥'),
       ('CHF', 'Swiss Franc', 'Fr.'),
       ('AUD', 'Australian Dollar', 'A$'),
       ('CAD', 'Canadian Dollar', 'C$'),
       ('INR', 'Indian Rupee', '₹');

INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate)
VALUES (1, 2, 0.899),
       (1, 3, 80.50),
       (2, 4, 0.837),
       (1, 5, 157.89),
       (1, 6, 6.67);