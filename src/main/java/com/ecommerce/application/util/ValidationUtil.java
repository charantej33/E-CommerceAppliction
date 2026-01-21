package com.ecommerce.application.util;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.ecommerce.application.exception.BadRequestException;

@Component
public class ValidationUtil {

    /**
     * Validate email format
     */
    public void validateEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new BadRequestException("email", "Invalid email format");
        }
    }

    /**
     * Validate password strength
     */
    public void validatePassword(String password) {
        if (password == null || password.length() < 6) {
            throw new BadRequestException("password", "Password must be at least 6 characters long");
        }
    }

    /**
     * Validate not null or empty string
     */
    public void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new BadRequestException(fieldName, "Cannot be empty");
        }
    }

    /**
     * Validate positive price
     */
    public void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("price", "Price must be greater than 0");
        }
    }

    /**
     * Validate non-negative stock
     */
    public void validateStock(Integer stock) {
        if (stock == null || stock < 0) {
            throw new BadRequestException("stock", "Stock cannot be negative");
        }
    }

    /**
     * Validate positive integer
     */
    public void validatePositive(Integer value, String fieldName) {
        if (value == null || value <= 0) {
            throw new BadRequestException(fieldName, "Must be greater than 0");
        }
    }
}
