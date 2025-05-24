<?php

namespace App\Responses;

use Fig\Http\Message\StatusCodeInterface;
use Psr\Http\Message\StreamInterface;
use Slim\Psr7\Interfaces\HeadersInterface;
use Slim\Psr7\Response;

final class ErrorResponse extends Response
{
    //    public function __construct(string $message, int $status)
    //    {
    //        $this->getBody()->write(json_encode([
    //            'error' => $message
    //        ]));
    //
    //        $this->withStatus($status);
    //    }

    public function __construct(string $message, int $status = StatusCodeInterface::STATUS_OK, ?HeadersInterface $headers = null, ?StreamInterface $body = null)
    {
        parent::__construct($status, $headers, $body);

        $this->getBody()->write(json_encode([
            'error' => $message,
        ]));

        $this->withStatus($status);
    }
}
