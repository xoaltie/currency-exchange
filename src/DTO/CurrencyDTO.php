<?php

namespace App\DTO;

final readonly class CurrencyDTO
{
    public function __construct(
        public int    $id,
        public string $code,
        public string $name,
        public string $sign,
    ) {}
}
