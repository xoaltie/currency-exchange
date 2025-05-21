<?php

declare(strict_types=1);

use App\Database\DatabaseConnection;

return function (\DI\ContainerBuilder $containerBuilder) {
    $containerBuilder->addDefinitions([
        'db.connection' => DI\factory(function () {
            return 'sqlite:' . __DIR__ . '/../resources/currencyExchange.db';
        }),
        PDO::class => DI\autowire()->constructor(\DI\get('db.connection')),
        DatabaseConnection::class => DI\autowire()->constructor(\DI\get(PDO::class)),
    ]);
};
