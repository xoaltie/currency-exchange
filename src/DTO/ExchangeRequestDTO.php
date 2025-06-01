<?php

namespace App\DTO;

final readonly class ExchangeRequestDTO
{
    public function __construct(
        public string $baseCurrencyCode,
        public string $targetCurrencyCode,
        public float $amount,
    ) {}
}
