<?php

namespace App\Repository;

use App\Database\DatabaseConnection;
use App\Models\Currency;

final readonly class CurrencyRepository
{
    private const string QUERY_GET_ALL = 'SELECT * FROM currencies';

    public function __construct(
        private DatabaseConnection $connection,
    ) {}

    /**
     * @return list<Currency>
     */
    public function getAll(): array
    {
        $data = $this->connection->exec(self::QUERY_GET_ALL);

        $currencies = [];

        foreach ($data as $value) {
            $currencies[] = new Currency(
                id: $value['id'],
                code: $value['code'],
                fullName: $value['full_name'],
                sign: $value['sign'],
            );
        }

        return $currencies;
    }
}
