<?php

declare(strict_types=1);

use App\Controllers\CurrencyController;
use App\Controllers\ExchangeRateController;
use App\Middlewares\ApiMiddleware;
use Slim\App;
use Slim\Routing\RouteCollectorProxy;

return function (App $app) {
    $app->group('', function (RouteCollectorProxy $group) {
        $group->get('/currencies', [CurrencyController::class, 'index']);
        $group->get('/currency/{code}', [CurrencyController::class, 'show']);
        $group->post('/currencies', [CurrencyController::class, 'store']);

        $group->get('/exchangeRates', [ExchangeRateController::class, 'index']);
        $group->get('/exchangeRate/{pairCodes}', [ExchangeRateController::class, 'show']);
        $group->post('/exchangeRates', [ExchangeRateController::class, 'store']);
        $group->patch('/exchangeRate/{pairCodes}', [ExchangeRateController::class, 'update']);
    })->add(new ApiMiddleware());
};
