<?php

namespace App\Validators;

use App\Exceptions\ValidateException;

final class RequestBodyValidator
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
                    "Form don't have " . $field . " field in body.",
                    400,
                );
            }
        }

        return $data;
    }
}
