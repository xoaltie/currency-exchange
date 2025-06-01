<?php

namespace App\Services;

use App\DTO\CurrencyDTO;
use App\DTO\ExchangeDTO;
use App\DTO\ExchangeRequestDTO;
use App\Exceptions\ExchangeRateException;
use App\Exceptions\ModelNotFound;
use App\Models\EmptyObject;
use App\Repository\CurrencyRepository;
use App\Repository\ExchangeRateRepository;

final readonly class ExchangeService
{
    public function __construct(
        private ExchangeRateService $exchangeRateService,
    ) {}

    /**
     * @throws ModelNotFound
     */
    public function exchange(ExchangeRequestDTO $dto)
    {
        try {
            $forwardExchange = $this->exchangeRateService->getExchangeRateByCodes($dto->baseCurrencyCode . $dto->targetCurrencyCode);

            return new ExchangeDTO(
                baseCurrency: new CurrencyDTO(
                    id: $forwardExchange->baseCurrency->id,
                    code: $forwardExchange->baseCurrency->code,
                    name: $forwardExchange->baseCurrency->name,
                    sign: $forwardExchange->baseCurrency->sign,
                ),
                targetCurrency: new CurrencyDTO(
                    id: $forwardExchange->targetCurrency->id,
                    code: $forwardExchange->targetCurrency->code,
                    name: $forwardExchange->targetCurrency->name,
                    sign: $forwardExchange->targetCurrency->sign,
                ),
                rate: $forwardExchange->rate,
                amount: $dto->amount,
                convertedAmount: $forwardExchange->rate * $dto->amount,
            );
        } catch (ExchangeRateException $exception) {
        }

        try {
            $backwardExchange = $this->exchangeRateService->getExchangeRateByCodes($dto->targetCurrencyCode . $dto->baseCurrencyCode);

            return new ExchangeDTO(
                baseCurrency: new CurrencyDTO(
                    id: $backwardExchange->baseCurrency->id,
                    code: $backwardExchange->baseCurrency->code,
                    name: $backwardExchange->baseCurrency->name,
                    sign: $backwardExchange->baseCurrency->sign,
                ),
                targetCurrency: new CurrencyDTO(
                    id: $backwardExchange->targetCurrency->id,
                    code: $backwardExchange->targetCurrency->code,
                    name: $backwardExchange->targetCurrency->name,
                    sign: $backwardExchange->targetCurrency->sign,
                ),
                rate: 1 / $backwardExchange->rate,
                amount: $dto->amount,
                convertedAmount: (1 / $backwardExchange->rate) * $dto->amount,
            );
        } catch (ExchangeRateException $exception) {
        }

        $crossExchange = $this->exchangeRateService->getCrossExchangeRateByCodes($dto->baseCurrencyCode . $dto->targetCurrencyCode);

        return new ExchangeDTO(
            baseCurrency: new CurrencyDTO(
                id: $crossExchange->baseCurrency->id,
                code: $crossExchange->baseCurrency->code,
                name: $crossExchange->baseCurrency->name,
                sign: $crossExchange->baseCurrency->sign,
            ),
            targetCurrency: new CurrencyDTO(
                id: $crossExchange->targetCurrency->id,
                code: $crossExchange->targetCurrency->code,
                name: $crossExchange->targetCurrency->name,
                sign: $crossExchange->targetCurrency->sign,
            ),
            rate: $crossExchange->rate,
            amount: $dto->amount,
            convertedAmount: $crossExchange->rate * $dto->amount,
        );

        //        $baseCurrency = $this->currencyRepository->getByCode($dto->baseCurrencyCode);
        //        $targetCurrency = $this->currencyRepository->getByCode($dto->targetCurrencyCode);
        //
        //        if ($baseCurrency instanceof EmptyObject || $targetCurrency instanceof EmptyObject) {
        //            throw new ModelNotFound(
        //                message: "Currency not found.",
        //                code: 404,
        //            );
        //        }
        //
        //        $exchangeRateForward = $this->exchangeRateRepository->getByCodes($dto->baseCurrencyCode, $dto->targetCurrencyCode);
        //
        //        if (!$exchangeRateForward instanceof EmptyObject) {
        //            return new ExchangeDTO(
        //                baseCurrency: new CurrencyDTO(
        //                    id: $baseCurrency->id,
        //                    code: $baseCurrency->code,
        //                    name: $baseCurrency->name,
        //                    sign: $baseCurrency->sign,
        //                ),
        //                targetCurrency: new CurrencyDTO(
        //                    id: $targetCurrency->id,
        //                    code: $targetCurrency->code,
        //                    name: $targetCurrency->name,
        //                    sign: $targetCurrency->sign,
        //                ),
        //                rate: $exchangeRateForward->rate,
        //                amount: $dto->amount,
        //                convertedAmount: $exchangeRateForward->rate * $dto->amount,
        //            );
        //        }
        //
        //        $exchangeRateBackward = $this->exchangeRateRepository->getByCodes($dto->targetCurrencyCode, $dto->baseCurrencyCode);
        //
        //        if ($exchangeRateBackward instanceof EmptyObject) {
        //            throw new ModelNotFound(
        //                message: "Currency not found.",
        //                code: 404,
        //            );
        //        }
        //
        //        return new ExchangeDTO(
        //            baseCurrency: new CurrencyDTO(
        //                id: $baseCurrency->id,
        //                code: $baseCurrency->code,
        //                name: $baseCurrency->name,
        //                sign: $baseCurrency->sign,
        //            ),
        //            targetCurrency: new CurrencyDTO(
        //                id: $targetCurrency->id,
        //                code: $targetCurrency->code,
        //                name: $targetCurrency->name,
        //                sign: $targetCurrency->sign,
        //            ),
        //            rate: 1 / $exchangeRateBackward->rate,
        //            amount: $dto->amount,
        //            convertedAmount: (1 / $exchangeRateBackward->rate) * $dto->amount,
        //        );
    }
}
