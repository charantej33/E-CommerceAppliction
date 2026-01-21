package com.ecommerce.application.entity.enums;

public enum OrderStatus {
    CREATED("Order created but not confirmed"),
    CONFIRMED("Order confirmed and ready for processing"),
    CANCELLED("Order cancelled");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
