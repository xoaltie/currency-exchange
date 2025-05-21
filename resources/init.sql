CREATE TABLE IF NOT EXISTS currencies
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    code TEXT UNIQUE NOT NULL,
    full_name TEXT NOT NULL,
    sign TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS exchange_rates (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    base_currency_id INTEGER NOT NULL,
    target_currency_id INTEGER NOT NULL,
    rate REAL NOT NULL,
    UNIQUE (base_currency_id, target_currency_id),
    FOREIGN KEY (base_currency_id) REFERENCES currencies (id),
    FOREIGN KEY (target_currency_id) REFERENCES currencies (id)
);

INSERT INTO currencies (code, full_name, sign) VALUES ("code", "full_name", "sign");
INSERT INTO currencies (code, full_name, sign) VALUES ("123", "full_name", "sign");