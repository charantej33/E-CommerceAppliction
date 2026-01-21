package com.ecommerce.application.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when authentication fails (401)
 */
public class UnauthorizedException extends ApplicationException {
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED.value());
    }

    public UnauthorizedException() {
        super("Invalid credentials", HttpStatus.UNAUTHORIZED.value());
    }
}
