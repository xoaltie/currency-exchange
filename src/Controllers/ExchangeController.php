<?php

namespace App\Controllers;

use App\DTO\ExchangeRequestDTO;
use App\Responses\ErrorResponse;
use App\Services\ExchangeService;
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;

final readonly class ExchangeController
{
    public function __construct(
        private ExchangeService $service,
    ) {}

    public function __invoke(Request $request, Response $response): Response
    {
        $query = $request->getQueryParams();

        try {
            $result = $this->service->exchange(new ExchangeRequestDTO(
                baseCurrencyCode: $query['from'],
                targetCurrencyCode: $query['to'],
                amount: $query['amount'],
            ));

            $response->getBody()->write(json_encode($result));
        } catch (\Exception $exception) {
            return new ErrorResponse(
                $exception->getMessage(),
                $exception->getCode(),
            );
        }

        return $response;
    }
}
