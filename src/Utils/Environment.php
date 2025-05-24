<?php

namespace App\Utils;

use Symfony\Component\Dotenv\Dotenv;

final class Environment
{
    private const string PATH = __DIR__ . '/../../.env';
    public function __construct(
        private readonly Dotenv $dotenv,
    ) {
        echo "Environment constructor called\n";
    }


    public static function get(string $key, string|null $default): string
    {
        if (!array_key_exists($key, $_ENV) && $default === null) {
            echo "{$key} does not exist in .env";
            exit();
        }

        return $_ENV[$key] ?? $default;
    }

    public function loadEnv(): void
    {
        echo Environment::class . " method loadEnv";
        try {
            $this->dotenv->load(self::PATH);
        } catch (\Exception $exception) {
            echo $exception->getMessage();
            exit();
        }
    }
}
