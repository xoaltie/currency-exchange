<?php

namespace App\DTO;

final readonly class CurrencyCreateDTO
{
    public function __construct(
        public string $code,
        public string $name,
        public string $sign,
    ) {}
}
