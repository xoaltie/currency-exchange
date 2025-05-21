<?php

namespace App\Services;

use App\DTO\CurrencyDTO;
use App\Repository\CurrencyRepository;

final class CurrencyService
{
    public function __construct(
        private readonly CurrencyRepository $repository,
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
                name: $currency->fullName,
                sign: $currency->sign,
            );
        }

        return $currencyDTOs;
    }
}
