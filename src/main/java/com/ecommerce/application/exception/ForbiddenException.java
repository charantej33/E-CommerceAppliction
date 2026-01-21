package com.ecommerce.application.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when user lacks necessary permissions (403)
 */
public class ForbiddenException extends ApplicationException {
    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN.value());
    }

    public ForbiddenException(String action, String role) {
        super(String.format("User with role %s cannot %s", role, action), 
              HttpStatus.FORBIDDEN.value());
    }
}
