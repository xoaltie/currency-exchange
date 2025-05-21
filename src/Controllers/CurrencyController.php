<?php

namespace App\Controllers;

use App\Services\CurrencyService;
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;

final class CurrencyController
{
    public function __construct(
        private readonly CurrencyService $service,
    ) {}

    public function index(Request $request, Response $response): Response
    {

        $currencies = $this->service->getAllCurrencies();

        $response->getBody()->write(
            json_encode($currencies),
        );

        return $response
            ->withHeader('Content-Type', 'application/json')
            ->withStatus(200);
    }
}
