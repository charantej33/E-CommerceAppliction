package com.ecommerce.application.util;

import com.ecommerce.application.entity.enums.Role;
import com.ecommerce.application.exception.ForbiddenException;
import org.springframework.stereotype.Component;

/**
 * Utility class for authorization checks
 * Follows SOLID principles: Single Responsibility
 */
@Component
public class AuthorizationUtil {

    /**
     * Check if user has the required role
     * @param userRole User's role
     * @param requiredRole Required role for operation
     * @param operation Description of the operation
     * @throws ForbiddenException if user doesn't have required role
     */
    public void checkAdminAccess(Role userRole, String operation) {
        if (userRole != Role.ADMIN) {
            throw new ForbiddenException(operation, userRole.name());
        }
    }

    /**
     * Check if user has any of the required roles
     */
    public void checkRoleAccess(Role userRole, Role... requiredRoles) {
        for (Role role : requiredRoles) {
            if (userRole == role) {
                return;
            }
        }
        throw new ForbiddenException("Access denied for role: " + userRole.name());
    }

    /**
     * Check if it's the same user or admin
     */
    public void checkUserOrAdmin(Role userRole, Long userId, Long requestedUserId) {
        if (!userId.equals(requestedUserId) && userRole != Role.ADMIN) {
            throw new ForbiddenException("Cannot access other user's data");
        }
    }
}
