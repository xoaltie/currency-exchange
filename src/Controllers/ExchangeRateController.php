<?php

namespace App\Controllers;

use App\DTO\ExchangeRateCreateDTO;
use App\Exceptions\ModelNotFound;
use App\Repository\ExchangeRateRepository;
use App\Responses\ErrorResponse;
use App\Services\ExchangeRateService;
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;

final readonly class ExchangeRateController
{
    public function __construct(
        private ExchangeRateService $service,
        private ExchangeRateRepository $repository,
    ) {}

    public function index(Request $request, Response $response): Response
    {
        $exchangeRates = $this->repository->getAll();

        $response ->getBody()->write(
            json_encode($exchangeRates),
        );

        return $response;
    }

    public function show(Request $request, Response $response, array $args): Response
    {
        $pairCodes = $args['pairCodes'];

        try {
            $exchangeRate = $this->service->getExchangeRateByCodes($pairCodes);

            $response->getBody()->write(
                json_encode($exchangeRate),
            );
        } catch (ModelNotFound $exception) {
            return new ErrorResponse(
                $exception->getMessage(),
                $exception->getCode(),
            );
        }

        return $response;
    }

    public function store(Request $request, Response $response): Response
    {
        $body = $request->getParsedBody();

        try {
            $newExchangeRate = $this->service->createExchangeRate(new ExchangeRateCreateDTO(
                baseCurrencyCode: $body['baseCurrencyCode'],
                targetCurrencyCode: $body['targetCurrencyCode'],
                rate: (float) $body['rate'],
            ));

            $response->getBody()->write(
                json_encode($newExchangeRate),
            );
        } catch (ModelNotFound $exception) {
            return new ErrorResponse(
                $exception->getMessage(),
                $exception->getCode(),
            );
        }

        return $response
            ->withStatus(201);
    }

    public function update(Request $request, Response $response, array $args): Response
    {
        $pairCodes = $args['pairCodes'];
        $body = $request->getParsedBody();

        try {
            $updatedExchangeRate = $this->service->updateExchangeRate($pairCodes, (float) $body['rate']);

            $response->getBody()->write(
                json_encode($updatedExchangeRate),
            );
        } catch (ModelNotFound $exception) {
            return new ErrorResponse(
                $exception->getMessage(),
                $exception->getCode(),
            );
        }

        return $response;
    }
}
