<?php

namespace App\Services;

use App\DTO\CurrencyDTO;
use App\DTO\ExchangeRateCreateDTO;
use App\DTO\ExchangeRateDTO;
use App\Exceptions\ModelNotFound;
use App\Models\EmptyObject;
use App\Repository\ExchangeRateRepository;

final readonly class ExchangeRateService
{
    public function __construct(
        private ExchangeRateRepository $repository,
    ) {}

    /**
     * @return list<ExchangeRateDTO>
     */
    public function getAllExchangeRates(): array
    {
        $exchangeRates = $this->repository->getAll();

        $exchangeRateDTOs = [];

        foreach ($exchangeRates as $exchangeRate) {
            $exchangeRateDTOs[] = new ExchangeRateDTO(
                id: $exchangeRate->id,
                baseCurrency: new CurrencyDTO(
                    id: $exchangeRate->baseCurrency->id,
                    code: $exchangeRate->baseCurrency->code,
                    name: $exchangeRate->baseCurrency->name,
                    sign: $exchangeRate->baseCurrency->sign,
                ),
                targetCurrency: new CurrencyDTO(
                    id: $exchangeRate->targetCurrency->id,
                    code: $exchangeRate->targetCurrency->code,
                    name: $exchangeRate->targetCurrency->name,
                    sign: $exchangeRate->targetCurrency->sign,
                ),
                rate: $exchangeRate->rate,
            );
        }

        return $exchangeRateDTOs;
    }

    /**
     * @param string $code
     * @return ExchangeRateDTO
     * @throws ModelNotFound
     */
    public function getExchangeRateByCodes(string $pairCodes): ExchangeRateDTO
    {
        $baseCurrencyCode = substr($pairCodes, 0, 3);
        $targetCurrencyCode = substr($pairCodes, -3, 3);

        $exchangeRate = $this->repository->getByCodes($baseCurrencyCode, $targetCurrencyCode);

        if ($exchangeRate instanceof EmptyObject) {
            throw new ModelNotFound(
                message: "Exchange rate with codes {$baseCurrencyCode} and {$targetCurrencyCode} not found.",
                code: 404,
            );
        }

        return new ExchangeRateDTO(
            id: $exchangeRate->id,
            baseCurrency: new CurrencyDTO(
                id: $exchangeRate->baseCurrency->id,
                code: $exchangeRate->baseCurrency->code,
                name: $exchangeRate->baseCurrency->name,
                sign: $exchangeRate->baseCurrency->sign,
            ),
            targetCurrency: new CurrencyDTO(
                id: $exchangeRate->targetCurrency->id,
                code: $exchangeRate->targetCurrency->code,
                name: $exchangeRate->targetCurrency->name,
                sign: $exchangeRate->targetCurrency->sign,
            ),
            rate: $exchangeRate->rate,
        );
    }

    public function createExchangeRate(ExchangeRateCreateDTO $dto)
    {
        $exchangeRate = $this->repository->create($dto);

        if ($exchangeRate instanceof EmptyObject) {
            throw new ModelNotFound(
                message: "Currencies with codes {$dto->baseCurrencyCode} or {$dto->targetCurrencyCode} not found.",
                code: 404,
            );
        }

        return new ExchangeRateDTO(
            id: $exchangeRate->id,
            baseCurrency: new CurrencyDTO(
                id: $exchangeRate->baseCurrency->id,
                code: $exchangeRate->baseCurrency->code,
                name: $exchangeRate->baseCurrency->name,
                sign: $exchangeRate->baseCurrency->sign,
            ),
            targetCurrency: new CurrencyDTO(
                id: $exchangeRate->targetCurrency->id,
                code: $exchangeRate->targetCurrency->code,
                name: $exchangeRate->targetCurrency->name,
                sign: $exchangeRate->targetCurrency->sign,
            ),
            rate: $exchangeRate->rate,
        );
    }

    public function updateExchangeRate(string $pairCodes, float $rate): ExchangeRateDTO
    {
        $baseCurrencyCode = substr($pairCodes, 0, 3);
        $targetCurrencyCode = substr($pairCodes, -3, 3);

        $exchangeRate = $this->repository->update($baseCurrencyCode, $targetCurrencyCode, $rate);

        if ($exchangeRate instanceof EmptyObject) {
            throw new ModelNotFound(
                message: "Exchange rate with codes {$baseCurrencyCode} and {$targetCurrencyCode} not found.",
                code: 404,
            );
        }

        return new ExchangeRateDTO(
            id: $exchangeRate->id,
            baseCurrency: new CurrencyDTO(
                id: $exchangeRate->baseCurrency->id,
                code: $exchangeRate->baseCurrency->code,
                name: $exchangeRate->baseCurrency->name,
                sign: $exchangeRate->baseCurrency->sign,
            ),
            targetCurrency: new CurrencyDTO(
                id: $exchangeRate->targetCurrency->id,
                code: $exchangeRate->targetCurrency->code,
                name: $exchangeRate->targetCurrency->name,
                sign: $exchangeRate->targetCurrency->sign,
            ),
            rate: $exchangeRate->rate,
        );
    }
}
