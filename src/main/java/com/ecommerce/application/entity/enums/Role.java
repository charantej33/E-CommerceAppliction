package com.ecommerce.application.entity.enums;

/**
 * Enum representing user roles in the system
 * ADMIN: Full access to all admin operations
 * CUSTOMER: Limited access to customer operations only
 */
public enum Role {
    ADMIN("ROLE_ADMIN"),
    CUSTOMER("ROLE_CUSTOMER");

    private final String roleValue;

    Role(String roleValue) {
        this.roleValue = roleValue;
    }

    public String getRoleValue() {
        return roleValue;
    }

    public static Role fromString(String value) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(value) || role.roleValue.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + value);
    }
}
