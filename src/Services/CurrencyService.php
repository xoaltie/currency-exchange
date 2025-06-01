<?php

namespace App\Services;

use App\DTO\CurrencyCreateDTO;
use App\DTO\CurrencyDTO;
use App\Exceptions\CurrencyException;
use App\Models\EmptyObject;
use App\Repository\CurrencyRepository;

final readonly class CurrencyService
{
    public function __construct(
        private CurrencyRepository $repository,
    ) {}


    /**
     * @return list<CurrencyDTO>
     */
    public function getAllCurrencies(): array
    {
        $currencies = $this->repository->getAll();

        $currencyDTOs = [];

        foreach ($currencies as $currency) {
            $currencyDTOs[] = new CurrencyDTO(
                id: $currency->id,
                code: $currency->code,
                name: $currency->name,
                sign: $currency->sign,
            );
        }

        return $currencyDTOs;
    }

    /**
     * @param string $code
     * @return CurrencyDTO
     * @throws CurrencyException
     */
    public function getCurrencyByCode(string $code): CurrencyDTO
    {
        $currency = $this->repository->getByCode($code);

        if ($currency instanceof EmptyObject) {
            throw new CurrencyException(
                "Currency with code {$code} don't found.",
                404,
            );
        }

        return new CurrencyDTO(
            id: $currency->id,
            code: $currency->code,
            name: $currency->name,
            sign: $currency->sign,
        );
    }

    public function createCurrency(CurrencyCreateDTO $dto): CurrencyDTO
    {
        $currency = $this->repository->create($dto);

        return new CurrencyDTO(
            id: $currency->id,
            code: $currency->code,
            name: $currency->name,
            sign: $currency->sign,
        );
    }
}
