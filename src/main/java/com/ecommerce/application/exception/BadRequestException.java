package com.ecommerce.application.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown for invalid request data (400)
 */
public class BadRequestException extends ApplicationException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST.value());
    }

    public BadRequestException(String field, String reason) {
        super(String.format("Invalid %s: %s", field, reason), HttpStatus.BAD_REQUEST.value());
    }
}
