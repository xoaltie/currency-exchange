<?php

namespace App\Repository;

use App\Database\DatabaseConnection;
use App\DTO\ExchangeRateCreateDTO;
use App\Models\Currency;
use App\Models\EmptyObject;
use App\Models\ExchangeRate;

final readonly class ExchangeRateRepository
{
    private const string QUERY_GET_ALL = "SELECT 
    er.id,
    base_curr.id as base_currency_id,
    base_curr.code as base_currency_code,
    base_curr.full_name as base_currency_name,
    base_curr.sign as base_currency_sign,
    target_curr.id as target_currency_id,
    target_curr.code as target_currency_code,
    target_curr.full_name as target_currency_name,
    target_curr.sign as target_currency_sign,
    er.rate
FROM exchange_rates er
JOIN currencies base_curr ON er.base_currency_id = base_curr.id
JOIN currencies target_curr ON er.target_currency_id = target_curr.id;";

    private const string QUERY_GET_BY_CODES = "
    SELECT 
    er.id,
    base_curr.id as base_currency_id,
    base_curr.code as base_currency_code,
    base_curr.full_name as base_currency_name,
    base_curr.sign as base_currency_sign,
    target_curr.id as target_currency_id,
    target_curr.code as target_currency_code,
    target_curr.full_name as target_currency_name,
    target_curr.sign as target_currency_sign,
    er.rate
FROM exchange_rates er
JOIN currencies base_curr ON er.base_currency_id = base_curr.id
JOIN currencies target_curr ON er.target_currency_id = target_curr.id
WHERE base_curr.code = :base_currency_code
AND target_curr.code = :target_currency_code;";

    private const string QUERY_CREATE_BY_DTO = "
        INSERT
        INTO exchange_rates (base_currency_id,
                         target_currency_id,
                         rate)
        VALUES (:base_currency_id,
            :target_currency_id,
            :rate);";

    private const QUERY_UPDATE_WITH_RATE = "
        UPDATE exchange_rates
        SET rate = :rate
        WHERE id = :id;";

    private const QUERY_GET_BY_CODES_CROSS = "
        SELECT
            base_curr.id AS base_currency_id,
            base_curr.code AS base_currency_code,
            base_curr.full_name AS base_currency_name,
            base_curr.sign AS base_currency_sign,
            target_curr.id AS target_currency_id,
            target_curr.code AS target_currency_code,
            target_curr.full_name AS target_currency_name,
            target_curr.sign AS target_currency_sign,
            er.rate
        FROM
            exchange_rates er
        JOIN currencies base_curr ON
            er.base_currency_id = base_curr.id
        JOIN currencies target_curr ON
            er.target_currency_id = target_curr.id
        WHERE
            er.base_currency_id IN (
            SELECT
                base_currency_id
            FROM
                exchange_rates er
            JOIN currencies base_curr ON
                er.base_currency_id = base_curr.id
            JOIN currencies target_curr ON
                er.target_currency_id = target_curr.id
            WHERE
                target_curr.code IN (:base_currency_code, :target_currency_code)
            GROUP BY
                er.base_currency_id
            HAVING
                COUNT(DISTINCT er.target_currency_id) > 1
        )
            AND target_curr.code IN (:base_currency_code, :target_currency_code)
        ORDER BY
            er.base_currency_id,
            target_curr.code = :base_currency_code desc
        LIMIT 2;";

    public function __construct(
        private DatabaseConnection $connection,
        private CurrencyRepository $currencyRepository,
    ) {}

    /**
     * @return list<ExchangeRate>
     */
    public function getAll(): array
    {
        $data = $this->connection->exec(self::QUERY_GET_ALL);

        $exchangeRates = [];

        foreach ($data as $value) {
            $exchangeRates[] = new ExchangeRate(
                id: $value['id'],
                baseCurrency: new Currency(
                    id: $value['base_currency_id'],
                    code: $value['base_currency_code'],
                    name: $value['base_currency_name'],
                    sign: $value['base_currency_sign'],
                ),
                targetCurrency: new Currency(
                    id: $value['target_currency_id'],
                    code: $value['target_currency_code'],
                    name: $value['target_currency_name'],
                    sign: $value['target_currency_sign'],
                ),
                rate: $value['rate'],
            );
        }

        return $exchangeRates;
    }

    public function getByCodes(string $baseCurrencyCode, string $targetCurrencyCode): ExchangeRate|EmptyObject
    {
        $exchangeRate = $this->connection->prepareExec(
            self::QUERY_GET_BY_CODES,
            [
                'base_currency_code' => $baseCurrencyCode,
                'target_currency_code' => $targetCurrencyCode,
            ],
        );

        if (empty($exchangeRate)) {
            return new EmptyObject();
        }

        return new ExchangeRate(
            id: $exchangeRate[0]['id'],
            baseCurrency: new Currency(
                id: $exchangeRate[0]['base_currency_id'],
                code: $exchangeRate[0]['base_currency_code'],
                name: $exchangeRate[0]['base_currency_name'],
                sign: $exchangeRate[0]['base_currency_sign'],
            ),
            targetCurrency: new Currency(
                id: $exchangeRate[0]['target_currency_id'],
                code: $exchangeRate[0]['target_currency_code'],
                name: $exchangeRate[0]['target_currency_name'],
                sign: $exchangeRate[0]['target_currency_sign'],
            ),
            rate: $exchangeRate[0]['rate'],
        );
    }

    public function getCrossByCodes(string $baseCurrencyCode, string $targetCurrencyCode)
    {
        $exchangeRateCross = $this->connection->prepareExec(
            self::QUERY_GET_BY_CODES_CROSS,
            [
                'base_currency_code' => $baseCurrencyCode,
                'target_currency_code' => $targetCurrencyCode,
            ],
        );

        if (empty($exchangeRateCross)) {
            return new EmptyObject();
        }

        return new ExchangeRate(
            id: 0,
            baseCurrency: new Currency(
                id: $exchangeRateCross[0]['target_currency_id'],
                code: $exchangeRateCross[0]['target_currency_code'],
                name: $exchangeRateCross[0]['target_currency_name'],
                sign: $exchangeRateCross[0]['target_currency_sign'],
            ),
            targetCurrency: new Currency(
                id: $exchangeRateCross[1]['target_currency_id'],
                code: $exchangeRateCross[1]['target_currency_code'],
                name: $exchangeRateCross[1]['target_currency_name'],
                sign: $exchangeRateCross[1]['target_currency_sign'],
            ),
            rate: (1 / $exchangeRateCross[0]['rate']) * $exchangeRateCross[1]['rate'],
        );
    }

    public function create(ExchangeRateCreateDTO $dto): ExchangeRate|EmptyObject
    {
        $baseCurrency = $this->currencyRepository->getByCode($dto->baseCurrencyCode);
        $targetCurrency = $this->currencyRepository->getByCode($dto->targetCurrencyCode);

        if ($baseCurrency instanceof EmptyObject || $targetCurrency instanceof EmptyObject) {
            return new EmptyObject();
        }

        $this->connection->prepareExec(
            self::QUERY_CREATE_BY_DTO,
            [
                'base_currency_id' => $baseCurrency->id,
                'target_currency_id' => $targetCurrency->id,
                'rate' => $dto->rate,
            ],
        );

        return $this->getByCodes($baseCurrency->code, $targetCurrency->code);
    }

    public function update(string $baseCurrencyCode, string $targetCurrencyCode, float $rate): ExchangeRate|EmptyObject
    {
        $exchangeRate = $this->getByCodes($baseCurrencyCode, $targetCurrencyCode);

        if ($exchangeRate instanceof EmptyObject) {
            return new EmptyObject();
        }

        $this->connection->prepareExec(
            self::QUERY_UPDATE_WITH_RATE,
            [
                'id' => $exchangeRate->id,
                'rate' => $rate,
            ],
        );

        return $this->getByCodes($baseCurrencyCode, $targetCurrencyCode);
    }
}
