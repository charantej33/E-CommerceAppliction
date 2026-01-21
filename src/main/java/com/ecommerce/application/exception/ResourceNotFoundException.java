package com.ecommerce.application.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when resource is not found (404)
 */
public class ResourceNotFoundException extends ApplicationException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s not found with id: %d", resourceName, id), HttpStatus.NOT_FOUND.value());
    }

    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s not found with %s: %s", resourceName, fieldName, fieldValue), 
              HttpStatus.NOT_FOUND.value());
    }
}
