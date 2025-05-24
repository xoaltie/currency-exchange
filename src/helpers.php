<?php

declare(strict_types=1);

use App\Utils\Environment;

if (!function_exists('env')) {
    function env(string $key, string $default = null): string
    {
        return Environment::get($key, $default);
    }
}
