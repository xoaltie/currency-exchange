<?php

namespace App\Models;

final readonly class ExchangeRate
{
    public function __construct(
        public int $id,
        public Currency $baseCurrency,
        public Currency $targetCurrency,
        public float $rate,
    ) {}
}
