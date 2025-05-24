<?php

namespace App\Repository;

use App\Database\DatabaseConnection;
use App\DTO\CurrencyCreateDTO;
use App\Models\Currency;
use App\Models\EmptyObject;

final readonly class CurrencyRepository
{
    private const string QUERY_GET_ALL = 'SELECT * FROM currencies';
    private const string QUERY_GET_BY_CODE = 'SELECT * FROM currencies WHERE code = :code';
    private const string QUERY_CREATE_FROM_DTO = 'INSERT INTO currencies(code, full_name, sign) VALUES (:code, :full_name, :sign)';


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

    public function getByCode(string $code): Currency|EmptyObject
    {
        $currency = $this->connection->prepareExec(
            self::QUERY_GET_BY_CODE,
            [
                'code' => $code,
            ],
        );

        if (empty($currency)) {
            return new EmptyObject();
        }

        return new Currency(
            id: $currency[0]['id'],
            code: $currency[0]['code'],
            fullName: $currency[0]['full_name'],
            sign: $currency[0]['sign'],
        );
    }

    public function create(CurrencyCreateDTO $dto): Currency
    {
        $this->connection->prepareExec(
            self::QUERY_CREATE_FROM_DTO,
            [
                'code' => $dto->code,
                'full_name' => $dto->name,
                'sign' => $dto->sign,
            ],
        );

        return $this->getByCode($dto->code);
    }
}
