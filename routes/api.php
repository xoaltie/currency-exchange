<?php

declare(strict_types=1);

use App\Controllers\CurrencyController;
use App\Middlewares\ApiMiddleware;
use Slim\App;
use Slim\Routing\RouteCollectorProxy;

return function (App $app) {
    $app->group('', function (RouteCollectorProxy $group) {
        $group->get('/currencies', [CurrencyController::class, 'index']);
        $group->get('/currency/{code}', [CurrencyController::class, 'show']);
        $group->post('/currencies', [CurrencyController::class, 'store']);
    })->add(new ApiMiddleware());
};
