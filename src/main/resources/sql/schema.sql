CREATE TABLE IF NOT EXISTS currencies
(
    id INTEGER PRIMARY KEY,
    code TEXT NOT NULL UNIQUE,
    full_name TEXT NOT NULL,
    sign TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS exchange_rates
(
    id INTEGER PRIMARY KEY,
    base_currency_id INTEGER NOT NULL,
    target_currency_id INTEGER NOT NULL,
    rate REAL NOT NULL,

    UNIQUE (base_currency_id, target_currency_id),
    FOREIGN KEY (base_currency_id) REFERENCES currencies(id),
    FOREIGN KEY (target_currency_id) REFERENCES currencies(id)
);