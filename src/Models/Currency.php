<?php

namespace App\Models;

final class Currency
{
    public function __construct(
        public int $id,
        public string $code,
        public string $fullName,
        public string $sign,
    ) {}
}
