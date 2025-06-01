<?php

namespace App\Controllers;

use App\DTO\CurrencyCreateDTO;
use App\Exceptions\CurrencyException;
use App\Exceptions\ValidateException;
use App\Responses\ErrorResponse;
use App\Services\CurrencyService;
use App\Utils\Validators\RequestValidator;
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;

final readonly class CurrencyController
{
    public function __construct(
        private CurrencyService $service,
    ) {}

    public function index(Request $request, Response $response): Response
    {

        $currencies = $this->service->getAllCurrencies();

        $response->getBody()->write(json_encode($currencies));

        return $response;
    }

    /**
     * @param Request $request
     * @param Response $response
     * @param array{
     *     code: string
     * } $args
     * @return Response
     */
    public function show(Request $request, Response $response, array $args): Response
    {
        try {
            $validated = RequestValidator::validate([
                'code' => 'string',
            ], $args);

            $currency = $this->service->getCurrencyByCode($validated['code']);

            $response->getBody()->write(json_encode($currency));
        } catch (CurrencyException $exception) {
            return new ErrorResponse(
                message: $exception->getMessage(),
                status: $exception->getCode(),
            );
        }

        return $response;
    }

    public function store(Request $request, Response $response): Response
    {
        try {
            $validated = RequestValidator::validate(
                [
                    'code' => 'string',
                    'name' => 'string',
                    'sign' => 'string',
                ],
                $request->getParsedBody(),
            );

            $currency = $this->service->createCurrency(new CurrencyCreateDTO(
                code: $validated['code'],
                name: $validated['name'],
                sign: $validated['sign'],
            ));
        } catch (ValidateException|CurrencyException $exception) {
            return new ErrorResponse(
                $exception->getMessage(),
                $exception->getCode(),
            );
        }

        $response->getBody()->write(json_encode($currency));

        return $response
            ->withStatus(201);
    }
}
