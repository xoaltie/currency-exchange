<?php

namespace App\DTO;

final readonly class ExchangeRateCreateDTO
{
    public function __construct(
        public string $baseCurrencyCode,
        public string $targetCurrencyCode,
        public float $rate,
    ) {}
}
