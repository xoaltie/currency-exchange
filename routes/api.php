<?php

declare(strict_types=1);

use App\Controllers\CurrencyController;
use Slim\App;

return function (App $app) {
    $app->get('/currency', [CurrencyController::class, 'index']);
};
