<?php

namespace App\Utils\Validators;

use App\Exceptions\ValidateException;

final class RequestValidator
{
    private function __construct() {}

    /**
     * @param array<string, string> $rules
     * @param array<string, string> $data
     * @return array<string, mixed>
     * @throws ValidateException
     */
    public static function validate(array $rules, array $data): array
    {
        foreach ($rules as $field => $rule) {
            if (!array_key_exists($field, $data)) {
                throw new ValidateException(
                    "Request don't have {" . $field . "} field.",
                    400,
                );
            }
        }

        return $data;
    }
}
