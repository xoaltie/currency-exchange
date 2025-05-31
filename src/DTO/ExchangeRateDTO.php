<?php

namespace App\DTO;

final readonly class ExchangeRateDTO
{
    public function __construct(
        public int $id,
        public CurrencyDTO $baseCurrency,
        public CurrencyDTO $targetCurrency,
        public float $rate,
    ) {}
}
