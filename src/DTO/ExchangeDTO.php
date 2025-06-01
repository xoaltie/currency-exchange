<?php

namespace App\DTO;

final readonly class ExchangeDTO
{
    public CurrencyDTO $baseCurrency;
    public CurrencyDTO $targetCurrency;
    public float $rate;
    public float $amount;
    public float $convertedAmount;

    public function __construct(
        CurrencyDTO $baseCurrency,
        CurrencyDTO $targetCurrency,
        float $rate,
        float $amount,
        float $convertedAmount,
    ) {
        $this->baseCurrency = $baseCurrency;
        $this->targetCurrency = $targetCurrency;
        $this->rate = round($rate, 2);
        $this->amount = round($amount, 2);
        $this->convertedAmount = round($convertedAmount, 2);
    }
}
