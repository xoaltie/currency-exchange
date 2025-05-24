<?php

namespace App\Exceptions;

final class ValidateException extends \Exception
{
    /**
     * @param string $message
     * @param int $code
     */
    public function __construct(
        protected $message,
        protected $code,
    ) {
        parent::__construct($this->message, $this->code);
    }
}
